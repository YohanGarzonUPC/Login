package co.edu.unipiloto.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElegirConductor extends AppCompatActivity {

    private Spinner spinner;
    private Button button;
    private List<String> listaConductores;
    private RequestQueue requestQueue;
    private String jefe;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_conductor);

        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.button);
        Intent intent = getIntent();
        id = intent.getStringExtra("clave");
        listaConductores = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext()); // Usar contexto de aplicación
        jefe = UserManager.getInstance().getEmail(); // Obtener email del UserManager

        obtenerConductores();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedConductor = spinner.getSelectedItem().toString();
                asignarCarga(selectedConductor,id);
                Intent intent = new Intent(ElegirConductor.this, Home.class);
                startActivity(intent);

            }
        });
    }
    private void asignarCarga(String conductor, String idPublicacion) {
        String URL = "http://192.168.56.1/rodo/asignarCargaConductor.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ElegirConductor.this, "Correcto", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ElegirConductor.this, "Error en la solicitud HTTP", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("conductor", conductor);
                params.put("idPublicacion", idPublicacion);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void obtenerConductores() {
        String URL = "http://192.168.56.1/rodo/buscarConductor.php?jefe=" + jefe;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            listaConductores.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                listaConductores.add(userObject.getString("conductor"));
                            }
                            // Configurar el adaptador del Spinner después de obtener la lista de conductores
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ElegirConductor.this, android.R.layout.simple_spinner_item, listaConductores);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Manejar el error de manera más elegante
                        Toast.makeText(ElegirConductor.this, "Error al obtener conductores", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}