package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class EdicionPerfil extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    ImageView imageViewPerfil;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_perfil);

        handleIntent();
        setupImageView();

        Button actualizarButton = findViewById(R.id.button);
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager userManager = UserManager.getInstance();
                if (userManager != null) {
                    String userEmail = userManager.getEmail();
                    EditText nombreEditText = findViewById(R.id.editTextNombre);
                    EditText phoneEditText = findViewById(R.id.editTextTelefono);
                    EditText direccionEditText = findViewById(R.id.editTextDireccion);

                    String name = nombreEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    String direccion = direccionEditText.getText().toString();

                    if (selectedImageUri != null) {
                        uploadImage(selectedImageUri, userEmail, name, phone, direccion);
                    } else {
                        enviarDatosUsuario(userEmail, name, phone, direccion);
                    }
                } else {
                    Toast.makeText(EdicionPerfil.this, "No se pudo obtener la información del usuario.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            UserManager userManager = UserManager.getInstance();
            if (userManager != null) {
                String nombre = userManager.getName();
                String telefono = userManager.getPhone();
                String email = userManager.getEmail();
                String direccion = userManager.getDireccion();

                EditText editTextNombre = findViewById(R.id.editTextNombre);
                editTextNombre.setText(nombre);

                EditText editTextDireccion = findViewById(R.id.editTextDireccion);
                editTextDireccion.setText(direccion);

                EditText editTextTelefono = findViewById(R.id.editTextTelefono);
                editTextTelefono.setText(telefono);

                TextView editTextEmail = findViewById(R.id.textView14);
                editTextEmail.setText(email);

                ImageView imageView = findViewById(R.id.imageViewPerfil);

                String imagenUrl = userManager.getImagen_url();

                new LoadImageFromUrlTask(imageView).execute(imagenUrl);
            }
        }
    }

    private void setupImageView() {
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        imageViewPerfil.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(EdicionPerfil.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EdicionPerfil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            } else {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewPerfil.setImageURI(selectedImageUri);
        }
    }

    private void uploadImage(Uri imageUri, String userEmail, String name, String phone, String direccion) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(imageUri);
            byte[] inputData = getBytes(inputStream);
            String encodedImage = Base64.encodeToString(inputData, Base64.DEFAULT);

            enviarImagen(encodedImage, userEmail, name, phone, direccion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void enviarImagen(String encodedImage, String userEmail, String name, String direccion, String phone) {
        String url = "http://192.168.0.15/rodo/update.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("ServerResponse", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.optString("status");
                        if ("success".equals(status)) {
                            Toast.makeText(EdicionPerfil.this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = jsonResponse.optString("message", "Error desconocido");
                            Toast.makeText(EdicionPerfil.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EdicionPerfil.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("ServerError", error.toString());
                    Toast.makeText(EdicionPerfil.this, "Error en la conexión con el servidor.", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                params.put("email", userEmail);
                params.put("name", name);
                params.put("direccion", direccion);
                params.put("phone", phone);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void enviarDatosUsuario(String userEmail, String name, String phone, String direccion) {
        String url = "http://192.168.0.15/rodo/updatePerfil.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("ServerResponse", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.optString("status");
                        if ("success".equals(status)) {
                            Toast.makeText(EdicionPerfil.this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = jsonResponse.optString("message", "Error desconocido");
                            Toast.makeText(EdicionPerfil.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EdicionPerfil.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("ServerError", error.toString());
                    Toast.makeText(EdicionPerfil.this, "Error en la conexión con el servidor.", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("name", name);
                params.put("direccion", direccion);
                params.put("phone", phone);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}
