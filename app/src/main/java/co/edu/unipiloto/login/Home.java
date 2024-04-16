package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.jvm.internal.markers.KMutableList;

public class Home extends AppCompatActivity implements AdapterPublicaciones.OnConductorButtonClickListener {

    private DrawerLayout drawerLayout;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    static int contador = 0;

    UserManager userManager;
    static RequestQueue requestQueue;
    private AdapterPublicaciones adapter;
    private static List<Publicacion> listaPublicaciones = new ArrayList<>();
    private static List<String> listaIds = new ArrayList<>();
    private String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestQueue = Volley.newRequestQueue(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        nestedScrollView = findViewById(R.id.nsvVista);
        recyclerView = findViewById(R.id.rvUsuarios);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressBar = findViewById(R.id.pbCargando);
        userManager = UserManager.getInstance();
        obtenerPublicaciones();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    contador++;
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (contador < 10) {
                                obtenerPublicaciones();
                            }
                        }
                    }, 3000);
                }
            }
        });
    }

    private void obtenerPublicaciones() {
        String URL = "http://192.168.0.15/rodo/traerPublicaciones.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listaPublicaciones.clear();
                            for (int i=0; i<response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                Publicacion publicacion = new Publicacion();
                                publicacion.setNombre("producto: " + userObject.getString("name"));
                                publicacion.setOrigen("Origen: " + userObject.getString("origen"));
                                publicacion.setDestino("Destino: " + userObject.getString("destino"));
                                publicacion.setPeso("Peso: " + userObject.getInt("peso"));
                                publicacion.setDescripcion("Descripcion: " + userObject.getString("detalles"));
                                publicacion.setFecha(userObject.getString("fecha"));
                                publicacion.setIdPublicacion(userObject.getInt("id"));
                                listaPublicaciones.add(publicacion);
                            }
                            ActualizarHome();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void actualizarListaPublicaciones() {
        if (adapter == null) {
            adapter = new AdapterPublicaciones(this, listaPublicaciones, null);
            adapter.setOnConductorButtonClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConductorButtonClick(Publicacion publicacion) {
        agregarConductor(publicacion);
    }

    private void agregarConductor(Publicacion publicacion) {
        Intent intent = new Intent(Home.this, ElegirConductor.class);
        int idPublicacion = publicacion.getIdPublicacion();
        intent.putExtra("clave", idPublicacion);
        String URL = "http://192.168.0.15/rodo/actualizarConductor.php";
        String email=userManager.getEmail();
        enviar(String.valueOf(idPublicacion), email, URL);
        startActivity(intent);
    }

    private void enviar(final String idPublicacion, final String email, final String URL1) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Home.this, "Correcto", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idPublicacion", idPublicacion);
                params.put("correo", email);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.nav_Home:
                startActivity(new Intent(this, Home.class));
                return true;
            case R.id.nav_Favoritos:
                startActivity(new Intent(this, FavoritosActivity.class));
                return true;
            case R.id.nav_formulario:
                startActivity(new Intent(this, SolicitudActivity.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            default:
                return false;
        }
    }
    private void ActualizarHome() {
        String URL = "http://192.168.0.15/rodo/actualizarHome.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listaIds.clear();
                            for (int i=0; i<response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                listaIds.add(userObject.getString("idPublicacion"));
                            }
                            for (int i=0;i<listaPublicaciones.size();i++){
                                for (int j=0;j<listaIds.size();j++){
                                    String idPublicacionActual =""+ listaPublicaciones.get(i).getIdPublicacion();
                                    String idPubliAceptada = listaIds.get(j);
                                if (idPublicacionActual.equals(idPubliAceptada)) {
                                    listaPublicaciones.remove(listaPublicaciones.get(i));
                                }
                                }
                            }
                            actualizarListaPublicaciones();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}
