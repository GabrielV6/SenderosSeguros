package com.example.senderosseguros;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.senderosseguros.databinding.FragmentLogoutBinding;
import com.example.senderosseguros.databinding.FragmentReportarBinding;

public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        logout();

        return view;
    }

    private void logout() {
        // Redirigir al LoginFragment y limpiar la pila de navegación
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.loginFragment);

        // Limpia el back stack para evitar regresar al fragmento anterior
        navController.popBackStack(R.id.loginFragment, false);
    }
}
