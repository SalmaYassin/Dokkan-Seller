package com.example.dokkanseller.views.orders.allOrders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.OrderItemModel;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.AllOrdersVHolder>  {

    private List<OrderItemModel> orderItemModels = new ArrayList<>();


    @NonNull
    @Override
    public AllOrdersVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllOrdersVHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull AllOrdersVHolder holder, int position) {

         holder.productName.setText(orderItemModels.get(position).getCartItem().get(position).productName);

         holder.productQuantity.setText(orderItemModels.get(position).getCartItem().get(position).getProductQuanitity()+" ");
         // picaso for productImage
        holder.customerName.setText(orderItemModels.get(position).getAddress().getCustomerName());
        holder.customerPhoneNum.setText(orderItemModels.get(position).getAddress().getCustomerNumber());
        String address = orderItemModels.get(position).getAddress().getCustomerCountry() + orderItemModels.get(position).getAddress().customerAddress ;
        holder.customerAddressLocation.setText(address);
        Log.e("a",orderItemModels.get(position).getCartItem().size()+"");


    }

    @Override
    public int getItemCount() {

        return orderItemModels == null ? 0 : orderItemModels.size();
    }

    public void setList(List<OrderItemModel> orderItemModels) {
        this.orderItemModels = orderItemModels;
        notifyDataSetChanged();
    }


    public class AllOrdersVHolder extends RecyclerView.ViewHolder {

        private CircleImageView productImage;
        private TextView productName;
        private EditText productQuantity;
        private CircleImageView customerImage;
        private TextView customerName;
        private EditText customerPhoneNum;
        private EditText customerAddressLocation;
        private Button acceptBtn;
        private Button rejectBtn;

        public AllOrdersVHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(@NonNull final View itemView) {
            productImage = (CircleImageView) itemView.findViewById(R.id.product_image_0);
            productName = (TextView) itemView.findViewById(R.id.item_name_0);
            productQuantity = (EditText) itemView.findViewById(R.id.quantity_number_0);
            customerImage = (CircleImageView) itemView.findViewById(R.id.customer_image_0);
            customerName = (TextView) itemView.findViewById(R.id.customer_name_0);
            customerPhoneNum = (EditText) itemView.findViewById(R.id.customer_phone_num_0);
            customerAddressLocation = (EditText) itemView.findViewById(R.id.customer_address_location_0);
            acceptBtn = (Button) itemView.findViewById(R.id.accept_btn);
            rejectBtn = (Button) itemView.findViewById(R.id.reject_btn);
        }
    }
}
