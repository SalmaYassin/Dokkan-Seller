package com.example.dokkanseller.views.Home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.engineering.dokkanseller.R;
import com.example.dokkanseller.data_model.ProductitemModel;
import com.example.dokkanseller.data_model.SliderItemModel;
import com.example.dokkanseller.views.base.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    //firebase
    private DatabaseReference dbReference;
   // private FirebaseStorage mstorge;
    private ArrayList<ProductitemModel> data;
    //slider
    private ArrayList<SliderItemModel> datasider;
    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    //ProductRecycAdapter.onItemClickListener ListenerProducts;
    FloatingActionButton floatingActionButton;

    //recyclerview
    private RecyclerView recyclerView;
    ProductRecycAdapter productAdapter;
    ProductRecycAdapter.ItemClickListener ListenerProducts;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
        ;
    }

    @Override
    public void initializeViews(View view) {
        floatingActionButton= (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        recyclerView = view.findViewById(R.id.recyclerview_id);
        data = new ArrayList<>();
        setListeners();
        SliderWork(view);

    }

    private void initViewModel() {
        floatingActionButton= (FloatingActionButton)view.findViewById(R.id.floating_action_button);
        recyclerView = view.findViewById(R.id.recyclerview_id);
        data = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //get all product of one category to sow it in recycelerview
    void getProductsByCategoryId(String catID) {
//        for(int i=0 ;i <data.size();i++) {
//            if(i==0){
//                data.add(new ProductitemModel(false,"add Item",R.drawable.ic_add_circle));
//            }else {
        dbReference = FirebaseDatabase.getInstance().getReference("products");
        Query query = FirebaseDatabase.getInstance().getReference("products")
                .orderByChild("categoryid").equalTo(catID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductitemModel categ = snapshot.getValue(ProductitemModel.class);
                    // categ.setKey(snapshot.getKey());
                    data.add(categ);
                }
                productAdapter = new ProductRecycAdapter(getContext() ,data, ListenerProducts);
                recyclerView.setAdapter(productAdapter);
                //  productAdapter.setOnItemClickListener(ListenerProducts);
                Log.d("STEP", "adapter: " + "done" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    // get data from firedase to array of slider and show it in viewpager2...
    private void showSlider(View view) {
        datasider = new ArrayList<>();
        //  productAdapter.setOnItemClickListener(ListenerProducts);
        dbReference = FirebaseDatabase.getInstance().getReference("categories");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SliderItemModel item = snapshot.getValue(SliderItemModel.class);
                    datasider.add(item);
                }
                //set adapter to view pager2
                sliderAdapter = new SliderAdapter(datasider, viewPager2);
                viewPager2.setAdapter(sliderAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
    // work the slider
    private void SliderWork(View view) {
        viewPager2 = view.findViewById(R.id.viewPagerSlider);
        showSlider(view);
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

    ///hen you add to item product of recyclerview
    @Override
    public void setListeners() {

        ListenerProducts = new ProductRecycAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final ProductitemModel item) {
                Toast.makeText(getActivity(), "item clicked", Toast.LENGTH_SHORT).show();
            }
        };
    }
    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }

    public void floating(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.

                Toast.makeText(getActivity(), "go to add item product", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
