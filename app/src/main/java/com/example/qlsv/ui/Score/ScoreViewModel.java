package com.example.qlsv.ui.Score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScoreViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ScoreViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is score fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
