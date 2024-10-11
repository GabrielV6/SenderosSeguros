package com.example.senderosseguros.ui.reporte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReporteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReporteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("INFORME DE OBSTACULOS");
    }

    public LiveData<String> getText() {
        return mText;
    }
}