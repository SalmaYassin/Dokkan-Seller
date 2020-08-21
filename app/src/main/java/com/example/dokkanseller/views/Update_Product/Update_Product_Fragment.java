package com.example.dokkanseller.views.Update_Product;


import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dokkanseller.R;
import com.example.dokkanseller.views.Add_Product.LoadingDialog;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Update_Product_Fragment extends BaseFragment {
    SliderView sliderView;
    ImageView addphoto;
    EditText itemName, itemPrice, itemDescryption, itemSize, itemMaterials;


    Button up;
    ArrayList<Uri> mArrayUri;
    int PICK_IMAGE_MULTIPLE = 0;
    private DatabaseReference databaseReference;
    private static final String TAG = "MainActivity";
    private int finalUploads = 0;

    private  String productId ;
    private Bundle bundle_prodID;


    public Update_Product_Fragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_update__product_;
    }

    @Override
    public void initializeViews(View view) {

        bundle_prodID = getArguments();
        productId = bundle_prodID.getString("productId");
        Log.d("product id " , " id : " + productId);

        sliderView =view. findViewById(R.id.imageSlider);
        addphoto = view.findViewById(R.id.add_photo);
        itemName =view. findViewById(R.id.Item_name);
        itemPrice =view. findViewById(R.id.price);
        itemDescryption =view. findViewById(R.id.description);
        itemSize = view.findViewById(R.id.size);
        itemMaterials = view.findViewById(R.id.materials);
        up = view.findViewById(R.id.update);

        createInstance();

        ShowProductDetails();
        SliderShowAdapter adapter = new SliderShowAdapter(productId);
        sliderView.setSliderAdapter(adapter);


    }



    @Override
    public void setListeners() {
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showProgress(getActivity());
                ItemDetails();
                UploadAndStoreImages();

            }
        });
        //==========================================================================//
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(gallaryIntent, "Select Picture"), PICK_IMAGE_MULTIPLE);

            }
        });}
//================================================================================//

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

            SliderUpdateAdapter adapter2 = new SliderUpdateAdapter(getActivity() ,mArrayUri );
            sliderView.setSliderAdapter(adapter2);
        } else {
            Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //==============================================================================//
    private void ItemDetails() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products").child(productId);

      //  Log.i("pp", productId);
        databaseReference.child("name").setValue(itemName.getText().toString());
        databaseReference.child("price").setValue(itemPrice.getText().toString()+" L.E");
        databaseReference.child("description").setValue(itemDescryption.getText().toString());
        databaseReference.child("size").setValue(itemSize.getText().toString());
        databaseReference.child("materials").setValue(itemMaterials.getText().toString());


    }

    //==============================================================================//
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
                                getNavController().navigate(R.id.action_update_Product_Fragment_to_homeFragment2);
                            }
                        }
                    });

                }
            });

        }
    }

    //==============================================================================//
    private void storeLink(List<String> urlList) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("Image1",urlList.get(0));
//        hashMap.put("Image2", urlList.get(1));
//        hashMap.put("Image3", urlList.get(2));
        databaseReference.child(productId).child("image1").setValue(urlList.get(0));
        databaseReference.child(productId).child("image2").setValue(urlList.get(1));
        databaseReference.child(productId).child("image3").setValue(urlList.get(2));
    }
    //==============================================================================//
    private void ShowProductDetails() {

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pName = snapshot.child("name").getValue(String.class);
                Log.d("SHOW PRODUCT" , " name " + pName);
                itemName.setText(pName);
                String pPrice = snapshot.child("price").getValue(String.class);
                Log.d("SHOW PRODUCT" , " price " + pPrice);

                itemPrice.setText(pPrice);
                String pDescription = snapshot.child("description").getValue(String.class);
                itemDescryption.setText(pDescription);
                String pMaterial=snapshot.child("materials").getValue(String.class);
                itemMaterials.setText(pMaterial);
                String pSize=snapshot.child("size").getValue(String.class);
                itemSize.setText(pSize);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
//====================================================================================//

}
