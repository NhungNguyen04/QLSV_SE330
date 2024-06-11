package com.example.qlsv.ui.TKB;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TKBAdapter extends FragmentStateAdapter {
    public TKBAdapter(@NonNull TKBFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new fragment1();
            case 1:
                return new fragment2();
        }
        return new fragment1();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
