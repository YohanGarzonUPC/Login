package co.edu.unipiloto.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritosActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private RecyclerView recyclerViewFav;
    private AdapterPublicacionesFavoritos adapter;
    private List<Publicacion> listaPublicacionesFavoritas=new ArrayList<>();
    static RequestQueue requestQueue;
    private List<String> listid = new ArrayList<>();

    private UserManager userManager= UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        requestQueue = Volley.newRequestQueue(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);


        List<Publicacion> publicaciones = userManager.getPublications();
        String name = userManager.getName();
        String phone = userManager.getPhone();
        String email = userManager.getEmail();
        String type = userManager.getType();

        TextView textViewName = findViewById(R.id.nombrefavoritos);
        textViewName.setText(name);

        TextView textViewPhone = findViewById(R.id.textViewPhoneF);
        textViewPhone.setText(phone);

        TextView textViewEmail = findViewById(R.id.textViewEmailF);
        textViewEmail.setText(email);

        TextView textViewType = findViewById(R.id.textViewTypeF);
        textViewType.setText(type);
        recyclerViewFav = findViewById(R.id.recyclerViewFavoritos);
        adapter = new AdapterPublicacionesFavoritos(this, publicaciones);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFav.setAdapter(adapter);

    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.nav_Home:
                UserManager userManager = UserManager.getInstance();
                Intent intent;
                String type = userManager.getType();
                if (type.equalsIgnoreCase("Conductor")){
                    intent = new Intent(this, MapsActivity.class);
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
                startActivity(new Intent(this,SolicitudActivity.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            default:
                return false;
        }
    }

    // Método para obtener las publicaciones favoritas (implementa según tus necesidades)



    @Override
    protected void onResume() {
        String correo = userManager.getEmail();
        super.onResume();
        obteneridPublicaciones(correo);


    }



    private void obteneridPublicaciones(String correo) {

        String URL = "http://192.168.1.22/rodo/getidEmail.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject userObject = jsonArray.getJSONObject(i);
                                listid.add(userObject.getString("idPublicacion"));
                            }
                            for (String id : listid) {
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
                params.put("correo", correo); // Asegúrate de cambiar esto por el correo actual que necesitas enviar
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }




    private void obtenerPublicaciones(String id) {
        UserManager userManager = UserManager.getInstance();
        String email = userManager.getEmail();

        String url = "http://192.168.1.22/rodo/getPublicacionesId.php";

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
                                    listaPublicacionesFavoritas.add(publicacion);
                                }
                                // Aquí actualizas el adaptador de tu RecyclerView
                                runOnUiThread(() -> {
                                    adapter.updateData(listaPublicacionesFavoritas);
                                });

                            } else {
                                String errorMessage = jsonResponse.optString("message", "Error desconocido");
                                Toast.makeText(FavoritosActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(FavoritosActivity.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FavoritosActivity.this, "Error en la conexión con el servidor.", Toast.LENGTH_LONG).show();
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

}