package co.edu.unipiloto.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class    RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button accederButton;
    RequestQueue requestQueue;
    private static final String URL1="http://192.168.0.13/rodo/usuarios.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Volley.newRequestQueue(this);

        InitUI();
        accederButton.setOnClickListener(this);
    }

    private void InitUI() {
        nameEditText= findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        accederButton = findViewById(R.id.registrar);
    }

    private boolean validarCampos() {


        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Ingrese su nombre");
            return false;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Ingrese su telefono");
            return false;
        }

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
    public void onClick(View v){
        int id = v.getId();

        if(id==R.id.registrar) {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            createUser(name,phone,email,password);

            if (validarCampos()) {
                Intent intent=new Intent(RegisterActivity.this,AuthActivity.class);
                startActivity(intent);
            }
        }
    }

    private void createUser(final String name, final String phone,final String email,final String password) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Toast.makeText(RegisterActivity.this,"Correcto", Toast.LENGTH_SHORT).show();
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
                params.put("phone",phone);
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
