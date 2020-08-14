package com.example.dokkanseller.views.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;

import java.util.ArrayList;
import java.util.List;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.ProcessHolder> {

    private List<OrderItemModel> itemList;

    public ProcessAdapter(ArrayList<OrderItemModel> data) {

    }

    @NonNull
    @Override
    public ProcessAdapter.ProcessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_item, parent, false);
        return new ProcessHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessAdapter.ProcessHolder holder, int position) {
        holder.product_Image.setImageResource(itemList.get(position).getItem_image());
        holder.product_name.setText(itemList.get(position).getItem_name());
        holder.product_quantity.setText(itemList.get(position).getItem_quantity());
        holder.product_quantity_number.setText(itemList.get(position).getItem_quantity_number());

        holder.customer_Image.setImageResource(itemList.get(position).getCustomer_image());
        holder.customer_name.setText(itemList.get(position).getCustomer_name());
        holder.customer_phone.setText(itemList.get(position).getCustomer_phone());
        holder.customer_phone_number.setText(itemList.get(position).getCustomer_phone_number());
        holder.customer_address.setText(itemList.get(position).getCustomer_address());
        holder.customer_address_details.setText(itemList.get(position).getCustomer_address_details());

        holder.product_statue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ProcessHolder extends RecyclerView.ViewHolder {
        ImageView product_Image;
        TextView product_name;
        TextView product_quantity;
        EditText product_quantity_number;
        Button product_statue;
        ImageView customer_Image;
        TextView customer_name;
        TextView customer_phone;
        EditText customer_phone_number;
        TextView customer_address;
        EditText customer_address_details;

        public ProcessHolder(@NonNull View itemView) {
            super(itemView);
            product_Image = itemView.findViewById(R.id.product_image_1);
            product_name = itemView.findViewById(R.id.item_name_1);
            product_quantity= itemView.findViewById(R.id.quantity_1);
            product_quantity_number= itemView.findViewById(R.id.quantity_number_1);
            product_statue = itemView.findViewById(R.id.btn_next_1);
            customer_Image = itemView.findViewById(R.id.customer_image_1);
            customer_name = itemView.findViewById(R.id.customer_name_1);
            customer_phone = itemView.findViewById(R.id.customer_phone_1);
            customer_phone_number = itemView.findViewById(R.id.customer_phone_num_1);
            customer_address = itemView.findViewById(R.id.customer_address_1);
            customer_address_details = itemView.findViewById(R.id.customer_address_location_1);
        }
    }
}
