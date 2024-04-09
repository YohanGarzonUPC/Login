package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class PerfilActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerViewPublicaciones;
    private AdapterPublicacionesPerfil adapterPublicacionesPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        drawerLayout=findViewById(R.id.drawer_layout_perfil);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        UserManager userManager = UserManager.getInstance();
        List<Publicacion> publicaciones = userManager.getPublications();
        String name = userManager.getName();
        String phone = userManager.getPhone();
        String email = userManager.getEmail();
        String type = userManager.getType();

        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(name);

        TextView textViewPhone = findViewById(R.id.textViewPhone);
        textViewPhone.setText(phone);

        TextView textViewEmail = findViewById(R.id.textViewEmail);
        textViewEmail.setText(email);

        TextView textViewType = findViewById(R.id.textViewType);
        textViewType.setText(type);
        recyclerViewPublicaciones = findViewById(R.id.recyclerViewPublicaciones);
        adapterPublicacionesPerfil = new AdapterPublicacionesPerfil(this, publicaciones);

        recyclerViewPublicaciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPublicaciones.setAdapter(adapterPublicacionesPerfil);


    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.nav_Home:
                startActivity(new Intent(this, Home.class));
                return true;
            case R.id.nav_Favoritos:
                startActivity(new Intent(this, FavoritosActivity.class));
                return true;
            case R.id.nav_formulario:
                startActivity(new Intent(this,SolicitudActivity.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerPublicaciones();
    }

    private void obtenerPublicaciones() {
        UserManager userManager = UserManager.getInstance();
        String email = userManager.getEmail();

        String url = "http://192.168.0.15/rodo/getPublicacionesEmail.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.optString("status");
                            if ("success".equals(status)) {
                                JSONArray publicacionesArray = jsonResponse.getJSONArray("data");
                                List<Publicacion> publicationList = new ArrayList<>();
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
                                    publicationList.add(publicacion);
                                }
                                // Aquí actualizas el adaptador de tu RecyclerView
                                runOnUiThread(() -> {
                                    adapterPublicacionesPerfil.updateData(publicationList);
                                });
                            } else {
                                String errorMessage = jsonResponse.optString("message", "Error desconocido");
                                Toast.makeText(PerfilActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(PerfilActivity.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PerfilActivity.this, "Error en la conexión con el servidor.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}