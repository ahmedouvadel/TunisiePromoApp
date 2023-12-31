package com.example.projrcttunisiepromo;

import static com.example.projrcttunisiepromo.R.id.TextInputDescription;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projrcttunisiepromo.Model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProduct extends AppCompatActivity {
    Button addProductbtn , listProductbtn, selectimagebtn;
    Uri selectedImageUri;
    EditText txtProductID , txtProductName, txtProductPrice, txtProductPromo ,Description;
    ImageView imageProductView;
    DatabaseReference databaseUsers;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        addProductbtn = findViewById(R.id.buttonAddProduct);
        listProductbtn = findViewById(R.id.buttonViewListProduct);
        imageProductView = findViewById(R.id.imageViewProduct);
        selectimagebtn = findViewById(R.id.buttonSelectImage);
        txtProductID = findViewById(R.id.editTextProductID);
        txtProductName = findViewById(R.id.editTextProductName);
        txtProductPrice = findViewById(R.id.editTextProductPrice);
        txtProductPromo =findViewById(R.id.editTextProductPromo);
        Description = findViewById(R.id.TextInputDescription);
        databaseUsers = FirebaseDatabase.getInstance().getReference();

        selectimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                getContent.launch(galleryIntent);
            }
        });

        addProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductData();
            }
        });

        listProductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProduct.this, merchantsplace.class));
                finish();
            }
        });
    }

    private void ProductData() {
        String productID = txtProductID.getText().toString();
        String productname = txtProductName.getText().toString();
        String productpriceStr = txtProductPrice.getText().toString();
        String productpromoStr = txtProductPromo.getText().toString();
        String description = Description.getText().toString();
        String id = databaseUsers.push().getKey();

        if (!productpriceStr.isEmpty()) {
            double productprice = Double.parseDouble(productpriceStr);
            if (!productpromoStr.isEmpty()) {
                double productpromo = Double.parseDouble(productpromoStr);
                if (selectedImageUri != null) {
                    // Upload image to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("UPLOAD");
                    UploadTask uploadTask = storageRef.putFile(selectedImageUri);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Create a Product object
                            Product product = new Product(productID, productname, productprice, productpromo, description, imageUrl);

                            // Save the product details to the Realtime Database
                            databaseUsers.child("products").child(productID).setValue(product)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AddProduct.this, "Product Inserted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            // Clear the input fields
                            txtProductID.setText("");
                            txtProductName.setText("");
                            txtProductPrice.setText("");
                            txtProductPromo.setText("");
                            Description.setText("");

                            // Inform the user that the product has been added
                            // You can add a Toast or any other UI feedback here

                        }).addOnFailureListener(e -> {
                            // Handle failure to get download URL
                        });
                    }).addOnFailureListener(e -> {
                        // Handle unsuccessful image upload
                    });
                } else {
                    // Handle the case where no image is selected
                    // You can add a Toast or any other UI feedback here
                }
            }
        }


    }
    private final ActivityResultLauncher<Intent> getContent =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri imageUri = data.getData();
                        selectedImageUri = imageUri;
                        Picasso.get().load(imageUri).into(imageProductView);
                    }
                }
            });
}