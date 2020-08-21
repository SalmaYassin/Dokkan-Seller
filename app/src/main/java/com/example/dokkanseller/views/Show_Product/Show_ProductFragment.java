package com.example.dokkanseller.views.Show_Product;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.RateModel;
import com.example.dokkanseller.data_model.ReviewModel;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;


public class Show_ProductFragment extends BaseFragment {
    RecyclerView rv;
    ReviewAdapter adapter;
    RatingBar ratingBar;
    Button arrowBtn1;
    LinearLayout contaner;

    //product
    Button edit,back;
    TextView ProductName, productDescription, productPrice, ProductMaterial, productSize;
    //slider
    SliderView sliderView;
    ArrayList<ReviewModel> reviewList;
    ReviewModel reviewModel;
    //firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //private FirebaseStorage storage;
    // private StorageReference mStorageRef;
    String productId;
    private Bundle bundle_prodID;
    private CardView review ;

    private ArrayList<RateModel> rateList ;
    private double rateAverage = 0 ;

    public Show_ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_show__product;
    }

    @Override
    public void initializeViews(View view) {
        intialize(view);
        bundle_prodID = getArguments();
        productId = bundle_prodID.getString("productId");
        Log.d("product id " , " id : " + productId);

        ShowProductDetails(productId);
        ShowReviews(productId);
        SliderShowAdapter adapter = new SliderShowAdapter(getContext());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);



        SliderShowAdapter adapter2 = new SliderShowAdapter(productId);
        sliderView.setSliderAdapter(adapter2);

    }

    private void intialize(View view) {
        rateList = new ArrayList<>();

        review = view.findViewById(R.id.expandable);
        //slider
        sliderView = view.findViewById(R.id.imageSlider);
//Expand Review
        arrowBtn1 = view.findViewById(R.id.expand_more);
//recyclerView
        rv =view. findViewById(R.id.recycler);
// contaner = findViewById(R.id.contaner);
        ratingBar = view.findViewById(R.id.ratingBar);

//RatingBar For  The Item
        RatingBar rat_bar_item = view.findViewById(R.id.rate_bar_Item);
        float ratingNumber = rat_bar_item.getRating();
//product
        ProductName =view. findViewById(R.id.item_name);
        productPrice =view. findViewById(R.id.item_price);
        productDescription = view.findViewById(R.id.item_descroption);
        ProductMaterial = view.findViewById(R.id.item_material);
        productSize =view. findViewById(R.id.item_size);
        edit=view.findViewById(R.id.Edit_butt);
        back=view.findViewById(R.id.back_button);
    }

    @Override
    public void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to  home Activity
            }
        });


        //=====================================================================//
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to update Activity
                Bundle bundle = new Bundle();
                bundle.putString("productId",productId);
                Log.d("CATEG_ID" , " ID:" + productId);
                getNavController().navigate(R.id.action_show_ProductFragment_to_update_Product_Fragment,bundle);


            }
        });
        //=====================================================================================//

        // Expandable Review
        arrowBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rv.getVisibility() == View.GONE) {
                    // TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    rv.setVisibility(View.VISIBLE);
                    arrowBtn1.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp);
                } else {
                    // TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    rv.setVisibility(View.GONE);
                    arrowBtn1.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }
            }
        });

    }

    //===================================================================================//
    private void ShowProductDetails(String pID) {
        //Retrive all data about product

        databaseReference = FirebaseDatabase.getInstance().getReference("products").child(pID);
        databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String pName = dataSnapshot.child("name").getValue(String.class);
                ProductName.setText(pName);
                String pPrice = dataSnapshot.child("price").getValue(String.class);
                productPrice.setText(pPrice);
                String pDescription = dataSnapshot.child("description").getValue(String.class);
                productDescription.setText(pDescription);
                String pMaterial=dataSnapshot.child("materials").getValue(String.class);
                ProductMaterial.setText( pMaterial);
                String pSize=dataSnapshot.child("size").getValue(String.class);
                productSize.setText(pSize);

               DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("RatedList")
                       .child(productId).child("ListOfRated");
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
//
//                           DatabaseReference databaseReference = FirebaseDatabase.getInstance()
//                                   .getReference("products").child(productId);
//                           databaseReference.child("rate").setValue(  (float)(rateAverage / rateList.size() )  );
//
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });



           }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //===================================================================================//
    private void ShowReviews(String productId) {
        //Retrive RecyclerView
        Query query = FirebaseDatabase.getInstance().getReference("Reviews")
                .orderByChild("productID").equalTo(productId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList = new ArrayList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        reviewModel = snapshot.getValue(ReviewModel.class);
                        reviewList.add(reviewModel);
                    }
                    adapter = new ReviewAdapter(reviewList);
                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                    rv.setLayoutManager(lm);
                    rv.setAdapter(adapter);
                    DividerItemDecoration dv;
                    dv = new DividerItemDecoration(rv.getContext(), ((LinearLayoutManager) lm).getOrientation());
                    rv.addItemDecoration(dv);
                } else {
                    review.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }



}