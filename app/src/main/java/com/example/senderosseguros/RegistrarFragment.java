package com.example.senderosseguros;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.senderosseguros.conexion.AccesoDatos;
import com.example.senderosseguros.entidad.Usuario;

public class RegistrarFragment extends Fragment {

    private Button btnRegistrar;
    private EditText et_userRegis, et_passRegis, et_Nombre, et_Apellido, et_DNI, et_Correo;

    public RegistrarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registrar, container, false);
        AccesoDatos accesoDatos = new AccesoDatos(requireContext());

        btnRegistrar = view.findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
        return view;

        /*// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrar, container, false);*/
    }
     private void registrarUsuario() {

        et_userRegis = getView().findViewById(R.id.et_userRegis);
        et_passRegis = getView().findViewById(R.id.et_passRegis);
        et_Nombre = getView().findViewById(R.id.et_Nombre);
        et_Apellido = getView().findViewById(R.id.et_Apellido);
        et_DNI = getView().findViewById(R.id.et_DNI);
        et_Correo = getView().findViewById(R.id.et_Correo);

        String et_user = et_userRegis.getText().toString().trim();
        String et_pass = et_passRegis.getText().toString().trim();
        String et_nombre = et_Nombre.getText().toString().trim();
        String et_apellido = et_Apellido.getText().toString().trim();
        String et_correo = et_Correo.getText().toString().trim();
        String et_dni = et_DNI.getText().toString().trim();

         // Verificación de campos vacíos
         if (et_user.isEmpty() || et_pass.isEmpty() || et_nombre.isEmpty() || et_correo.isEmpty() || et_apellido.isEmpty() || et_dni.isEmpty()) {
             Toast.makeText(getContext(), "Todos los campos deben estar llenos.", Toast.LENGTH_SHORT).show();
             return;
         }

         // Verificación de longitud máxima de 9 dígitos para dni
         if (et_dni.length() > 9) {
             Toast.makeText(getContext(), "El DNI no puede tener más de 9 dígitos.", Toast.LENGTH_SHORT).show();
             return;
         }

         // Verificación de que no contengan números
         if (!et_nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
             Toast.makeText(getContext(), "El nombre no debe contener números.", Toast.LENGTH_SHORT).show();
             return;
         }

         if (!et_apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
             Toast.makeText(getContext(), "El apellido no debe contener números.", Toast.LENGTH_SHORT).show();
             return;
         }

         Usuario usuario = new Usuario ();
         usuario.setUser(et_user);
         usuario.setPass(et_pass);
         usuario.setNombre(et_nombre);
         usuario.setApellido(et_apellido);
         usuario.setDNI(et_dni);
         usuario.setCorreo(et_correo);
         usuario.setEstado(true);
         usuario.setPuntaje(0);


     }
}