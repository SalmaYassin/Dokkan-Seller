package com.example.dokkanseller.views.register;


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
import android.widget.Toast;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends BaseFragment {
    private Button register;
    EditText shopname, Email, password, confirmPassword;
    private ProgressDialog mProgress;
    FirebaseAuth mFireBaseAuth;
    private DatabaseReference databaseReference;
    private String currentUserID;

    public Register() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_register;
    }

    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }


    @Override
    public void initializeViews(View view) {
        register = view.findViewById(R.id.registerBtn);
        shopname = view.findViewById(R.id.shopnameId);
        Email = view.findViewById(R.id.emailId);
        password = view.findViewById(R.id.passwordId);
        confirmPassword = view.findViewById(R.id.confPassId);
        mFireBaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(getActivity());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }

        });
    }

    private void createAccount() {
        final String nameOfShop = shopname.getText().toString();
        final String mail = Email.getText().toString();
        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(nameOfShop)) {
            Toast.makeText(getContext(), "Please! write your user name first...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getContext(), "Error!! Email is Empty", Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "Error!! PassWord is Empty", Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(getContext(), "Error!! ConfirmPassWord is Empty", Toast.LENGTH_LONG).show();

        } else if (!pass.equals(confirmPass)) {
            Toast.makeText(getContext(), "Error!! ConfirmPassWord should match PassWord", Toast.LENGTH_LONG).show();

        } else {
            mProgress.setTitle("Create User");
            mProgress.setMessage("please Wait! User creation is in Progress...");
            mProgress.setCanceledOnTouchOutside(true);
            mProgress.show();

            mFireBaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        currentUserID = mFireBaseAuth.getCurrentUser().getUid();
                        databaseReference.child("shops").child(currentUserID).setValue(mail);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("key", currentUserID);
                        map.put("shopName", nameOfShop);
                        databaseReference.child("shops").child(currentUserID).setValue(map);
                        mProgress.dismiss();
                        Toast.makeText(getContext(), "Registered Successful :) Please, Check your E-mail for Verifications", Toast.LENGTH_LONG).show();

                        mFireBaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("currentID" , currentUserID);
                                            bundle.putString("shopName" , nameOfShop);
                                            Log.d("ID_USER", "current user id : "+ currentUserID );
                                            getNavController().navigate(R.id.action_register_to_step1 , bundle);
                                        } else {
                                            mProgress.dismiss();
                                            String message = task.getException().toString();
                                            Toast.makeText(getContext(), "Exception:" + message, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(getContext(), "Exception:" + message, Toast.LENGTH_LONG).show();
                    }
                }


            });
        }

    }


    @Override
    public void setListeners() {

    }


}