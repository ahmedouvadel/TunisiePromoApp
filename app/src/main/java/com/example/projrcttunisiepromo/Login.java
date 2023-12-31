package com.example.projrcttunisiepromo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    private TextView signUp, signuplattkalatalik, textView, textView2;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textView2 =findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);
        edittextEmailLogin = findViewById(R.id.loginTextEmailAddress);
        edittextPasswordLogin = findViewById(R.id.loginNumberPassword);
        buttonLogin = findViewById(R.id.buttonlogin);
        signuplattkalatalik = findViewById(R.id.signuplattkalatalik);
        textView = findViewById(R.id.textView);
        signUp = findViewById(R.id.signuplattkalatalik);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Hide login components
                edittextEmailLogin.setVisibility(View.INVISIBLE);
                edittextPasswordLogin.setVisibility(View.INVISIBLE);
                buttonLogin.setVisibility(View.INVISIBLE);
                signUp.setVisibility(View.INVISIBLE);
                signuplattkalatalik.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                simulateRegistration();
                setElevation(true);
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

    // Simulate a registration operation
    private void simulateRegistration() {
        // You should replace this with your actual registration logic

        // Start the progress bar update in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int totalProgressTime = 5000; // Total time for the registration simulation in milliseconds
                int interval = 500; // Update interval for the progress bar
                int progress = 0;

                while (progress <= 100) {
                    final int currentProgress = progress;

                    // Update the progress bar on the main (UI) thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentProgress);
                        }
                    });

                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progress += (interval * 100) / totalProgressTime;
                }
            }
        }).start();
    }

    private void setElevation(final boolean active) {
        // Update the elevation on the main (UI) thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                View parentLayout = findViewById(R.id.LoginPageID); // Replace with the actual ID
                float elevation = active ? getResources().getDimension(R.dimen.elevation_active) : getResources().getDimension(R.dimen.elevation_inactive);
                ViewCompat.setElevation(parentLayout, elevation);
            }
        });
    }

    public void signUp(View view) {
        startActivity(new Intent(Login.this, RegistrLogin.class));
    }
}
