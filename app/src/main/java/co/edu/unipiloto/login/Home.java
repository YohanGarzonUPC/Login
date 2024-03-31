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
import android.view.MenuItem;
import android.view.View;
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

public class Home extends AppCompatActivity implements AdapterPublicaciones.OnConductorButtonClickListener {

    private DrawerLayout drawerLayout;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    static int contador = 0;
    static RequestQueue requestQueue;
    private AdapterPublicaciones adapter;
    private static List<Publicacion> listaPublicaciones = new ArrayList<>();
    String correo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        correo =getIntent().getStringExtra("usuario");

        requestQueue = Volley.newRequestQueue(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        nestedScrollView = findViewById(R.id.nsvVista);
        recyclerView = findViewById(R.id.rvUsuarios);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        progressBar = findViewById(R.id.pbCargando);

        ObtenerPublicacion(recyclerView,Home.this);
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
                                ObtenerPublicacion(recyclerView,Home.this);
                            }
                        }
                    }, 3000);
                }
            }
        });
        adapter = new AdapterPublicaciones(this,listaPublicaciones);
        adapter.setOnConductorButtonClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public static void ObtenerPublicacion(RecyclerView recyclerView, Context context) {
        readUser(recyclerView, context);
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

    private static void readUser(RecyclerView recyclerView, Context context) {
        String URL = "http://192.168.0.13/rodo/traerPublicaciones.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0;i<response.length();i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                Publicacion publicacion = new Publicacion();
                                publicacion.setNombre("producto: "+userObject.getString("name"));
                                publicacion.setOrigen("Origen: "+userObject.getString("origen"));
                                publicacion.setDestino("Destino: "+userObject.getString("destino"));
                                publicacion.setPeso("Peso: "+userObject.getInt("peso"));
                                publicacion.setDescripcion("Descripcion: "+userObject.getString("detalles"));
                                publicacion.setFecha(userObject.getString("fecha"));
                                publicacion.setIdPublicacion(userObject.getInt("id"));
                                listaPublicaciones.add(publicacion);
                            }
                            recyclerView.setAdapter(new AdapterPublicaciones(context,listaPublicaciones));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onConductorButtonClick(int position) {
        Publicacion publicacion= listaPublicaciones.get(position);

        agregarConductor(publicacion);
    }

    private void agregarConductor(Publicacion publicacion) {

    }
}