package com.example.senderosseguros.ui.reporte;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.senderosseguros.MainActivity;
import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentReporteBinding;
import com.github.mikephil.charting.charts.HorizontalBarChart; // Cambiar a HorizontalBarChart
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;

public class ReporteFragment extends Fragment {

    private FragmentReporteBinding binding;
    private HorizontalBarChart barChart; // Cambiar a HorizontalBarChart

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReporteViewModel reporteViewModel =
                new ViewModelProvider(this).get(ReporteViewModel.class);

        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar los Spinners
        setupSpinners();

        // Inicializar el HorizontalBarChart y el botón de generación
        barChart = root.findViewById(R.id.barChart); // Asegúrate de que este ID coincida
        Button buttonGenerar = root.findViewById(R.id.buttonGenerar);
        ImageButton buttonVolver = root.findViewById(R.id.buttonVolver);

        buttonGenerar.setOnClickListener(v -> mostrarGrafico());
        buttonVolver.setOnClickListener(v -> mostrarElementos());

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

        // Spinner para "Periodo"
        ArrayAdapter<CharSequence> periodoAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periodo_options, android.R.layout.simple_spinner_item);
        periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTiempo.setAdapter(periodoAdapter);
    }

    private void mostrarGrafico() {
        // Ocultar elementos antes de mostrar el gráfico
        ocultarElementos();

        // Datos harcodeados grafico. El grafico se adapta o sea si pongo como valor maximo 100
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 85));
        entries.add(new BarEntry(1, 20));
        entries.add(new BarEntry(2, 30));
        entries.add(new BarEntry(3, 56));
        entries.add(new BarEntry(4, 20));
        entries.add(new BarEntry(5, 30));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(12f); // Cambia el tamaño del texto si es necesario
        dataSet.setValueTextColor(Color.RED); // Cambia el color del texto
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.LTGRAY);
        colors.add(Color.DKGRAY);
        colors.add(Color.RED);
        colors.add(Color.GRAY);
        colors.add(Color.LTGRAY);

        dataSet.setColors(colors);


        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Actualiza el gráfico
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Hacer visible el grafico
        barChart.setVisibility(View.VISIBLE);
    }

    private void ocultarElementos() {
        // Ocultar Spinners
        binding.spinnerBarrio.setVisibility(View.GONE);
        binding.spinnerObstaculo.setVisibility(View.GONE);
        binding.spinnerTiempo.setVisibility(View.GONE);

        // Ocultar el botón de generar
        binding.buttonGenerar.setVisibility(View.GONE);

        // Ocultar los RadioButtons
        binding.radioButton.setVisibility(View.GONE);
        binding.radioButton2.setVisibility(View.GONE);
        binding.radioButton3.setVisibility(View.GONE);

        // Mostrar el botón volver
        binding.buttonVolver.setVisibility(View.VISIBLE);

        // Hacer visible el gráfico
        barChart.setVisibility(View.VISIBLE);
        binding.textUltimosTresMeses.setVisibility(View.VISIBLE);
    }

    private void mostrarElementos() {
        // Volver a mostrar los elementos ocultos
        binding.spinnerBarrio.setVisibility(View.VISIBLE);
        binding.spinnerObstaculo.setVisibility(View.VISIBLE);
        binding.spinnerTiempo.setVisibility(View.VISIBLE);
        binding.buttonGenerar.setVisibility(View.VISIBLE);
        binding.radioButton.setVisibility(View.VISIBLE);
        binding.radioButton2.setVisibility(View.VISIBLE);
        binding.radioButton3.setVisibility(View.VISIBLE);

        // Ocultar el GRAFICO Y TEXTO
        barChart.setVisibility(View.GONE);
        binding.textUltimosTresMeses.setVisibility(View.GONE);
        // Ocultar el botón volver
        binding.buttonVolver.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
