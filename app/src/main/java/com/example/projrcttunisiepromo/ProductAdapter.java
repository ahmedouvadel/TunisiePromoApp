package com.example.projrcttunisiepromo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projrcttunisiepromo.Model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener mListener; // Define the interface reference

    // Define an interface to handle clicks
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productID.setText("Product ID: " + product.getProductId());
        holder.Name.setText("Product Name: " + product.getName());
        holder.Price.setText("Price:" + product.getProductprice() +"Dt");
        // Use Glide to load the image from the URL
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.Img);

        // Set the click listener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productID, Name, Price;
        ImageView Img;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productID = itemView.findViewById(R.id.textviewproductID);
            Name = itemView.findViewById(R.id.textViewProductName);
            Price = itemView.findViewById(R.id.textViewProductPrice);
            Img = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
