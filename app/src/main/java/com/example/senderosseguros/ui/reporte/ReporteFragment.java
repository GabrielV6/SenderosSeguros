package com.example.senderosseguros.ui.reporte;

import static com.example.senderosseguros.R.id.radioGroup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.senderosseguros.R;
import com.example.senderosseguros.conexion.AccesoDatos;
import com.example.senderosseguros.databinding.FragmentReporteBinding;
import com.github.mikephil.charting.charts.HorizontalBarChart; // Cambiar a HorizontalBarChart
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Looper;

public class ReporteFragment extends Fragment {
    private TextView textUltimosTresMeses;
    private FragmentReporteBinding binding;
    private HorizontalBarChart barChart;
    private AccesoDatos accesoDatos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accesoDatos = new AccesoDatos(getContext());
       // setupSpinners();

        barChart = root.findViewById(R.id.barChart);
        Button buttonGenerar = root.findViewById(R.id.buttonGenerar);
        ImageButton buttonVolver = root.findViewById(R.id.buttonVolver);

        buttonGenerar.setOnClickListener(v -> mostrarGrafico());
        buttonVolver.setOnClickListener(v -> mostrarElementos());

        textUltimosTresMeses = root.findViewById(R.id.textUltimosTresMeses);

        // Obtener el RadioGroup
        RadioGroup radioGroup = root.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Acción según el RadioButton seleccionado con if
                if (checkedId == R.id.rbBarrio) {
                    // Mostrar spinner correspondiente
                    habilitarSpinnerBarrio();
                } else if (checkedId == R.id.rbObstaculo) {
                    // Mostrar spinner correspondiente
                    habilitarSpinnerObstaculo();
                } else if (checkedId == R.id.rbTiempo) {
                    // Mostrar spinner correspondiente
                    habilitarSpinnerTiempo();
                }
            }
        });
        return root;
    }

    private void habilitarSpinnerBarrio() {
        // Inhabilitar los spinners que no se están utilizando
        binding.spinnerObstaculo.setEnabled(false);
        binding.spinnerTiempo.setEnabled(false);

        // Hacer el spinner de Barrio visible y habilitarlo
        binding.spinnerBarrio.setVisibility(View.VISIBLE);
        binding.spinnerBarrio.setEnabled(true);
    }

    private void habilitarSpinnerObstaculo() {
        // Inhabilitar los spinners que no se están utilizando
        binding.spinnerBarrio.setEnabled(false);
        binding.spinnerTiempo.setEnabled(false);

        // Hacer el spinner de Obstáculo visible y habilitarlo
        binding.spinnerObstaculo.setVisibility(View.VISIBLE);
        binding.spinnerObstaculo.setEnabled(true);
    }

    private void habilitarSpinnerTiempo() {
        // Inhabilitar los spinners que no se están utilizando
        binding.spinnerBarrio.setEnabled(false);
        binding.spinnerObstaculo.setEnabled(false);

        // Hacer el spinner de Tiempo visible y habilitarlo
        binding.spinnerTiempo.setVisibility(View.VISIBLE);
        binding.spinnerTiempo.setEnabled(true);
    }


    private void setupSpinners() {
        // Spinner para "Barrio"
        String seleccionarBarrio = getString(R.string.sp_barrio);
        String seleccionarTipo = getString(R.string.sp_tipo);

        // Spinner para "Barrio"
        new Thread(() -> {
            List<String> barrios = accesoDatos.obtenerBarrios();
            barrios.add(0, seleccionarBarrio);

            new Handler(Looper.getMainLooper()).post(() -> {
                ArrayAdapter<String> barrioAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, barrios);
                barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerBarrio.setAdapter(barrioAdapter);
            });
        }).start();

        // Spinner para "Tipo de Obstáculo"
        new Thread(() -> {
            List<String> obstaculos = accesoDatos.obtenerObstaculosActivos();
            obstaculos.add(0, seleccionarTipo);

            new Handler(Looper.getMainLooper()).post(() -> {
                ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, obstaculos);
                tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerObstaculo.setAdapter(tipoAdapter);
            });
        }).start();

        // Spinner para "Periodo"
        ArrayAdapter<CharSequence> periodoAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periodo_options, android.R.layout.simple_spinner_item);
        periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTiempo.setAdapter(periodoAdapter);
    }

    private void mostrarGrafico() {

        AccesoDatos accesoDatos = new AccesoDatos(requireContext());
        boolean existe = accesoDatos.obtenerTextoDesdeBD();

        if(existe){
            Toast.makeText(this.getContext(), "CONECTA ok>", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.getContext(), "NO CONECTA =(", Toast.LENGTH_SHORT).show();
        }

        /*new Thread(() -> {
            // Realiza la llamada a la base de datos en un hilo en segundo plano
            int id = 3;
            boolean existe = accesoDatos.obtenerTextoDesdeBD(id);
            String texto = "";
            if(existe){
                texto = " FUNCIONA";
            }

            // Actualiza la interfaz de usuario en el hilo principal
            /*getActivity().runOnUiThread(() -> {
                textUltimosTresMeses.setText(texto);
            });
        }).start();*/

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
        binding.rbBarrio.setVisibility(View.GONE);
        binding.rbObstaculo.setVisibility(View.GONE);
        binding.rbTiempo.setVisibility(View.GONE);

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
        binding.rbBarrio.setVisibility(View.VISIBLE);
        binding.rbObstaculo.setVisibility(View.VISIBLE);
        binding.rbTiempo.setVisibility(View.VISIBLE);

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
    // Metodo para habilitar y deshabilitar Spinners y RadioButtons
    private void updateSpinnersAndRadios() {
        boolean barrioSelected = binding.rbBarrio.isChecked();
        boolean obstaculoSelected = binding.rbObstaculo.isChecked();
        boolean tiempoSelected = binding.rbTiempo.isChecked();

        binding.spinnerBarrio.setEnabled(barrioSelected);
        binding.spinnerObstaculo.setEnabled(obstaculoSelected);
        binding.spinnerTiempo.setEnabled(tiempoSelected);

        binding.rbBarrio.setEnabled(!obstaculoSelected && !tiempoSelected);
        binding.rbObstaculo.setEnabled(!barrioSelected && !tiempoSelected);
        binding.rbTiempo.setEnabled(!barrioSelected && !obstaculoSelected);
    }
}
