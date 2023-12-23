package com.example.projrcttunisiepromo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projrcttunisiepromo.Model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class merchantsplace extends AppCompatActivity {

    private Button btnLogOut, btnAddProduct;
    private FirebaseAuth mAuth; // FirebaseAuth reference
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchantsplace);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance
        user = mAuth.getCurrentUser();

        if (user == null) {
            // Redirect user to Login Activity if not logged in
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return; // Stop executing further code since we are redirecting the user
        }

        btnLogOut = findViewById(R.id.buttonLogoutmerchands);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this); // Make sure you have a ProductAdapter class.
        recyclerView.setAdapter(productAdapter);

        loadProductsFromFirebase();

        btnAddProduct.setOnClickListener(v -> startActivity(new Intent(this, AddProduct.class)));

        btnLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        });
    }

    private void loadProductsFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}