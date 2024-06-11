package com.example.qlsv.ui.TKB;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qlsv.R;
import com.example.qlsv.databinding.FragmentTkbBinding;
import com.google.android.material.tabs.TabLayout;

public class TKBFragment extends Fragment {

    private FragmentTkbBinding binding;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private TextView name;
    TKBAdapter tkbAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TKBViewModel tkbViewModel =
                new ViewModelProvider(this).get(TKBViewModel.class);

        binding = FragmentTkbBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tabLayout = (TabLayout) root.findViewById(R.id.tabLayout2);
        viewPager2 = (ViewPager2) root.findViewById(R.id.viewPager);
        tkbAdapter = new TKBAdapter(this);
        viewPager2.setAdapter(tkbAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

//        final TextView textView = binding.textTkb;
//        tkbViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}