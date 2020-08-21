package com.example.dokkanseller.views.orders.OrderDetailsFragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.CartItem;
import com.example.dokkanseller.data_model.OrderItemModel;
import com.example.dokkanseller.views.login.Login;
import com.example.dokkanseller.views.orders.allOrders.AllOrdersAdapter;
import com.example.dokkanseller.views.orders.allOrders.AllOrdersListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {


    private CircleImageView customerProfileImg;
    private TextView customerName;
    private TextView customerPhone;
    private TextView customerAdress;
    private TextView orderDate;
    private RecyclerView cartItemsRec;

    OrderItemModel orderItemModel;
List <CartItem> cartItems = new ArrayList<>();
    private DatabaseReference databaseReference ;

    CartAdapter adapter ;
    private List<CartItem> cartItemList;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

         assert getArguments() != null;
        orderItemModel = (OrderItemModel) getArguments().getSerializable("order");

        initView(view );
        initRecView();
        fetchCarts();
        return view;
    }

    private void fetchCarts() {




            //adapter.setList(AllOrdersListFragment.orderItemModelList.get(AllOrdersListFragment.ORDERPOS).getCartItem());

        for(CartItem cartItem : orderItemModel.getCartItem())
        {
            if(cartItem.shopId.equals(Login.USERID))
                cartItems.add(cartItem);
        }
        adapter.setList(cartItems);

        /*databaseReference= FirebaseDatabase.getInstance().getReference("Orders");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               cartItemList = new ArrayList<CartItem>();
               for(DataSnapshot dataSnapshot1: snapshot.getChildren()) {
                   CartItem orderModel = dataSnapshot1.getValue(CartItem.class);
                   cartItemList.add(orderModel);
               }
               adapter.setList(cartItemList);
               Toast.makeText(getContext(),cartItemList.size()+" ",Toast.LENGTH_LONG).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

         */

    }

    private void initView(@NonNull final View itemView ) {
        customerProfileImg = (CircleImageView) itemView.findViewById(R.id.imgCustomerProfile);
        customerName = (TextView) itemView.findViewById(R.id.tvCustomerName);
        customerName.setText(orderItemModel.getAddress().getCustomerName());

        customerPhone = (TextView) itemView.findViewById(R.id.tvCustomerPhone);
        customerPhone.setText(orderItemModel.getAddress().getCustomerNumber());

        customerAdress = (TextView) itemView.findViewById(R.id.tvCustomerAdress);
        customerAdress.setText(orderItemModel.getAddress().getCustomerCountry() + " , "+ orderItemModel.getAddress().getCustomerAddress());

        orderDate = (TextView) itemView.findViewById(R.id.tvOrderDateValue);
        cartItemsRec = (RecyclerView) itemView.findViewById(R.id.rvCartItems);
    }

    private void initRecView() {
        adapter = new CartAdapter();
        cartItemsRec.setAdapter(adapter);

    }


}
