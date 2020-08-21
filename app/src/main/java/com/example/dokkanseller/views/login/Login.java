package com.example.dokkanseller.views.login;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends BaseFragment {
    private Button btn_sing;
    private EditText Email, Password;
    private TextView newAccount, forgetPassword_btn;
    ProgressDialog loadingbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public static String USERID;



    public Login() {
        // Required empty public constructor
    }
    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void initializeViews(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Email = (EditText) view.findViewById(R.id.email_login);
        Password = (EditText) view.findViewById(R.id.password_login);
        forgetPassword_btn = (TextView) view.findViewById(R.id.forget_password);
        newAccount = (TextView) view.findViewById(R.id.regester_here);
        loadingbar = new ProgressDialog(getActivity());
        btn_sing = view.findViewById(R.id.but_sing);
    }



    @Override
    public void setListeners() {
        btn_sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowUserToLogin();
            }
        });

        // send to register Activity

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavController().navigate(R.id.action_login_to_register);
            }
        });

        // send to forget password activity
        forgetPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNavController().navigate(R.id.action_login_to_forgetPassword);

            }
        });

    }

    // allow user to sing in
    private void allowUserToLogin() {
        final String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "This email is not valid.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "This password is not valid.", Toast.LENGTH_SHORT).show();

        } else {

            loadingbar.setTitle("Sing In");
            loadingbar.setMessage("please Wait,While we are checking the account.");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    loadingbar.dismiss();
                                    getNavController().navigate(R.id.action_login_to_homeFragment2);
                                    Toast.makeText(getActivity(), "Logged is succesfully.", Toast.LENGTH_SHORT).show();
                                    Log.e("a",firebaseAuth.getCurrentUser().getUid());
                                    USERID = firebaseAuth.getCurrentUser().getUid();
                                } else {
                                    loadingbar.dismiss();
                                    Toast.makeText(getActivity(), "please verify your email address", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                loadingbar.dismiss();
                                String error = task.getException().toString();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



}
