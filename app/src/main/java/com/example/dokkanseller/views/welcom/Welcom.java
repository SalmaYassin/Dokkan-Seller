package com.example.dokkanseller.views.welcom;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;


public class Welcom extends BaseFragment {
    public Button btn_singIn;
    public Button btn_singUp;



    public Welcom() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_welcom_page;
    }




    @Override
    public void initializeViews(View view) {
        btn_singIn = view.findViewById(R.id.btn_signIn);
        btn_singUp = view.findViewById(R.id.btn_signUp);
        btn_singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavController().navigate(R.id.action_welcomPage_to_login);
            }
        });

        btn_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavController().navigate(R.id.action_welcomPage_to_register);
            }
        });
    }



    @Override
    public void setListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcom_page, container, false);
    }

}
