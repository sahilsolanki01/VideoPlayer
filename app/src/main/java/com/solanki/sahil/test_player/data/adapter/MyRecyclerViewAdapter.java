package com.solanki.sahil.test_player.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.solanki.sahil.test_player.R;
import com.solanki.sahil.test_player.data.model.Model_Items;
import com.solanki.sahil.test_player.databinding.ItemBinding;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {

    private List<Model_Items> mDataset;
    private LayoutInflater layoutInflater;
    private RecyclerViewClickListener mListener;


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        if(layoutInflater == null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemBinding itemBinding =
                DataBindingUtil.inflate(layoutInflater,
                        R.layout.list_item, parent, false);

        return new DataObjectHolder(itemBinding, mListener);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        Model_Items model_items = mDataset.get(position);
        holder.itemBinding.setViewmodel(model_items);

        /*holder.title.setText(mDataset.get(position).getTitle());

        Glide.with(holder.itemView)
                .load(mDataset.get(position).getThumbnail())
                .into(holder.thumbnail);*/
    }


    @Override
    public int getItemCount() {
        if(mDataset != null){
            return mDataset.size();
        } else
            return 0;
    }

    public void setModelList(ArrayList<Model_Items> items, RecyclerViewClickListener listener) {
        this.mDataset = items;
        this.mListener = listener;
        notifyDataSetChanged();
    }



    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private TextView title;
        //private ImageView thumbnail;
        private RecyclerViewClickListener mListener;
        private ItemBinding itemBinding;

        public DataObjectHolder(ItemBinding itemBinding, RecyclerViewClickListener listener) {
            /*super(itemView);
            title = itemView.findViewById(R.id.title);
            thumbnail = itemView.findViewById(R.id.thumbnail);*/

            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            mListener = listener;
            itemBinding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());

        }
    }


}
