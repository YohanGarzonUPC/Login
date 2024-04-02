package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    static int contador=0;
    static int aux=0;
    static List<String> listaPublicaciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        nestedScrollView = findViewById(R.id.nsvVista);
        recyclerView = findViewById(R.id.rvUsuarios);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        progressBar = findViewById(R.id.pbCargando);

        ObtenerPublicacion(recyclerView,Home.this);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    contador++;
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (contador<10){
                                ObtenerPublicacion(recyclerView, Home.this);
                            }
                        }
                    },3000);
                }
            }
        });
    }

    public static void ObtenerPublicacion(RecyclerView recyclerView, Context context) {
        for (int i=0;i<10;i++){
            aux++;
            listaPublicaciones.add("Usuario con ID"+aux);
        }
        recyclerView.setAdapter(new AdapterPublicaciones(context, listaPublicaciones));
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
}