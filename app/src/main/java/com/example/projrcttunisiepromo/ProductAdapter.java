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

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
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

        holder.productID.setText(product.getProductId());
        holder.Name.setText(product.getName());
        holder.Price.setText(String.valueOf(product.getProductprice()));
        // Use Glide to load the image from the URL
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.Img);




    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productID , Name, Price;
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
