package com.bitfield.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth appAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appAuth = FirebaseAuth.getInstance();
        if(appAuth.getCurrentUser()== null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void logout(View view) {
        appAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
    public void Go_Leave(View view){
        finish();
        startActivity(new Intent(this,LeaveEntry.class));
    }
    public void Go_Complaint(View view){
        finish();
        startActivity(new Intent(this,HostelComplaint.class));
    }
}
