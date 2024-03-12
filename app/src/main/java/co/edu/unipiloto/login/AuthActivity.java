package co.edu.unipiloto.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

        textViewRegister = findViewById(R.id.textView2);

        accederButton.setOnClickListener(this);

        textViewRegister.setOnClickListener(this);
    }


    private boolean validarCampos() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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
                Intent intent = new Intent(AuthActivity.this, SolicitudActivity.class);
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

    private void readUser(String email, String password) {
        String URL="http://192.168.0.13/rodo/buscar.php?email="+email+"&password="+password;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("name").equals("")){
                                Intent intent = new Intent(AuthActivity.this, AuthActivity.class);
                                startActivity(intent);
                            }
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
        requestQueue.add(jsonObjectRequest);
    }
}
