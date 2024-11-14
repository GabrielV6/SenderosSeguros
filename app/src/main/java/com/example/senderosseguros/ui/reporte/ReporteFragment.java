package com.example.senderosseguros.ui.reporte;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.senderosseguros.R;
import com.example.senderosseguros.conexion.AccesoDatos;
import com.example.senderosseguros.databinding.FragmentReporteBinding;
import com.example.senderosseguros.entidad.Barrio;
import com.example.senderosseguros.entidad.ItemReporte;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.os.Handler;
import android.os.Looper;

public class ReporteFragment extends Fragment {
    private TextView textUltimosTresMeses;
    private FragmentReporteBinding binding;
    private PieChart pieChart;
    private AccesoDatos accesoDatos;
    private RadioButton rb_Barrio, rb_Obstaculo, rb_Periodo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        accesoDatos = new AccesoDatos(getContext());
        cargarSpinners();

        pieChart = root.findViewById(R.id.pieChart);
        Button buttonGenerar = root.findViewById(R.id.buttonGenerar);
        ImageButton buttonVolver = root.findViewById(R.id.buttonVolver);

        buttonGenerar.setOnClickListener(v -> mostrarGrafico());
        buttonVolver.setOnClickListener(v -> mostrarElementos());

        //  RadioButtons
        rb_Barrio = root.findViewById(R.id.rbBarrio);
        rb_Obstaculo = root.findViewById(R.id.rbObstaculo);
        rb_Periodo = root.findViewById(R.id.rbTiempo);

        textUltimosTresMeses = root.findViewById(R.id.subtitle_descripcion);
        deshabilitarTodosLosSpinners();

        // Obtener el RadioGroup
        RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Deshabilitar todos los Spinners antes de habilitar el correspondiente
            deshabilitarTodosLosSpinners();
            limpiarTodosLosSpinners();
            // Acción según el RadioButton seleccionado
            if (checkedId ==  R.id.rbBarrio) {
                binding.spinnerBarrio.setEnabled(true);

            } else if (checkedId == R.id.rbObstaculo) {
                binding.spinnerObstaculo.setEnabled(true);

            } else if (checkedId == R.id.rbTiempo) {
                binding.spinnerTiempo.setEnabled(true);
            }

        });
        return root;
    }

    private void deshabilitarTodosLosSpinners() {
        binding.spinnerBarrio.setEnabled(false);
        binding.spinnerObstaculo.setEnabled(false);
        binding.spinnerTiempo.setEnabled(false);
    }
    private void limpiarTodosLosSpinners() {
       binding.spinnerBarrio.setSelection(0);
        binding.spinnerObstaculo.setSelection(0);
        binding.spinnerTiempo.setSelection(0);}

    private void cargarSpinners() {

        String seleccionarBarrio = getString(R.string.sp_barrio);
        String seleccionarTipo = getString(R.string.sp_tipo);
        String seleccionarPeriodo = getString(R.string.sp_periodo);

        // Spinner para "Barrio"
        new Thread(() -> {
            List<Barrio> barrios = accesoDatos.obtenerBarrios();

            List<String> nombresBarrios = new ArrayList<>();
            nombresBarrios.add(seleccionarBarrio);

            for (Barrio barrio : barrios) {
                nombresBarrios.add(barrio.getDescripcion());
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                ArrayAdapter<String> barrioAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, nombresBarrios);
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

        // Spinner para "Periodo" con las tres opciones
        List<String> opcionesPeriodo = Arrays.asList("Seleccione un periodo","Última semana", "Último mes", "Últimos tres meses");
        ArrayAdapter<String> periodoAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, opcionesPeriodo);
        periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTiempo.setAdapter(periodoAdapter);
    }

    private void mostrarGrafico() {
        AccesoDatos accesoDatos = new AccesoDatos(requireContext());

        int checkedId = binding.radioGroup.getCheckedRadioButtonId();
        List<ItemReporte> datos = new ArrayList<>();

        if (checkedId == R.id.rbBarrio) {
            String selectedBarrio = binding.spinnerBarrio.getSelectedItem().toString();
            if (selectedBarrio.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, selecciona un barrio válido.", Toast.LENGTH_SHORT).show();
                return;
            }
            datos = accesoDatos.obtenerObstaculosPorBarrio(selectedBarrio);

        } else if (checkedId == R.id.rbObstaculo) {
            String selectedTipo = binding.spinnerObstaculo.getSelectedItem().toString();
            if (selectedTipo.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, selecciona un tipo de obstaculo válido.", Toast.LENGTH_SHORT).show();
                return;
            }
            datos = accesoDatos.obtenerObstaculosPorTipo(selectedTipo);

        } else if (checkedId == R.id.rbTiempo) {
            String selectedPeriodo = binding.spinnerTiempo.getSelectedItem().toString();
            if (selectedPeriodo == "Seleccione un periodo" ) {
                Toast.makeText(getContext(), "Por favor, selecciona un rango válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            Date fechaInicio = new Date();

            switch (selectedPeriodo) {
                case "Última semana":
                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    fechaInicio = calendar.getTime();
                    break;
                case "Último mes":
                    calendar.add(Calendar.MONTH, -1);
                    fechaInicio = calendar.getTime();
                    break;
                case "Últimos tres meses":
                    calendar.add(Calendar.MONTH, -3);
                    fechaInicio = calendar.getTime();
                    break;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fechaInicioStr = dateFormat.format(fechaInicio);
            datos = accesoDatos.obtenerObstaculosPorPeriodo(fechaInicioStr);

        } else {
            Toast.makeText(getContext(), "Por favor, selecciona una opción válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Texto del reporte basado en el RadioButton seleccionado
        String texto = llenarTipoReporte(checkedId);
        binding.textUltimosTresMeses.setText(texto);
        ocultarElementos();

        List<PieEntry> entries = new ArrayList<>();
        for (ItemReporte item : datos) {
            entries.add(new PieEntry(item.getCantidad(), item.getDescripcion()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS );
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.getDescription().setEnabled(false); // Disables the description label entirely
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
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
        pieChart.setVisibility(View.VISIBLE);
        binding.buttonVolver.setVisibility(View.VISIBLE);
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
        pieChart.setVisibility(View.GONE);
        binding.textUltimosTresMeses.setVisibility(View.GONE);
        binding.buttonVolver.setVisibility(View.GONE);

        limpiarRbSpinner();
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

    private void limpiarRbSpinner() {
        binding.rbBarrio.setChecked(false);
        binding.rbObstaculo.setChecked(false);
        binding.rbTiempo.setChecked(false);
        binding.spinnerBarrio.setSelection(0);
        binding.spinnerObstaculo.setSelection(0);
        binding.spinnerTiempo.setSelection(0);
    }

    private String llenarTipoReporte(int rbSeleccionado) {
        String texto = "";

        if (rbSeleccionado == R.id.rbBarrio) {
            texto = binding.spinnerBarrio.getSelectedItem().toString();
        } else if (rbSeleccionado == R.id.rbObstaculo) {
            texto = binding.spinnerObstaculo.getSelectedItem().toString();
        } else if (rbSeleccionado == R.id.rbTiempo) {
            texto = binding.spinnerTiempo.getSelectedItem().toString();
        }

        return texto;
    }

}
