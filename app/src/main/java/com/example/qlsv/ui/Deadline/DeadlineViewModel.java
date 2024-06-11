package com.example.qlsv.ui.Deadline;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DeadlineViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public DeadlineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is deadline fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
