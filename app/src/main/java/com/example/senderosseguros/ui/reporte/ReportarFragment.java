package com.example.senderosseguros.ui.reporte;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.senderosseguros.entidad.Barrio;
import com.example.senderosseguros.entidad.Punto;
import com.example.senderosseguros.entidad.TipoObstaculo;
import com.google.android.gms.maps.model.LatLng;

import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentReportarBinding;
import com.example.senderosseguros.ui.slideshow.SlideshowFragment;
import com.example.senderosseguros.conexion.AccesoDatos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public class ReportarFragment extends Fragment {

    private FragmentReportarBinding binding;

    private LatLng punto1;
    private LatLng punto2;
    private String selectedOption;
    private AccesoDatos accesoDatos;
    private Barrio barrioSeleccionado = null;  // Variable para almacenar el barrio seleccionado

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        accesoDatos = new AccesoDatos(getContext());

        // Recuperar la opción seleccionada de los iconos de obstaculos
        if (getArguments() != null) {
            selectedOption = getArguments().getString("selected_option");
            double latitudPunto1 = getArguments().getDouble("latitud");
            double longitudPunto1 = getArguments().getDouble("longitud");

            punto1 = new LatLng(latitudPunto1, longitudPunto1);

            // coordenadas para verificar que seleccione el punto 1
            Toast.makeText(getContext(), "Punto 1: " + punto1.latitude + ", " + punto1.longitude, Toast.LENGTH_SHORT).show();
        }

        int selectedId = Integer.parseInt(selectedOption);
        TipoObstaculo tipoObstaculo = accesoDatos.obtenerTipoObstaculoPorId(selectedId);

        if (tipoObstaculo != null) {
            binding.typeSpinner.setText(tipoObstaculo.getDescripcion());
        } else {
            binding.typeSpinner.setText("Tipo de obstáculo no encontrado");
        }

        String seleccionarBarrio = getString(R.string.sp_barrio);

// Spinner para "Barrio"
        new Thread(() -> {
            List<Barrio> barrios = accesoDatos.obtenerBarrios();
            barrios.add(0, new Barrio(-1, seleccionarBarrio));
            new Handler(Looper.getMainLooper()).post(() -> {
                ArrayAdapter<Barrio> barrioAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, barrios);

                barrioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerBarrio.setAdapter(barrioAdapter);

                // Agregar un listener para capturar la selección del barrio
                binding.spinnerBarrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        if (position != 0) {
                            barrioAdapter.notifyDataSetChanged();
                            Barrio selectedBarrio = (Barrio) parentView.getItemAtPosition(position);
                            ReportarFragment.this.barrioSeleccionado = selectedBarrio;
                            int barrioId = selectedBarrio.getIdBarrio();
                            Log.d("Barrio Seleccionado", "ID: " + barrioId + ", Descripción: " + selectedBarrio.getDescripcion());
                        }
                        barrioAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Manejo de cuando no se selecciona nada
                        barrioAdapter.notifyDataSetChanged();
                    }
                });
                barrioAdapter.notifyDataSetChanged();  // Esto asegura que el Spinner actualice su vista
            });
        }).start();



        binding.reportButton.setOnClickListener(v -> {
            if (punto1 != null && selectedOption != null && barrioSeleccionado != null) {

                Punto punto = new Punto();
                punto.setBarrio(barrioSeleccionado);
                punto.setLatitud(punto1.latitude);
                punto.setLongitud(punto1.longitude);

                int idPunto = accesoDatos.insertarPunto(punto);

                int idUsuario = 5; // Este debe ser el ID del usuario autenticado
                String comentarios = "Descripción del obstáculo"; //recuperar el texto del fragment de estef
                String imagen = null;
                String fechaCreacionStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String fechaBaja = null;
                int contadorSolucion = 0;
                int estado = 1;

                if (idPunto != -1) {  // Si la inserción fue exitosa (idPunto es diferente de -1)
                    boolean exito = accesoDatos.insertarObstaculo(
                            tipoObstaculo.getIdTipo(),
                            comentarios,
                            imagen,
                            idUsuario,
                            idPunto,
                            fechaBaja,
                            contadorSolucion,
                            estado
                    );

                    if (exito) {
                        Toast.makeText(getContext(), "Obstáculo reportado correctamente.", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.nav_slideshow);
                    } else {
                        Toast.makeText(getContext(), "Hubo un error al reportar el obstáculo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hubo un error al insertar el punto de coordenada.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Por favor, asegúrate de seleccionar el punto y el obstáculo.", Toast.LENGTH_SHORT).show();
            }
        });

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
