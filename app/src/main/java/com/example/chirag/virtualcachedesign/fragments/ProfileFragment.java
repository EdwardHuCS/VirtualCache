package com.example.chirag.virtualcachedesign.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chirag.virtualcachedesign.R;
import com.example.chirag.virtualcachedesign.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    @Bind(R.id.about)
    TextView about;

    @Bind(R.id.aboutDescription)
    TextView aboutDesc;

    @Bind(R.id.signOut)
    Button signOut;

    @Bind(R.id.email)
    TextView email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String emailText = ((MainActivity)getActivity()).email;
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Avenir-Heavy.ttf");
        about.setTypeface(font);
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Avenir-Light.ttf");
        aboutDesc.setTypeface(font1);
        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Avenir-Heavy.ttf");
        signOut.setTypeface(font2);
        Typeface font3 = Typeface.createFromAsset(getActivity().getAssets(), "Avenir-Heavy.ttf");
        email.setTypeface(font3);
        email.setText(emailText);
    }
}
