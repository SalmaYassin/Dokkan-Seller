package com.example.dokkanseller.views.orders.allOrders;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.CartItem;
import com.example.dokkanseller.data_model.OrderItemModel;
import com.example.dokkanseller.views.login.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllOrdersListFragment extends Fragment {


    private RecyclerView allOrderedRecyclerview;
    private AllOrdersAdapter adapter ;

    private DatabaseReference databaseReference;
    public static List<OrderItemModel> orderItemModelList;
    public  static  int ORDERPOS ;

    public AllOrdersListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_orders_list, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initView(view);

        initRecView();
        fetchOrders();
        return view;
    }
    private void fetchOrders() {
        Toast.makeText(getContext(),Login.USERID,Toast.LENGTH_LONG).show();

        databaseReference.child("Orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderItemModelList = new ArrayList<OrderItemModel>();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            OrderItemModel orderModel = dataSnapshot1.getValue(OrderItemModel.class);

                            if (orderModel.getCartItem() != null) {
                                for (CartItem cartItem : orderModel.getCartItem()) {
                                    if ((Login.USERID).equals(cartItem.shopId)) {
                                        orderItemModelList.add(orderModel);
                                        break;
                                    }
                                }
                            }
                        }
                        adapter.setList(orderItemModelList);
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
        adapter.setOnItemClickListener(new AllOrdersAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int pos, OrderItemModel orderItemModel) {
              ORDERPOS=pos;
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", orderItemModel);
                getNavController().navigate(R.id.action_orderFragment_to_orderDetailsFragment,bundle);
            }
        });
    }

    NavController getNavController() {
        return Navigation.findNavController(getActivity(), R.id.my_nav_host);
    }

    private void initView(@NonNull final View itemView) {
        allOrderedRecyclerview = (RecyclerView) itemView.findViewById(R.id.recyclerview_id_all_product_list);
    }
}
