package com.example.qlsv.ui.TKB;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TKBViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TKBViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is TKB fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
