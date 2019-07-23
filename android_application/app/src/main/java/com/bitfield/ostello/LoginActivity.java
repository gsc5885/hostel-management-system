package com.bitfield.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editusername;
    private EditText editpassword;
    private FirebaseAuth appAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        appAuth = FirebaseAuth.getInstance();
        editusername = (EditText) findViewById(R.id.editusername);
        editpassword = (EditText) findViewById(R.id.editpassword);

        if(appAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    public void login(View view) {
        String username = editusername.getText().toString().trim() + "@ostello.com";
        String password = editpassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(this,"fields empty", Toast.LENGTH_LONG).show();
        }
        else {
            appAuth.signInWithEmailAndPassword(username,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            }
                            else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
}

