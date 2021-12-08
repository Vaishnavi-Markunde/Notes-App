package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText email, pass;
    private TextView txtsign;
    private Button login,signup;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.btnlogin);
        //signup= findViewById(R.id.btnsignup);
        txtsign=findViewById(R.id.textSignUp);
        progressBar= findViewById(R.id.progress_bar);

        View backgroundImage = findViewById(R.id.background);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(email.getText().toString(),pass.getText().toString());
                login.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        txtsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,SignUp.class));
               finish();
            }
        });


    }

  //  @Override
  public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }


    private void updateUI(Object o) {
        if(o!=null){
            Toast.makeText(this,"Signed in successfully! ",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,Home.class));
            finish();

        }
        else{
            Toast.makeText(this,"You didn't signed in!",Toast.LENGTH_SHORT);

        }
    }


    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.INVISIBLE);
                            updateUI(user);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void reload(){
        startActivity(new Intent(MainActivity.this,Home.class));
        finish();

    }

}