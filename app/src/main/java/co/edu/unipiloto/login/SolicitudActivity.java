package co.edu.unipiloto.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class SolicitudActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nameEditText;
    EditText origenEditText;
    EditText destinoEditText;
    EditText pesoEditText;
    EditText detallesEditText;
    String correo;

    Button accederButton;
    RequestQueue requestQueue;

    private static final String URL1="http://192.168.0.13/rodo/cargas.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);
        
        requestQueue = Volley.newRequestQueue(this);

        InitUI();
        accederButton.setOnClickListener(this);
    }

    private void InitUI() {
        nameEditText= findViewById(R.id.nameEditText);
        origenEditText = findViewById(R.id.originEditText);
        destinoEditText = findViewById(R.id.destinationEditText);
        pesoEditText = findViewById(R.id.weightEditText);
        detallesEditText = findViewById(R.id.descriptionEditText);
        correo= getIntent().getStringExtra("usuario");

        accederButton = findViewById(R.id.submitButton);
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
}