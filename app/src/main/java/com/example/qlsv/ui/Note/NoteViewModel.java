package com.example.qlsv.ui.Note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public NoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is note fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

