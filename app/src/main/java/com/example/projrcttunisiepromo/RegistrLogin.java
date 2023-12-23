package com.example.projrcttunisiepromo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrLogin extends AppCompatActivity {
    private EditText editTextName, editTextSurname, editTextPassword, editTextEmail;
    private CheckBox checkBoxMerchant, checkBoxCustomer;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextnom);
        editTextSurname = findViewById(R.id.editTextprenom);
        editTextEmail = findViewById(R.id.regstrTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextNumberPassword);
        buttonRegister = findViewById(R.id.buttonregistr);
        checkBoxMerchant = findViewById(R.id.checkBoxMerchant);
        checkBoxCustomer = findViewById(R.id.checkBoxCustomer);

        // Checkbox Logic
        checkBoxCustomer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxMerchant.setChecked(false);
        });

        checkBoxMerchant.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxCustomer.setChecked(false);
        });

        buttonRegister.setOnClickListener(v -> {
            if (validateForm()) {
                createUserAccount();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (editTextName.getText().toString().trim().isEmpty()) {
            editTextName.setError("Required.");
            valid = false;
        } else {
            editTextName.setError(null);
        }

        if (editTextSurname.getText().toString().trim().isEmpty()) {
            editTextSurname.setError("Required.");
            valid = false;
        } else {
            editTextSurname.setError(null);
        }

        if (editTextEmail.getText().toString().trim().isEmpty()) {
            editTextEmail.setError("Required.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (editTextPassword.getText().toString().trim().isEmpty()) {
            editTextPassword.setError("Required.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        if (!checkBoxCustomer.isChecked() && !checkBoxMerchant.isChecked()) {
            Toast.makeText(this, "Select the Account Type", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void createUserAccount() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(RegistrLogin.this, "Account Created.", Toast.LENGTH_SHORT).show();
                                saveUserData(user);
                                launchAppropriateIntent();
                            }
                        } else {
                            Toast.makeText(RegistrLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserData(FirebaseUser user) {
        DocumentReference df = db.collection("Users").document(user.getUid());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("FirstName", editTextName.getText().toString().trim());
        userInfo.put("LastName", editTextSurname.getText().toString().trim());
        userInfo.put("UserEmail", editTextEmail.getText().toString().trim());

        if (checkBoxCustomer.isChecked()) {
            userInfo.put("Customer", "1");
        } else if (checkBoxMerchant.isChecked()) {
            userInfo.put("Merchant", "1");
        }

        df.set(userInfo);
    }

    private void launchAppropriateIntent() {
        if (checkBoxCustomer.isChecked()) {
            startActivity(new Intent(this, marketplace.class));
        } else if (checkBoxMerchant.isChecked()) {
            startActivity(new Intent(this, merchantsplace.class));
        }
        finish();
    }
}