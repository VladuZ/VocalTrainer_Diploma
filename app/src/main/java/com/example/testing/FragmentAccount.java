package com.example.testing;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentAccount extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }
}
