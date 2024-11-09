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
import com.example.senderosseguros.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private Button btnLogin;
    private EditText et_user, et_pass;

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

        boolean existe = accesoDatos.existeUser(user);

        if (existe) {
            Toast.makeText(this.getContext(), "Login Correcto", Toast.LENGTH_SHORT).show();

            //Redirecciona a LoginFragment
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_loginFragment_to_nav_home);

            et_user.setText("");
            et_pass.setText("");

        } else {
            Toast.makeText(this.getContext(), "Login Incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
}