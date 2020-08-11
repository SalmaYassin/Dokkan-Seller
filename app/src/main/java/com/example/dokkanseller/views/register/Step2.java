package com.example.dokkanseller.views.register;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.Step1Model;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class
Step2 extends BaseFragment {

    private Button donebtn, categList;
    private EditText bio, about, policies;

    private CircleImageView vendoerImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private ProgressDialog loadingBar;
    Uri mImageUri;
    String url ;

    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    int i = 0;

    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private Bundle bundle;

    HashMap<String, String> map ;

    String currentID ,name , loc , number , face , insta;

    public Step2() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_step2;
    }

    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }

    @Override
    public void initializeViews(View view) {
        initialize(view);

        map =  new HashMap<>();
        bundle = getArguments();
        currentID = bundle.getString("user_id2");
        name = bundle.getString("name2");

        Step1Model step1_model = bundle.getParcelable("step1_model");
        loc = step1_model.getLocation();
        number = step1_model.getPhoneNum();
        face = step1_model.getFbLink();
        insta = step1_model.getInstaLink();


    }

    @Override
    public void setListeners() {
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                Log.d("ID_USER", "image  : "+ url );
            }
        });

        categList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogWork();
            }
        });

        vendoerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });



    }

    private void initialize(View view) {
        bio = view.findViewById(R.id.bioId);
        about = view.findViewById(R.id.aboutId);
        policies = view.findViewById(R.id.policiesId);
        donebtn = view.findViewById(R.id.btnDone);
        vendoerImage = view.findViewById(R.id.profile_image);
        categList = view.findViewById(R.id.categ_butt);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        listItems = getResources().getStringArray(R.array.products);
        checkedItems = new boolean[listItems.length];

        loadingBar = new ProgressDialog(getContext());


    }

    private void categoryDialogWork() {
        final AlertDialog.Builder mB = new AlertDialog.Builder(getContext());
        mB.setTitle("The Categories available in ashop");
        mB.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if (isChecked) {
                    if (!mUserItems.contains(position)) {
                        mUserItems.add(position);
                    }
                } else if (mUserItems.contains(position)) {
                    mUserItems.remove(position);
                }

            }
        });
        mB.setCancelable(false);
        mB.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ArrayList<String> categories = new ArrayList<>();
                for (int i = 0; i < mUserItems.size(); i++) {
                    categories.add(listItems[mUserItems.get(i)]);
                }
                categList.setText(categories.toString());
                categories.clear();
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
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();

                }
            }
        });
        AlertDialog m = mB.create();
        mB.show();
    }

    private void saveDetails(String currentID, String name, String loc, String number, String face, String insta) {
        final String a = about.getText().toString();
        Log.d("ID_USER", "about : "+a );
        final String b = policies.getText().toString();
        Log.d("ID_USER", "polic : " + b);
        final String c = bio.getText().toString();
        Log.d("ID_USER", "bio : "+ c );

        RootRef = FirebaseDatabase.getInstance().getReference("shops").child(currentID);
        map.put("key",currentID);
        map.put("shopName",name);
        map.put("location",loc);
        map.put("phoneNum",number);
        map.put("fbLink",face);
        map.put("instaLink",insta);
        map.put("shopImage",url);
        map.put("about",a);
        map.put("policies",b);
        map.put("bio",c);
        RootRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
             public void onSuccess(Void aVoid) {
                ArrayList<String> list = new ArrayList<>();
                for (int positions : mUserItems) {
                    String categName = listItems[positions];
                    list.add(categName);
                }
                RootRef.child("listOfcategIDs").setValue(list);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK&& data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(vendoerImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = storageReference.child("Images/" +System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = String.valueOf(uri);
                                    Log.d("ID_USER", "url image : "+ url );
                                    saveDetails(currentID, name, loc, number, face, insta);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}