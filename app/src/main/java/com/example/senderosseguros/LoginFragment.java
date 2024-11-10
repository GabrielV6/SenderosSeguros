package com.example.senderosseguros;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.senderosseguros.conexion.AccesoDatos;
import com.example.senderosseguros.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private Button btnLogin;
    private EditText et_user, et_pass;
    private TextView tv_regis;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existeEnBD();
            }
        });

        tv_regis = view.findViewById(R.id.tv_regis);
        tv_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Redirecciona a RegistrarFragment
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_registrarFragment);
            }
        });
        return view;

        /*// Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrar, container, false);*/
    }

    public void existeEnBD() {

        et_user = this.getView().findViewById(R.id.et_user);
        et_pass = this.getView().findViewById(R.id.et_pass);

        String user = et_user.getText().toString().trim();
        String pass = et_pass.getText().toString().trim();

        // Verificación de campos vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos deben estar llenos.", Toast.LENGTH_SHORT).show();
            return;
        }

        AccesoDatos accesoDatos = new AccesoDatos(requireContext());

        boolean existe = accesoDatos.existeUserPass(user, pass);
        String correo = accesoDatos.recuperarCorreo(user);

        if (existe) {
            Toast.makeText(this.getContext(), "Login Correcto", Toast.LENGTH_SHORT).show();
            
            et_user.setText("");
            et_pass.setText("");

            // Crear el Intent para pasar los datos a MainActivity
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("correo", correo);

            // Iniciar MainActivity
            startActivity(intent);

            // Finaliza LoginFragment para que no se quede en el stack
            getActivity().finish();

        } else {
            Toast.makeText(this.getContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            //Falta contador de login malos para bloquear usuario temporalmente.
        }
    }
}