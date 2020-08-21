package com.example.dokkanseller.views.orders.OrderDetailsFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.CartItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItem = new ArrayList<>();



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_product, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.itemName.setText(cartItem.get(position).getProductName());
        holder.numberOfQuantity.setText(cartItem.get(position).getProductQuanitity()+" ");


    }

    @Override
    public int getItemCount() {

        return cartItem == null ? 0 : cartItem.size();
    }

    public void setList(List<CartItem> cartItem) {
        this.cartItem = cartItem;
        notifyDataSetChanged();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView productImage;
        private TextView itemName;
        private TextView numberOfQuantity;
        private Button acceptBtn;
        private Button rejectBtn;
        public CartViewHolder(@NonNull View itemView){
            super(itemView);
            initView(itemView);
        }
        private void initView(@NonNull final View itemView) {
            productImage = (CircleImageView) itemView.findViewById(R.id.product_image_0);
            itemName = (TextView) itemView.findViewById(R.id.item_name_0);

            numberOfQuantity = (TextView) itemView.findViewById(R.id.quantity_number_0);
            acceptBtn = (Button) itemView.findViewById(R.id.accept_btn);
            rejectBtn = (Button) itemView.findViewById(R.id.reject_btn);
        }
    }
}
