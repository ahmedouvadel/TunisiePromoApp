package com.example.projrcttunisiepromo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private EditText edittextEmailLogin, edittextPasswordLogin;
    private Button buttonLogin ;
    private TextView signUp;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edittextEmailLogin = findViewById(R.id.loginTextEmailAddress);
        edittextPasswordLogin = findViewById(R.id.loginNumberPassword);
        buttonLogin = findViewById(R.id.buttonlogin);
        signUp = findViewById(R.id.signuplattkalatalik);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String email = edittextEmailLogin.getText().toString().trim();
        String password = edittextPasswordLogin.getText().toString().trim();

        if (!validateForm(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null; // If task is successful, user should never be null.
                            handleLoginSuccess(user);
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (email.isEmpty()) {
            edittextEmailLogin.setError("Required.");
            valid = false;
        } else {
            edittextEmailLogin.setError(null);
        }

        if (password.isEmpty()) {
            edittextPasswordLogin.setError("Required.");
            valid = false;
        } else {
            edittextPasswordLogin.setError(null);
        }

        return valid;
    }

    private void handleLoginSuccess(FirebaseUser user) {
        checkUserAccessLevel(user.getUid());
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = db.collection("Users").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Check if the user is a customer or a merchant and start the appropriate activity
                        if (documentSnapshot.getString("Customer") != null) {
                            startActivity(new Intent(Login.this, marketplace.class));
                        } else if (documentSnapshot.getString("Merchant") != null) {
                            startActivity(new Intent(Login.this, merchantsplace.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(Login.this, "User doesn't exist or is missing a role.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Failed to check user access level.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUp(View view) {
        startActivity(new Intent(Login.this, RegistrLogin.class));
    }
}
