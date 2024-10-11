package com.example.senderosseguros.ui.reporte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.senderosseguros.databinding.FragmentReporteBinding;

public class ReporteFragment extends Fragment {

    private FragmentReporteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReporteViewModel reporteViewModel =
                new ViewModelProvider(this).get(ReporteViewModel.class);

        binding = FragmentReporteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReporte;
        reporteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}