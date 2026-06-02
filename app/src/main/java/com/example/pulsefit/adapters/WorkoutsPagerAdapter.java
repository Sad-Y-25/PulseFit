package com.example.pulsefit.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.pulsefit.fragments.SessionsFragment;

public class WorkoutsPagerAdapter extends FragmentStateAdapter {

    public WorkoutsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return SessionsFragment.newInstance(true); // Reserved Sessions
        }
        return SessionsFragment.newInstance(false); // Available Sessions
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
