package com.example.senderosseguros;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
        //AccesoDatos accesoDatos = new AccesoDatos(requireContext());

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
        // Validación para condiciones de la pass
         if (!validarContrasena(et_pass)) {
             Toast.makeText(getContext(), "La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, una minúscula y un número.", Toast.LENGTH_SHORT).show();
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

         //Verificación para que el correo tenga el formato correcto
         if (!et_correo.matches("^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$")) {
             // El formato de correo electrónico es inválido
             Toast.makeText(getContext(), "Por favor, ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
             return;
         }

         Usuario usuario = new Usuario ();
         usuario.setNombre(et_nombre);
         usuario.setApellido(et_apellido);
         usuario.setDNI(et_dni);
         usuario.setCorreo(et_correo);
         usuario.setUser(et_user);
         usuario.setPass(et_pass);

         AccesoDatos accesoDatos = new AccesoDatos(requireContext());

         boolean existeDNI = accesoDatos.existeDNI(et_dni);
         boolean existeUser = accesoDatos.existeUser(et_user);

         if (existeDNI) {
             Toast.makeText(getContext(), "El DNI ya está registrado.", Toast.LENGTH_SHORT).show();
         } else if (existeUser) {
             Toast.makeText(getContext(), "El nombre de usuario ya está registrado.", Toast.LENGTH_SHORT).show();
         } else if (accesoDatos.agregarUser(usuario)) {
             Toast.makeText(getContext(), "Usuario agregado.", Toast.LENGTH_SHORT).show();

             et_userRegis.setText("");
             et_passRegis.setText("");
             et_Nombre.setText("");
             et_Apellido.setText("");
             et_Correo.setText("");
             et_DNI.setText("");

             // Redirecciona a LoginFragment
             NavHostFragment.findNavController(RegistrarFragment.this)
                     .navigate(R.id.action_registrarFragment_to_loginFragment);
         } else {
             Toast.makeText(getContext(), "Error al agregar usuario.", Toast.LENGTH_SHORT).show();
         }

     }

    public boolean validarContrasena(String et_pass) {
        // Verifica longitud mínima de 8 caracteres
        if (et_pass.length() < 8) {
            return false;
        }

        // Expresión regular para verificar que contenga al menos una mayúscula, una minúscula y un número
        String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        return et_pass.matches(patron);
    }

}