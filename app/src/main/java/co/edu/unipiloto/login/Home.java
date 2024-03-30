package co.edu.unipiloto.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.nav_Home:
                startActivity(new Intent(this, Home.class));
                return true;
            case R.id.nav_Favoritos:
                startActivity(new Intent(this,RegisterActivity.class));
                return true;
            case R.id.nav_formulario:
                startActivity(new Intent(this,SolicitudActivity.class));
                return true;
            case R.id.nav_perfil:
                startActivity(new Intent(this,SolicitudActivity.class));
                return true;
            default:
                return false;
        }
    }
}