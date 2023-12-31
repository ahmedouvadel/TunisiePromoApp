package com.example.projrcttunisiepromo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projrcttunisiepromo.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageViewProduct;
    TextView textViewProductId, textViewProductPrice, textViewProductName, textViewProductPromo, textViewProductDiscription;

    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        textViewProductName = findViewById(R.id.textViewProductName);
        textViewProductId = findViewById(R.id.textViewProductId);
        textViewProductPrice = findViewById(R.id.textViewProductPrice);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewProductPromo = findViewById(R.id.textViewProductPromo);
        textViewProductDiscription = findViewById(R.id.textViewProductDiscription);

        // Assuming you have passed the product ID to this activity using intent
        String productId = getIntent().getStringExtra("productId");

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);

        // Fetch product data from Firebase Realtime Database based on the product ID
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/products");
        databaseReference.orderByChild("productId").equalTo(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    textViewProductName.setText("Product Name: " + product.getName());
                    textViewProductId.setText("Product ID: " + product.getProductId());
                    textViewProductPrice.setText("Price:" + product.getProductprice() +"Dt");

                    // Load product image using Picasso
                    Picasso.get().load(product.getImageUrl()).into(imageViewProduct);

                    // Set other product details if needed
                    // textViewProductPromo.setText("Promo: " + product.getPromo());
                    // textViewProductDiscription.setText("Description: " + product.getDescription());
                }

                // Notify the adapter after updating the productList
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


}