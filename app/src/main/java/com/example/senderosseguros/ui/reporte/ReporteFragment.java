package com.example.senderosseguros.ui.reporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentReporteBinding;

public class ReporteFragment extends Fragment {

    private FragmentReporteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReporteViewModel reporteViewModel =
                new ViewModelProvider(this).get(ReporteViewModel.class);

        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar los Spinners
        setupSpinners();

        return root;
    }

    private void setupSpinners() {
        // Spinner para "Barrio"
        ArrayAdapter<CharSequence> barrioAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.barrio_options, android.R.layout.simple_spinner_item);
        barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBarrio.setAdapter(barrioAdapter);

        // Spinner para "Tipo de Obstáculo"
        ArrayAdapter<CharSequence> tipoAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipo_options, android.R.layout.simple_spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerObstaculo.setAdapter(tipoAdapter);

        //  Spinner para "Periodo"
        ArrayAdapter<CharSequence> periodoAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periodo_options, android.R.layout.simple_spinner_item);
        periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTiempo.setAdapter(periodoAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
