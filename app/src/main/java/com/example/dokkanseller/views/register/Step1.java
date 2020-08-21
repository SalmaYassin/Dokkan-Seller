package com.example.dokkanseller.views.register;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step1 extends BaseFragment {
    private EditText location, phone  , facebookLink ,instgramLink ;
    private Button nextBtn;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference reff ;
    private Bundle bundle_userID  ;





    public Step1() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_step1;
    }


    @Override
    public void initializeViews(View view) {
        location =  view.findViewById(R.id.locationId);
        phone = view.findViewById(R.id.phoneId);
        instgramLink = view.findViewById(R.id.instgramId);
        facebookLink = view.findViewById(R.id.facebookId);

        nextBtn= view.findViewById(R.id.nxtbtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle_userID = getArguments();
                String id = bundle_userID.getString("currentID");
                String name = bundle_userID.getString("shopName");


                String loc = location.getText().toString();
                String number = phone.getText().toString();
                String face = facebookLink.getText().toString();
                String insta = instgramLink.getText().toString();

                Step1Model step1model = new Step1Model(loc ,number,face,insta);
                Bundle bundle = new Bundle();
                bundle.putString("user_id2" , id);
                bundle.putString("name2" , name);
                bundle.putParcelable("step1_model" ,step1model);
                getNavController().navigate(R.id.action_step1_to_step2 ,bundle );

            }
        });

      //  bundle = getArguments();
      //  String id = bundle.getString("currentID");






    }


    @Override
    public void setListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_step1, container, false);


        return v ;
    }

}
