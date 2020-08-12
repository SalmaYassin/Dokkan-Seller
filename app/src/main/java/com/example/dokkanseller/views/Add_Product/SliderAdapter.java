package com.example.dokkanseller.views.Add_Product;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dokkanseller.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    ArrayList<Uri> mArrayUri;

    public SliderAdapter(Context context, ArrayList<Uri> mArrayUri) {
        this.context = context;
        this.mArrayUri = mArrayUri;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(final SliderAdapterVH viewHolder, final int position) {

        switch (position) {
            case 1:
                Glide.with(viewHolder.itemView).load(mArrayUri.get(1)).into(viewHolder.imageViewBackground);
                break;
            case 2:

                Glide.with(viewHolder.itemView).load(mArrayUri.get(2)).into(viewHolder.imageViewBackground);
                break;
            default:
                Glide.with(viewHolder.itemView).load(mArrayUri.get(0)).into(viewHolder.imageViewBackground);
                break;

        }

    }
    //================================
    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 3;
    }
    //===========================================================================//
    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
    //=======================================================================//
}