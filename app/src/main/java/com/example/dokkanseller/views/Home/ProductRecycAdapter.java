package com.example.dokkanseller.views.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokkanseller.R;
import com.example.dokkanseller.data_model.ProductitemModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRecycAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context c;
    private ArrayList<ProductitemModel> productsList;
    private ItemClickListener onItemClickListener;


// constactor

    public ProductRecycAdapter(Context c, ArrayList<ProductitemModel> productsList, ItemClickListener onItemClickListener
    ) {
        this.c = c;
        this.productsList = productsList;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
       return new favouriteHolder( v , onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ProductitemModel itemList = productsList.get(position);
        final favouriteHolder favourite = (favouriteHolder) holder;
        Picasso.get().load(itemList.getImage()).into(favourite.Item_Image);
        favourite.Item_Name.setText(itemList.getName());
        favourite.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(c, favourite.moreBtn);
                popupMenu.inflate(R.menu.more_opition);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
//                               int position = getAdapterPosition();
                        switch (item.getItemId()) {
                            case R.id.update_opition_id:
//                                onItemClickListener.onUpdateClick(position);
                                Toast.makeText(c, "update", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete_opition_id:
//                                onItemClickListener.onDeleteClick(position);
                                Toast.makeText(c, "delete", Toast.LENGTH_SHORT).show();
                                return true;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ((favouriteHolder) holder).setDatainView(productsList.get(position));
//        ((favouriteHolder) holder).setmenuinView(productsList.get(position));


    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }


    class favouriteHolder extends RecyclerView.ViewHolder {
        TextView Item_Name;
        ImageView Item_Image;
        ItemClickListener itemClickListener;
        View rootView;
        ImageButton moreBtn;

        public favouriteHolder(@NonNull View itemView, final ItemClickListener itemClickListener

        ) {

            super(itemView);
            rootView = itemView;
            this.itemClickListener = itemClickListener;
            Item_Image = itemView.findViewById(R.id.itemImage);
            Item_Name = itemView.findViewById(R.id.itemName);
            moreBtn = itemView.findViewById(R.id.share_product);
        }

        public void setDatainView(final ProductitemModel productitemModel) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(productitemModel);
                }
            });
        }


}
//inner class
    interface ItemClickListener {
        void onItemClick(ProductitemModel item);
    }

}


