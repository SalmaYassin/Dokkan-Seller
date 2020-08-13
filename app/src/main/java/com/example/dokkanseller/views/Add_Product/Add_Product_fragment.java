package com.example.dokkanseller.views.Add_Product;


import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.MainActivity;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Add_Product_fragment extends BaseFragment {
    SliderView sliderView;
    ImageView addphoto;
    TextView itemName, itemPrice, itemDescryption, itemSize, itemMaterials;
    Button post_butt,back;
    ArrayList<Uri> mArrayUri;
    private   String productId ;
    int PICK_IMAGE_MULTIPLE = 0;
    private DatabaseReference databaseReference;
    private static final String TAG = "MainActivity";
    private int finalUploads = 0;

    public Add_Product_fragment() {
        // Required empty public constructor
    }

    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }

    @Override
    public int getLayoutId() {
        return  R.layout.fragment_add__product_fragment;
    }

    @Override
    public void initializeViews(View view) {
        sliderView = view.findViewById(R.id.imageSlider);
        addphoto = view.findViewById(R.id.add_photo);
        itemName =view. findViewById(R.id.Item_name);
        itemPrice =view. findViewById(R.id.price);
        itemDescryption = view.findViewById(R.id.description);
        itemSize =view. findViewById(R.id.size);
        itemMaterials = view.findViewById(R.id.materials);
        post_butt = view.findViewById(R.id.post_butt);
        back=view.findViewById(R.id.back_button);
        createInstance();
    }

    @Override
    public void setListeners() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

       // getNavController().navigate(R.id.action_add_Product_fragment_to_emptyFragment);
            }
        });



        //==========================================================================//
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                        .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(gallaryIntent, "Select Picture"), PICK_IMAGE_MULTIPLE);

            }
        });
        //==========================================================================//
        post_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showProgress(getActivity());
                ItemDetails();
              UploadAndStoreImages();

            }
        });
//==============================================================================//

    }
    private void ItemDetails() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products").push();
        productId = databaseReference.getKey();
        databaseReference.child("name").setValue(itemName.getText().toString());
        databaseReference.child("price").setValue(itemPrice.getText().toString()+" $");
        databaseReference.child("description").setValue(itemDescryption.getText().toString());
        databaseReference.child("size").setValue(itemSize.getText().toString());
        databaseReference.child("materials").setValue(itemMaterials.getText().toString());
        databaseReference.child("productId").setValue(productId);

    }

    //=========================================================================//
    private void createInstance() {
        mArrayUri = new ArrayList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK) {

            //If Single image selected then it will fetch from Gallery
            if (data.getData() != null) {
                android.net.Uri mImageUri = data.getData();
                mArrayUri.add(mImageUri);
                Log.i(TAG, " Select one image: " + mImageUri.toString());
            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        Uri uri = mClipData.getItemAt(i).getUri();
                        mArrayUri.add(uri);
                    }
                    Log.i(TAG, "Selected Images" + mArrayUri.size());
                }
            }

            SliderAdapter adapter2 = new SliderAdapter(getActivity(), mArrayUri);
            sliderView.setSliderAdapter(adapter2);
        } else {
            Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //===========================================================================//
    private void UploadAndStoreImages() {

//upload images on firebase Storage

        // to saveURL
        final List<String> imagesURL = new ArrayList<>();
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

        for (int uploads = 0; uploads < mArrayUri.size(); uploads++) {

            Uri Image = mArrayUri.get(uploads);
            final StorageReference imageName = ImageFolder.child("image/" + Image.getLastPathSegment());
            imageName.putFile(mArrayUri.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            imagesURL.add(String.valueOf(uri));
                            Log.i(TAG, String.valueOf(uri));
                            finalUploads++;

                            if (finalUploads == mArrayUri.size()) {
                                storeLink(imagesURL);
                                Toast.makeText(getActivity(), " upload success..", Toast.LENGTH_LONG).show();
                                LoadingDialog.hideProgress();
                                // Intent GoToHome = new Intent(MainActivity.this, HomeActivity.class);
                                //  startActivity(GoToHome);
                            }
                        }
                    });

                }
            });

        }
    }

    //===========================================================================//
    private void storeLink(List<String> urlList) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("Image1",urlList.get(0));
        hashMap.put("Image2", urlList.get(1));
        hashMap.put("Image3", urlList.get(2));
        databaseReference.child(productId).child("image1").setValue(urlList.get(0));
        databaseReference.child(productId).child("image2").setValue(urlList.get(1));
        databaseReference.child(productId).child("image3").setValue(urlList.get(2));
    }
//==============================================================================//
}


