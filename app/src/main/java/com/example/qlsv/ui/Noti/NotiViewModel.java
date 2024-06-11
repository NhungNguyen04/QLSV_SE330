package com.example.qlsv.ui.Noti;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotiViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public NotiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notification fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
