package co.edu.unipiloto.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    EditText emailEditText;
    EditText passwordEditText;
    TextView textViewRegister;

    Button accederButton;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


        requestQueue = Volley.newRequestQueue(this);

        accederButton = findViewById(R.id.button2);

        emailEditText = findViewById(R.id.emailEditText);

        passwordEditText = findViewById(R.id.passwordEditText);

        textViewRegister = findViewById(R.id.textView2);

        accederButton.setOnClickListener(this);

        textViewRegister.setOnClickListener(this);
    }


    private boolean validarCampos() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("correo",email);
        if (email.isEmpty()) {
            emailEditText.setError("Ingrese su correo electrónico");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Ingrese su contraseña");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        

        if (id == R.id.button2){

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (validarCampos()) {
                readUser(email,password);
            }
        } else if (id==R.id.textView2) {
            try {
                Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readUser(final String email, final String password) {
        String URL = "http://192.168.56.1/rodo/buscar.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.optString("status");
                            if ("success".equals(status)) {
                                JSONObject userData = jsonResponse.optJSONObject("data");
                                if (userData != null) {
                                    String name = userData.optString("name", "N/A");
                                    String phone = userData.optString("phone", "N/A");
                                    String direccion = userData.optString("direccion", "N/A");
                                    String emailRes = userData.optString("email", "N/A");
                                    String type = userData.optString("type", "N/A");
                                    String imagen_url = userData.optString("imagen_url", "N/A");

                                    UserManager userManager = UserManager.getInstance();
                                    userManager.setName(name);
                                    userManager.setPhone(phone);
                                    userManager.setDireccion(direccion);
                                    userManager.setEmail(emailRes);
                                    userManager.setType(type);
                                    userManager.setImagen_url(imagen_url);

                                    if (userData.has("publicaciones")) {
                                        JSONArray publicacionesArray = userData.getJSONArray("publicaciones");
                                        List<Publicacion> publicationList = new ArrayList<>();
                                        for (int i = 0; i < publicacionesArray.length(); i++) {
                                            JSONObject publicationJson = publicacionesArray.getJSONObject(i);
                                            Publicacion publicacion = new Publicacion(
                                                    publicationJson.optString("name", "N/A"),
                                                    publicationJson.optString("origen", "N/A"),
                                                    publicationJson.optString("destino", "N/A"),
                                                    publicationJson.optString("peso", "N/A"),
                                                    publicationJson.optString("detalles", "N/A"),
                                                    publicationJson.optString("fecha", "N/A"),
                                                    publicationJson.optInt("idPublicacion", -1)
                                            );
                                            publicationList.add(publicacion);
                                        }
                                        userManager.setPublicaciones(publicationList);
                                    }
                                    Intent intent;
                                    type = userManager.getType();
                                    if (type.equalsIgnoreCase("Conductor")){
                                        intent = new Intent(AuthActivity.this, CargasAsignadas.class);
                                    } else if (type.equalsIgnoreCase("Propietario de Camión")) {
                                         intent = new Intent(AuthActivity.this, Home.class);
                                    }else{
                                        intent = new Intent(AuthActivity.this, Home.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(AuthActivity.this, "Error en los datos de usuario", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String errorMessage = jsonResponse.optString("message", "Error desconocido");
                                Toast.makeText(AuthActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AuthActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            switch (statusCode) {
                                case 401:
                                    Toast.makeText(AuthActivity.this, "Autenticación fallida. Verifique su email y contraseña.", Toast.LENGTH_LONG).show();
                                    break;
                                case 404:
                                    Toast.makeText(AuthActivity.this, "Usuario no encontrado.", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(AuthActivity.this, "Error en la conexión con el servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(AuthActivity.this, "Error en la conexión con el servidor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }


}
