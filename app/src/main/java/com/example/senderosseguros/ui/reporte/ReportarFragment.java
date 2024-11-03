package com.example.senderosseguros.ui.reporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentReportarBinding;

public class ReportarFragment extends Fragment {

    private FragmentReportarBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Recuperar la opción seleccionada
        String selectedOption = getArguments() != null ? getArguments().getString("selected_option") : null;
        if (selectedOption != null) {
            // Establecer el texto en el TextView con ID type_spinner
            binding.typeSpinner.setText(selectedOption);
        }

        // Configurar el botón volver
        binding.volverButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_slideshow);
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
