package com.example.dokkanseller.views.orders.allOrders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.CartItem;
import com.example.dokkanseller.data_model.OrderItemModel;
import com.example.dokkanseller.views.base.BaseFragment;
import com.example.dokkanseller.views.login.LoginFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.dokkanseller.utils.Constants.ORDER_ID_KEY;


public class AllOrdersListFragment extends BaseFragment {


    private RecyclerView allOrderedRecyclerview;
    private AllOrdersAdapter adapter;

    private DatabaseReference databaseReference;
    public static List<OrderItemModel> orderItemModelList;
    public static int ORDERPOS;

    public AllOrdersListFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_orders_list;
    }

    @Override
    public void initializeViews(View view) {
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView(view);

        initRecView();
        fetchOrders();
    }

    @Override
    public void setListeners() {

    }


    private void fetchOrders() {
        Log.e("a", LoginFragment.USERID);
        Toast.makeText(getContext(), LoginFragment.USERID, Toast.LENGTH_LONG).show();

        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderItemModelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    OrderItemModel orderModel = dataSnapshot1.getValue(OrderItemModel.class);

                    Log.e("a", orderModel.toString());

                    for (CartItem x : orderModel.getCartItem())

                        if ((getUserIdWrapper()).equals(x.shopId))
                            orderItemModelList.add(orderModel);
                }

                adapter.setList(orderItemModelList);
                //Log.e("a",orderItemModelList.get(0).getCartItem().);
                //Toast.makeText(getContext(),orderItemModelList.size()+"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initRecView() {
        adapter = new AllOrdersAdapter();
        allOrderedRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((pos, orderItemModel) -> {


            ORDERPOS = pos;
            // HomeFragmentDirections.actionNavExploreToDetailsFragment(item);

            Bundle bundle = new Bundle();
            bundle.putString(ORDER_ID_KEY, orderItemModel.getKey());

            getNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment, bundle);
        });
    }


    private void initView(@NonNull final View itemView) {
        allOrderedRecyclerview = (RecyclerView) itemView.findViewById(R.id.recyclerview_id_all_product_list);
    }
}
