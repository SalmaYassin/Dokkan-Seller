package com.example.dokkanseller.views.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class forgetPassword extends BaseFragment {
    private EditText registeredEmail;
    private Button resetPassword_btn;
    private FirebaseAuth firebaseAuth;


    public forgetPassword() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_forget_password;
    }

    @Override
    public void initializeViews(View view) {
        registeredEmail = view.findViewById(R.id.rest_email);
        resetPassword_btn = view.findViewById(R.id.reset_btn);
        firebaseAuth=FirebaseAuth.getInstance();
        resetPassword_btn.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetEmail();
            }
        });

    }
    private void ResetEmail(){
        String email = registeredEmail.getText().toString();
        if(TextUtils.isEmpty(email)){

            Toast.makeText(getActivity(), "This email is not valid.", Toast.LENGTH_SHORT).show();

        }else {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), " email sent successfully, cheak your email...", Toast.LENGTH_SHORT).show();

                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    @Override
    public void setListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

}
