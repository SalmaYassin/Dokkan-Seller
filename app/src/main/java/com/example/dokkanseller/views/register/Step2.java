package com.example.dokkanseller.views.register;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step2 extends BaseFragment {

    private Button perbtn ,donebtn ,categList;
    private CircleImageView vendoerImage;
    private EditText about ,policies ;
    private static final int GalleryPick = 1;
    String[]listItems;
    boolean[]checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    int i = 0;

    private StorageReference UserProfileImagesRef;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private Bundle bundle ;




    public Step2() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_step2;
    }

    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
    }



    @Override
    public void initializeViews(View view) {



        bundle = getArguments();
        final String currentID = bundle.getString("user_id2");
        Step1Model step1_model = bundle.getParcelable("step1_model");

        String loc = step1_model.getLocation();
        String number = step1_model.getPhoneNum();
        String face = step1_model.getFbLink();
        String insta = step1_model.getInstaLink();


        about = view.findViewById(R.id.aboutId);
        policies = view.findViewById(R.id.policiesId);
        String a = about.getText().toString();
        String b = policies.getText().toString();


        perbtn = view.findViewById(R.id.btnPrev);
        perbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavController().navigate(R.id.action_step2_to_step1);
            }
        });

        donebtn = view.findViewById(R.id.btnDone);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        vendoerImage = view.findViewById(R.id.profile_image);
        vendoerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });
        //--------------------- ALERT DIALOG Initialization ------------------------------------

        categList = view.findViewById(R.id.categ_butt);
        listItems = getResources().getStringArray(R.array.products);
        checkedItems = new boolean[listItems.length];
        categList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mB =new AlertDialog.Builder(getContext());

                mB.setTitle("The Products available in ashop");
                mB.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked){
                            if(!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }else if(mUserItems.contains(position)){
                                mUserItems.remove(position);
                            }
                        }

                    }
                });
                mB.setCancelable(false);
                mB.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mB.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mB.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i<checkedItems.length; i++){
                            checkedItems[i]=false;
                            mUserItems.clear();

                        }
                    }
                });
                AlertDialog m= mB.create();
                mB.show();
            }
        });

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                for (int positions : mUserItems) {
                    String CategID = listItems[positions];

                    RootRef = FirebaseDatabase.getInstance().getReference("shops").child(currentID)
                            .child("listOfcategIDs");
                    HashMap<String , String> map = new HashMap<>();
                    map.put( "ID"+positions , CategID);
                    RootRef.setValue(map);



                }


                                       }
});
    }

    //---------------------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            Uri ImageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getActivity());
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){

                Uri resultUri = result.getUri();
                StorageReference filepath = UserProfileImagesRef.child(currentUserID + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Profile Images uploaded successfully",Toast.LENGTH_SHORT).show();

                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(getContext(),"Error..." + message,Toast.LENGTH_SHORT).show();


                        }


                    }
                });

            }
        }

    }



    @Override
    public void setListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step2, container, false);
    }

}
