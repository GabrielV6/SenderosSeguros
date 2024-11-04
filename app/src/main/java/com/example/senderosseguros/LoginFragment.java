package com.example.senderosseguros;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.senderosseguros.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

        private FragmentLoginBinding binding;

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {
            binding = FragmentLoginBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Usa binding para acceder al botón de inicio de sesión
            binding.btnLogin.setOnClickListener(v -> btnLogin_Click(view));

            // Usa binding para el TextView de registro
            binding.tvRegis.setOnClickListener(v ->
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_registrarFragment)
            );
        }

        // Libera el binding cuando se destruye la vista para evitar fugas de memoria
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

    public void btnLogin_Click(View view) {
        /*DataDB myDbHelper = new DataDB(this.getView().getContext());
        EditText etUserName = this.getView().findViewById(R.id.txtName);
        String userName = etUserName.getText().toString().trim();
        EditText etPass = this.getView().findViewById(R.id.txtPass);
        String pass = etPass.getText().toString().trim();
        RegisterUser user = myDbHelper.checkUser(userName, pass);

        if (user != null) {
            Toast.makeText(this.getContext(), "Login Correcto", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ParkingControl.class);
            intent.putExtra("user", user);
            etUserName.setText("");
            etPass.setText("");
            startActivity(intent);
        } else {
            Toast.makeText(this.getContext(), "Login Incorrecto", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    } */
}