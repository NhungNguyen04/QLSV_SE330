package com.example.qlsv.ui.Test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestScheduleViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public TestScheduleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is test schedule fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
