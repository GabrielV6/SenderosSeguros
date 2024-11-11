package com.example.senderosseguros.ui.reporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.gms.maps.model.LatLng;

import com.example.senderosseguros.R;
import com.example.senderosseguros.databinding.FragmentReportarBinding;
import com.example.senderosseguros.ui.slideshow.SlideshowFragment;
import com.example.senderosseguros.conexion.AccesoDatos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

public class ReportarFragment extends Fragment {

    private FragmentReportarBinding binding;

    private LatLng punto1;
    private LatLng punto2;
    private String selectedOption;
    private AccesoDatos accesoDatos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Recuperar la opción seleccionada de los iconos de obstaculos
        if (getArguments() != null) {
            selectedOption = getArguments().getString("selected_option");
            double latitudPunto1 = getArguments().getDouble("latitud");
            double longitudPunto1 = getArguments().getDouble("longitud");

            punto1 = new LatLng(latitudPunto1, longitudPunto1);

            // coordenadas para verificar que seleccione el punto 1
            Toast.makeText(getContext(), "Punto 1: " + punto1.latitude + ", " + punto1.longitude, Toast.LENGTH_SHORT).show();
        }

        if (selectedOption != null) {
            binding.typeSpinner.setText(selectedOption);
        }

        // Inicializamos el objeto de acceso a datos
        accesoDatos = new AccesoDatos(getContext());

        binding.reportButton.setOnClickListener(v -> {
            if (punto1 != null && selectedOption != null) {
                int idBarrio = 4; // Tengo que pasarle el idBarrio por el metodo de Jose
                int idTipoObstaculo = 1;// traerme el tipo de obstaculo!
                int idUsuario = 5; // Este debe ser el ID del usuario autenticado
                String comentarios = "Descripción del obstáculo"; //recuperar el texto del fragment de estef

                // Llamamos al método insertarPunto
                int idPunto = accesoDatos.insertarPunto(punto1.latitude, punto1.longitude, idBarrio);

                String imagen = null;
                String fechaCreacionStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String fechaBaja = null;
                int contadorSolucion = 0;
                int estado = 1;

                if (idPunto != -1) {  // Si la inserción fue exitosa (idPunto es diferente de -1)
                    boolean exito = accesoDatos.insertarObstaculo(
                            idTipoObstaculo,
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
