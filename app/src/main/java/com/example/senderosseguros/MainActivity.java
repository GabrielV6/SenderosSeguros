package com.example.senderosseguros;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.senderosseguros.entidad.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.senderosseguros.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_reporte, R.id.nav_slideshow, R.id.nav_reportar,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment) {
                // Quitar el botón de regreso en el fragmento de login
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                // Mostrar el botón de regreso en otros fragmentos
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Para quitar el título automático


        // Inicializa el NavigationView (asegúrate de que el ID sea correcto)
        navigationView = findViewById(R.id.nav_view);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        String correo = intent.getStringExtra("correo");
        int ID_User = intent.getIntExtra("ID_User", -1); // Si "ID_User" existe, obtendrás su valor; si no, -1.
//grabo el usuario en la session para usarlo en otro fragment...
        SessionManager.getInstance().setID_User(ID_User);


        // Actualizar los TextViews en el header del Navigation Drawer
        if (user != null && correo != null) {
            // Obtén la cabecera del NavigationView
            View headerView = navigationView.getHeaderView(0); // Obtener la vista del header
            TextView tvUser = headerView.findViewById(R.id.tv_user);
            TextView tvCorreo = headerView.findViewById(R.id.tv_correo);
            TextView tvIDUser = headerView.findViewById(R.id.tv_IDUser);

            tvUser.setText(user);
            tvCorreo.setText(correo);
            tvIDUser.setText(String.valueOf(ID_User));


            navController.navigate(R.id.nav_home);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}