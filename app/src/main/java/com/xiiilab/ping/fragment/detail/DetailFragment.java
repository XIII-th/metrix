package com.xiiilab.ping.fragment.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiiilab.ping.databinding.HostDetailFragmentBinding;
import com.xiiilab.ping.viewmodel.ItemViewModel;

public class DetailFragment extends Fragment {

    private ItemViewModel mItemViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null)
            throw new IllegalStateException("Unable to get activity");
        mItemViewModel = ViewModelProviders.of(getActivity()).get(ItemViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        HostDetailFragmentBinding binding = HostDetailFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setItemVm(mItemViewModel);
        return binding.getRoot();
    }
}