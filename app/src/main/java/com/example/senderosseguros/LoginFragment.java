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
import com.example.senderosseguros.conexion.DataBaseHelper;
import com.example.senderosseguros.databinding.FragmentLoginBinding;
import com.google.android.material.navigation.NavigationView;


public class LoginFragment extends Fragment {

    private Button btnLogin;
    private EditText et_user, et_pass;
    private TextView tv_regis;

    private DataBaseHelper dbHelper;
    private static final long LOCK_DURATION_MILLIS = 5 * 60 * 1000; // 5 minutos

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

        dbHelper = new DataBaseHelper(requireContext());

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

        // Verificar si la cuenta está bloqueada
        if (dbHelper.estaBloqueado(user, LOCK_DURATION_MILLIS)) {
            Toast.makeText(getContext(), "Cuenta bloqueada temporalmente. Intenta nuevamente más tarde.", Toast.LENGTH_SHORT).show();
            return;
        }

        AccesoDatos accesoDatos = new AccesoDatos(requireContext());

        boolean existe = accesoDatos.existeUserPass(user, pass);
        //boolean existe = true;
        String correo = accesoDatos.recuperarCorreo(user);

        if (existe) {
            dbHelper.resetearIntentosFallidos(user);
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
            int failedAttempts = dbHelper.obtenerIntentosFallidos(user);

            if (failedAttempts >= 5) {
                dbHelper.establecerTiempoBloqueo(user, System.currentTimeMillis());
                Toast.makeText(getContext(), "Demasiados intentos fallidos. Cuenta bloqueada.", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.incrementarIntentosFallidos(user);
                Toast.makeText(getContext(), "Usuario o contraseña incorrectos. Intentos fallidos: " + (failedAttempts + 1), Toast.LENGTH_SHORT).show();
            }
        }
    }
}