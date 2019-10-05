package com.canteen.net.cafeteria.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.canteen.net.cafeteria.Interface.ItemClickListener;
import com.canteen.net.cafeteria.R;

import org.w3c.dom.Text;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView foods_name;
    public ImageView foods_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public FoodViewHolder(View itemView) {
        super(itemView);

        foods_name = itemView.findViewById(R.id.foods_name);
        foods_image = itemView.findViewById(R.id.foods_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
