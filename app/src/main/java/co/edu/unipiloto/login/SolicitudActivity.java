package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class SolicitudActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ORIGIN = 1;
    private static final int REQUEST_CODE_DESTINATION = 2;
    EditText nameEditText;
    EditText origenEditText;
    EditText destinoEditText;
    EditText pesoEditText;
    EditText detallesEditText;
    String correo;

    Button accederButton;
    RequestQueue requestQueue;

    private static final String URL1="http://192.168.56.1/rodo/cargas.php";
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);  // Asegúrate de llamar setContentView una sola vez con el layout correcto
        Log.d("SolicitudActivity", "onCreate iniciado");

        // Inicializar las vistas
        drawerLayout = findViewById(R.id.drawer_layout_formulario);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        origenEditText = findViewById(R.id.originEditText);
        destinoEditText = findViewById(R.id.destinationEditText);
        nameEditText = findViewById(R.id.nameEditText);
        pesoEditText = findViewById(R.id.weightEditText);
        detallesEditText = findViewById(R.id.descriptionEditText);
        accederButton = findViewById(R.id.submitButton);
        requestQueue = Volley.newRequestQueue(this);

        if (origenEditText == null || destinoEditText == null) {
            Log.e("SolicitudActivity", "Una o más entradas de EditText son nulas.");
            return;
        }

        // Configuración de los listeners
        origenEditText.setOnClickListener(v -> {
            Intent intent = new Intent(SolicitudActivity.this, MapsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ORIGIN);
        });

        destinoEditText.setOnClickListener(v -> {
            Intent intent = new Intent(SolicitudActivity.this, MapsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_DESTINATION);
        });

        accederButton.setOnClickListener(this);

        Log.d("SolicitudActivity", "Listeners establecidos");
    }

    private void InitUI() {
        UserManager userManager = UserManager.getInstance();

        nameEditText= findViewById(R.id.nameEditText);
        origenEditText = findViewById(R.id.originEditText);
        destinoEditText = findViewById(R.id.destinationEditText);
        pesoEditText = findViewById(R.id.weightEditText);
        detallesEditText = findViewById(R.id.descriptionEditText);
        correo = userManager.getEmail();

        accederButton = findViewById(R.id.submitButton);
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
                startActivity(new Intent(this, SolicitudActivity.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            default:
                return false;
        }
    }

    private boolean validarCampos() {


        String name = nameEditText.getText().toString().trim();
        String origen = origenEditText.getText().toString().trim();
        String destine = destinoEditText.getText().toString().trim();
        String peso = pesoEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Ingrese el producto");
            return false;
        }

        if (origen.isEmpty()) {
            origenEditText.setError("Ingrese el origen");
            return false;
        }

        if (destine.isEmpty()) {
            destinoEditText.setError("Ingrese el destino");
            return false;
        }

        if (peso.isEmpty()) {
            pesoEditText.setError("Ingrese el peso");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.submitButton) {
            String name = nameEditText.getText().toString().trim();
            String origen = origenEditText.getText().toString().trim();
            String destine = destinoEditText.getText().toString().trim();
            String peso = pesoEditText.getText().toString().trim();
            String detalle = detallesEditText.getText().toString().trim();


            if (validarCampos()) {
                createApliccation(name,origen,destine,peso,detalle,correo);
            }
        }
    }

    private void createApliccation(final String name, final String origen, final String destine, final String peso, final String detalle,final String correo) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Toast.makeText(SolicitudActivity.this,"Correcto", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("origen",origen);
                params.put("destino",destine);
                params.put("peso",peso);
                params.put("detalles",detalle);
                params.put("propietario",correo);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            String location = latitude + ", " + longitude;

            if (requestCode == REQUEST_CODE_ORIGIN) {
                origenEditText.setText(location);
            } else if (requestCode == REQUEST_CODE_DESTINATION) {
                destinoEditText.setText(location);
            }
        }
    }

}