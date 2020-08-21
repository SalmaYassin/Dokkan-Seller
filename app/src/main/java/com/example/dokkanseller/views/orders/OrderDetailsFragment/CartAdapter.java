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
import com.example.dokkanseller.utils.Constants;
import com.example.dokkanseller.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

     List<CartItem> cartItemList = new ArrayList<>();
OnItemClickListener<CartItem> onItemClickListener;

    public CartAdapter(List<CartItem> cartItemList, OnItemClickListener<CartItem> onItemClickListener) {
        this.cartItemList = cartItemList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_product, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.itemName.setText(cartItemList.get(position).getProductName());
        holder.numberOfQuantity.setText(String.format("%d ", cartItemList.get(position).getProductQuanitity()));
        if (!cartItem.status.equals(Constants.PENDING)) {
            holder.acceptBtn.setVisibility(View.GONE);
            holder.rejectBtn.setVisibility(View.GONE);
            holder.statusBtn.setVisibility(View.VISIBLE);
            holder.statusBtn.setText(cartItem.status);
        }else {
            holder.acceptBtn.setVisibility(View.VISIBLE);
            holder.rejectBtn.setVisibility(View.VISIBLE);
            holder.statusBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return cartItemList == null ? 0 : cartItemList.size();
    }

    public void setList(List<CartItem> cartItem) {
        this.cartItemList = cartItem;
        notifyDataSetChanged();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView productImage;
        private TextView itemName;
        private TextView numberOfQuantity;
        private Button acceptBtn;
        private Button rejectBtn;
        private Button statusBtn;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
            acceptBtn.setOnClickListener(this);
            rejectBtn.setOnClickListener(this);
            statusBtn.setOnClickListener(this);
        }

        private void initView(@NonNull final View itemView) {
            productImage = (CircleImageView) itemView.findViewById(R.id.product_image_0);
            itemName = (TextView) itemView.findViewById(R.id.item_name_0);

            numberOfQuantity = (TextView) itemView.findViewById(R.id.quantity_number_0);
            acceptBtn = (Button) itemView.findViewById(R.id.accept_btn);
            rejectBtn = (Button) itemView.findViewById(R.id.reject_btn);
            statusBtn = (Button) itemView.findViewById(R.id.status_btn);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClicked(v,cartItemList.get(getLayoutPosition()));
        }
    }
}
