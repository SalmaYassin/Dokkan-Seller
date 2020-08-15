package com.example.dokkanseller.views.Home;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.ProductitemModel;
import com.example.dokkanseller.data_model.SliderItemModel;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {
    private String currentUserID;

    private String categID;

    private DatabaseReference dbReference;
    //private StorageReference storageReference;
    private ArrayList<ProductitemModel> data;
    //slider
    private ArrayList<SliderItemModel> datasider;
    private ViewPager2 viewPager2;
    private SliderHomeAdapter sliderHomeAdapter;
    ProductRecycAdapter.ItemClickListener ListenerProducts;
    FloatingActionButton floatingActionButton;

    //recyclerview
    private RecyclerView recyclerView;
    ProductRecycAdapter productAdapter;

    ArrayList<String> listofCateg;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;

    }

    @Override
    public void initializeViews(View view) {
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        listofCateg = new ArrayList<>();
        getCategoriesNames();
        SliderWork(view);
        floatingActionButton= (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        recyclerView = view.findViewById(R.id.recyclerview_id);
        data = new ArrayList<>();

    }



    private void getCategoriesNames() {
        dbReference = FirebaseDatabase.getInstance().getReference("shops").child(currentUserID);
        dbReference.child("listOfcategIDs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getValue(String.class);
                    Log.d("getCategoriesNames() ", " name : " + name);
                    listofCateg.add(name);
                }
                showSlider(listofCateg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //get all product of one category to sow it in recycelerview
    void getProductsByCategoryId(String catID) {

        final Query query = FirebaseDatabase.getInstance().getReference("products");
        query.orderByChild("categoryid").equalTo(catID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductitemModel categ = snapshot.getValue(ProductitemModel.class);
                    //if (categ.getShopId().equals(currentUserID)) {
                        data.add(categ);
                   // }
                }
                productAdapter = new ProductRecycAdapter(getContext(), data, ListenerProducts);
                recyclerView.setAdapter(productAdapter);
                //  productAdapter.setOnItemClickListener(ListenerProducts);
                Log.d("STEP", "adapter: " + "done");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    // get data from firedase to array of slider and show it in viewpager2...
    private void showSlider(ArrayList<String> list) {
        datasider = new ArrayList<>();
        for (String name : list) {
            Log.d("LIST CATEGORIES", " name : " + name);
            Query query = FirebaseDatabase.getInstance().getReference("categories");
            query.orderByChild("categoryname").equalTo(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SliderItemModel item = snapshot.getValue(SliderItemModel.class);
                        datasider.add(item);
                    }
                    //set adapter to view pager2
                    sliderHomeAdapter = new SliderHomeAdapter(datasider, viewPager2);
                    viewPager2.setAdapter(sliderHomeAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        //  productAdapter.setOnItemClickListener(ListenerProducts);


    }


    // work the slider
    private void SliderWork(View view) {
        viewPager2 = view.findViewById(R.id.viewPagerSlider);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        // when slider take action....
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // if slider selected sow data of itis category in recyclerview ...
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // floating();
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                categID = datasider.get(position).getKey();
                getProductsByCategoryId(datasider.get(position).getKey());
                Log.d("PAGE_SELECTED", "onPageSelected: " + position);
                Log.d("SET CATID", "categoriesKEY: " + datasider.get(position).getKey());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        // method to make slider work agood...
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
            float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

            @Override
            public void transformPage(@NonNull View page, float position) {
                String TAG = "VIEWPAGER_POS";
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (position < -1) {
                    page.setTranslationX(-myOffset);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    Log.d(TAG, "transformPageVALUE: " + scaleFactor);
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0);
                    page.setTranslationX(myOffset);
                }

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }
    @Override
    public void setListeners() {
        ListenerProducts=new ProductRecycAdapter.ItemClickListener() {
            @Override
            public void onItemClick(ProductitemModel item) {
                Toast.makeText(getActivity(), "item clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateClick(int position, ProductitemModel productitemModel) {
                Bundle bundle = new Bundle();
                bundle.putString("productId", productitemModel.getProductId());
                //  Log.d("CATEG_ID" , " ID:" + categID);
                getNavController().navigate(R.id.action_homeFragment2_to_update_Product_Fragment, bundle);
            }

            @Override
            public void onDeleteClick(int position,final ProductitemModel item) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbReference = FirebaseDatabase.getInstance().getReference("products");
                        dbReference.child(item.getProductId()).removeValue();
//                       mQuery.orderByChild("productId").equalTo(item.getProductId())
//                               .addChildEventListener(new ValueEventListener() {
//                                   @Override
//                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                       for (DataSnapshot snap : snapshot.getChildren()) {
//                                           snap.getRef().removeValue();
//                                        }
//                                       Toast.makeText(getActivity(), "product deleted", Toast.LENGTH_SHORT).show();
//                                   }
//
//                                   @Override
//                                   public void onCancelled(@NonNull DatabaseError error) {
//                                       Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
//                                   }
//                               });
//
//

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create().show();
            }
        };
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("categId", categID );
                Log.d("CATEG_ID" , " ID:" + categID);
                getNavController().navigate(R.id.action_homeFragment2_to_add_Product_fragment , bundle);

            }
        });
    }

    private NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }




}