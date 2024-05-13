package co.edu.unipiloto.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class CargasAsignadas extends AppCompatActivity implements AdapterPublicaciones.OnConductorButtonClickListener {

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
        setContentView(R.layout.activity_cargas_asignadas);

        requestQueue = Volley.newRequestQueue(this);

        drawerLayout = findViewById(R.id.drawer_layoutcargas);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        nestedScrollView = findViewById(R.id.nsvVistacargas);
        recyclerView = findViewById(R.id.rvUsuarioscargas);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressBar = findViewById(R.id.pbCargandocargas);
        userManager = UserManager.getInstance();
        // Create a Uri from an intent string. Use the result to create an Intent.

        Log.d("correo",userManager.getEmail());
        obteneridPublicaciones(userManager.getEmail());

    }


    private void obteneridPublicaciones(String conductor) {

        String URL = "http://192.168.56.1/rodo/cargasasignadas.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject userObject = jsonArray.getJSONObject(i);
                                listaIds.add(userObject.getString("idPublicacion"));
                            }
                            for (String id : listaIds) {
                                Log.d("ID", id);
                                obtenerPublicaciones(id);
                            }
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("conductor", conductor); // Asegúrate de cambiar esto por el correo actual que necesitas enviar
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void obtenerPublicaciones(String id) {
        UserManager userManager = UserManager.getInstance();
        String email = userManager.getEmail();

        String url = "http://192.168.56.1/rodo/getPublicacionesId.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        listaPublicaciones.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.optString("status");
                            if ("success".equals(status)) {
                                JSONArray publicacionesArray = jsonResponse.getJSONArray("data");
                                for (int i = 0; i < publicacionesArray.length(); i++) {
                                    JSONObject publicationJson = publicacionesArray.getJSONObject(i);
                                    Publicacion publicacion = new Publicacion(
                                            publicationJson.optString("name"),
                                            publicationJson.optString("origen"),
                                            publicationJson.optString("destino"),
                                            publicationJson.optString("peso"),
                                            publicationJson.optString("detalles"),
                                            publicationJson.optString("fecha"),
                                            publicationJson.optInt("idPublicacion")
                                    );


                                    listaPublicaciones.add(publicacion);
                                }

                                actualizarListaPublicaciones();
                                // Aquí actualizas el adaptador de tu RecyclerView


                            } else {
                                String errorMessage = jsonResponse.optString("message", "Error desconocido");
                                Toast.makeText(CargasAsignadas.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CargasAsignadas.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CargasAsignadas.this, "Error en la conexión con el servidor.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        requestQueue.add(stringRequest);
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

    private void agregarConductor(Publicacion publicacion) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin", publicacion.getOrigen())
                .appendQueryParameter("destination", publicacion.getDestino());
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onConductorButtonClick(Publicacion publicacion) {
        agregarConductor(publicacion);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.nav_Home:
                UserManager userManager = UserManager.getInstance();
                Intent intent;
                String type = userManager.getType();
                if (type.equalsIgnoreCase("Conductor")){
                    intent= new Intent(this,CargasAsignadas.class);

                } else if (type.equalsIgnoreCase("Propietario de Camión")) {
                    intent = new Intent(this, Home.class);
                }else{
                    intent = new Intent(this, Home.class);
                }
                startActivity(intent);
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
}
