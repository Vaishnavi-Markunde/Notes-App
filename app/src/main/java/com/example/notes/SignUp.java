package com.example.notes;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.notes.R.layout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText email, pass;
    private TextView txtview;
    private Button login, signup;
    ProgressBar progressBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_barSign);
        email = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        txtview=findViewById(R.id.textLogin);
        signup = findViewById(R.id.btnsignup);

        View backgroundImage = findViewById(R.id.signbg);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(50);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(email.getText().toString(), pass.getText().toString());
                signup.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
                finish();
            }
        });
    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            signup.setVisibility(View.VISIBLE);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Sign Up failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Toast.makeText(this,"Signed up successfully! ",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUp.this,Home.class));
        }
        else{
            Toast.makeText(this,"You didn't signed in!",Toast.LENGTH_SHORT);

        }
    }
}
