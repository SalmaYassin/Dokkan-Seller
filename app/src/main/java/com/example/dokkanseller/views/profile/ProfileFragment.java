package com.example.dokkanseller.views.profile;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.ProfileModel;
import com.example.dokkanseller.data_model.ProfileReviewModel;
import com.example.dokkanseller.data_model.RateModel;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {
    private String currentUserID;


    ProfileModel profile ;
    //review
    private ArrayList<ProfileReviewModel> reviewList;
    private RecyclerView reviewRecyclerView;

    //shop details
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private ProgressDialog loadingBar;
    Uri mImageUri;
    String url ;

    private TextView shopname , location , desc ,about , policies , phone_num ;
    private Button fblink , instalink  , done;
    private ImageView shopimg  , edit_name , edit_phone , edit_bio , edit_location , edit_about , edit_policies , edit_link , edit_img;
    private RatingBar ratingBar ;
    private String fb_link , insta_link  ;
    private EditText nameET , phoneET , locationET , descET  , aboutET , policiesET , fbET , instaET ;

    RelativeLayout review_relative ;
    private ArrayList<RateModel> rateList ;
    private  double rateAverage = 0 ;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initializeViews(View view) {
        initialization(view);
        showShopDetails(currentUserID);
        showShopReviews(currentUserID);

    }

    private void initialization(View view) {
        storageReference = FirebaseStorage.getInstance().getReference();
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();

        reviewList = new ArrayList<>();
        reviewRecyclerView = view.findViewById(R.id.recyclerview_review);

        rateList = new ArrayList<>();

        shopimg = view.findViewById(R.id.img_shop) ;

        shopname = view.findViewById(R.id.name_shop);
            location = view.findViewById(R.id.location);
            desc = view.findViewById(R.id.shop_desc);
            ratingBar = view.findViewById(R.id.rating_barprofile) ;
            about = view.findViewById(R.id.about_tv);
            policies = view.findViewById(R.id.policies_tv);
            fblink = view.findViewById(R.id.tv_fb);
            instalink = view.findViewById(R.id.tv_insta);
            phone_num = view.findViewById(R.id.phone_num);
            review_relative = view.findViewById(R.id.relative_review);

            edit_name = view.findViewById(R.id.edit_shopname);
            edit_phone = view.findViewById(R.id.edit_phone);
            edit_bio = view.findViewById(R.id.edit_bio);
            edit_location= view.findViewById(R.id.edit_location);
            edit_about = view.findViewById(R.id.edit_about);
            edit_policies = view.findViewById(R.id.edit_policies);
            edit_link = view.findViewById(R.id.edit_relatedlinks);
            edit_img = view.findViewById(R.id.edit_photo);

            done = view.findViewById(R.id.ok);
            nameET = view.findViewById(R.id.shop_name_et);
            phoneET = view.findViewById(R.id.phone_num_et);
            locationET = view.findViewById(R.id.location_et);
            descET = view.findViewById(R.id.shop_desc_et);
            aboutET = view.findViewById(R.id.about_et);
            policiesET = view.findViewById(R.id.policies_et);
            fbET = view.findViewById(R.id.fb_et);
            instaET = view.findViewById(R.id.insta_et);
        }


    @Override
    public void setListeners() {

        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                done.setVisibility(View.VISIBLE);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadFile();
                        done.setVisibility(View.GONE);
                    }
                });
            }
        });

        fblink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(fb_link) ;
                Intent i = new Intent(Intent.ACTION_VIEW , uri) ;
                startActivity(i);
            }
        });

        instalink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(insta_link);
                Intent i = new Intent(Intent.ACTION_VIEW , uri) ;
                startActivity(i);
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_name.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);

                shopname.setVisibility(View.INVISIBLE);
                nameET.setVisibility(View.VISIBLE);
                nameET.setText(profile.getShopName());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("shopName").setValue( nameET.getText().toString() );
                        shopname.setVisibility(View.VISIBLE);
                        shopname.setText(nameET.getText().toString());
                        nameET.setVisibility(View.INVISIBLE);

                        edit_name.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

        edit_bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_bio.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);

                desc.setVisibility(View.INVISIBLE);
                descET.setVisibility(View.VISIBLE);
                descET.setText(profile.getBio());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("bio").setValue( descET.getText().toString() );
                        desc.setVisibility(View.VISIBLE);
                        desc.setText(descET.getText().toString());
                        descET.setVisibility(View.INVISIBLE);

                        edit_bio.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_location.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);

                location.setVisibility(View.INVISIBLE);
                locationET.setVisibility(View.VISIBLE);
                locationET.setText(profile.getLocation());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("location").setValue( locationET.getText().toString() );
                        location.setVisibility(View.VISIBLE);
                        location.setText(locationET.getText().toString());
                        locationET.setVisibility(View.INVISIBLE);

                        edit_location.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                });


            }
        });

        edit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_phone.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);

                phone_num.setVisibility(View.INVISIBLE);
                phoneET.setVisibility(View.VISIBLE);
                phoneET.setText(profile.getPhoneNum());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("phoneNum").setValue( phoneET.getText().toString() );
                        phone_num.setVisibility(View.VISIBLE);
                        phone_num.setText(phoneET.getText().toString());
                        phoneET.setVisibility(View.INVISIBLE);
                        edit_phone.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

        edit_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done.setVisibility(View.VISIBLE);

                about.setVisibility(View.GONE);
                aboutET.setVisibility(View.VISIBLE);
                aboutET.setText(profile.getAbout());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("about").setValue( aboutET.getText().toString() );
                        about.setVisibility(View.VISIBLE);
                        about.setText(aboutET.getText().toString());
                        aboutET.setVisibility(View.GONE);

                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

        edit_policies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done.setVisibility(View.VISIBLE);

                policies.setVisibility(View.GONE);
                policiesET.setVisibility(View.VISIBLE);
                policiesET.setText(profile.getPolicies());

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("policies").setValue( policiesET.getText().toString() );
                        policies.setVisibility(View.VISIBLE);
                        policies.setText(aboutET.getText().toString());
                        policiesET.setVisibility(View.GONE);

                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

        edit_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done.setVisibility(View.VISIBLE);

                fbET.setVisibility(View.VISIBLE);
                fbET.setText(profile.getFbLink());

                instaET.setVisibility(View.VISIBLE);
                instaET.setText(profile.getInstaLink());


                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                .child(currentUserID);
                        databaseReference.child("fbLink").setValue( fbET.getText().toString() );
                        databaseReference.child("instaLink").setValue( instaET.getText().toString() );

                        fbET.setVisibility(View.GONE);
                        instaET.setVisibility(View.GONE);

                        fb_link = fbET.getText().toString();
                        insta_link = instaET.getText().toString();

                        done.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    private void showShopDetails(final String id) {
        final Query query = FirebaseDatabase.getInstance().getReference("shops")
                .orderByChild("key").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    profile = snapshot.getValue(ProfileModel.class);
                    Picasso.get().load(profile.getShopImage()).into(shopimg);
                    shopname.setText(profile.getShopName());
                    ratingBar.setRating(profile.getRate());
                    location.setText(profile.getLocation());
                    desc.setText(profile.getBio());
                    about.setText(profile.getAbout());
                    policies.setText(profile.getPolicies());
                    phone_num.setText(profile.getPhoneNum());
                    fb_link = profile.getFbLink();
                    insta_link = profile.getInstaLink();

                    DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("RatedList")
                            .child(id).child("ListOfRated");
                    dbreference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rateList.clear();
                            if ( dataSnapshot.exists()){
                                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    RateModel rateModel = snapshot.getValue(RateModel.class);
                                    rateAverage = rateAverage + rateModel.getRate() ;
                                    rateList.add(rateModel);
                                }
                                ratingBar.setRating( (float)(rateAverage / rateList.size() ) );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity() , databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showShopReviews(String shopid) {

        Query query = FirebaseDatabase.getInstance().getReference("Reviews")
                .orderByChild("shopID").equalTo(shopid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    review_relative.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProfileReviewModel reviewModel = snapshot.getValue(ProfileReviewModel.class);
                        reviewList.add(reviewModel);
                    }
                    ReviewsRecyclerAdapter adapter = new ReviewsRecyclerAdapter(reviewList);
                    reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    reviewRecyclerView.setAdapter(adapter);
                    DividerItemDecoration dv;
                    dv = new DividerItemDecoration(reviewRecyclerView.getContext(),
                            ((LinearLayoutManager)new LinearLayoutManager(getActivity()) ).getOrientation());
                    reviewRecyclerView.addItemDecoration(dv);

                } else{
                    review_relative.setVisibility(View.GONE);
                    reviewRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity() , databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK&& data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(shopimg);
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
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("shops")
                                            .child(currentUserID);
                                    databaseReference.child("shopImage").setValue(url);

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


    








