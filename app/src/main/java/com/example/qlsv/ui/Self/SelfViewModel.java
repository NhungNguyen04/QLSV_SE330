package com.example.qlsv.ui.Self;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelfViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public SelfViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is self fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
