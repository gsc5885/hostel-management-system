package com.bitfield.ostello;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;


public class HostelComplaint extends AppCompatActivity {
    private FirebaseAuth appAuth;
    private FirebaseUser user;
    private TextView textView;
    private Button button, enterComplaint, col1, col2;
    private Student student;
    private EditText compl_text;
    private TableLayout tableLayout;

    private String rollno;

    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef2;

    private ViewGroup.LayoutParams params1, params2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_complaint);

        textView = findViewById(R.id.tv3);
        button = findViewById(R.id.btn2);
        col1 = findViewById(R.id.btn5);
        col2 = findViewById(R.id.btn4);
        enterComplaint = findViewById(R.id.btn3);
        tableLayout = findViewById(R.id.tablelayout2);
        compl_text = findViewById(R.id.comp);

        col1.post(new Runnable() {
            @Override
            public void run() {
                params1 = col1.getLayoutParams();
            }
        });
        col2.post(new Runnable() {
            @Override
            public void run() {
                params2 = col2.getLayoutParams();
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    logout();
                }
                return false;
            }
        });

        enterComplaint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    EnterComplaint();
                }
                return false;
            }
        });

        appAuth = FirebaseAuth.getInstance();
        if(appAuth.getCurrentUser()== null)
        {
            finish();
            startActivity(new Intent(this , LoginActivity.class));
        }

        user = appAuth.getCurrentUser();
        rollno = user.getEmail().replaceAll("@ostello.com", "");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Students/"+rollno);
        myRef2 = database.getReference("Managers/Complaints/"+rollno);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                Log.i("xyxyx", "comp size " + student.Complaints.size());
                textView.setText("Welcome, " + student.name + " !");
                tableLayout.removeAllViews();
                for(int i=student.Complaints.size()-1 ; i>=0; i--){
                    Button b = new Button(getApplicationContext());
                    b.setText(student.Complaints.get(i).text);
                    b.setLayoutParams(params1);
                    b.setBackgroundResource(R.drawable.scroll2);
                    b.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    b.setTextColor(Color.BLACK);
                    Log.i("xyxyx", "onDataChange " + b.getWidth() + " " + col1.getWidth());
                    b.setId(i);
                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    tr.addView(b);
                    b = new Button(getApplicationContext());
                    b.setText("X");
                    b.setId(-i);
                    b.setBackgroundResource(R.drawable.scroll2);
                    b.setLayoutParams(params2);
                    b.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    b.setTextColor(Color.BLACK);
                    Log.i("xyxyx", "onDataChange: width " + col2.getWidth() + " 2 : " + col1.getWidth() + " 3 " + b.getWidth());
                    b.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                student.Complaints.remove(-view.getId());
                                myRef.child("Complaints/").setValue(student.Complaints);
                                if(student.Complaints.size()==0){
                                    myRef2.removeValue();
                                }
                                else{
                                    myRef2.setValue(student.Complaints.size());
                                }
                            }
                            return false;
                        }
                    });
                    tr.addView(b);
                    tableLayout.addView(tr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void EnterComplaint() {
        if(enterComplaint.getText().toString().equals("OK")){
            String text =  compl_text.getText().toString();
            if(text.isEmpty()){
                Toast toast = Toast.makeText(this, "message", Toast.LENGTH_SHORT);
                toast.setText("Please Enter Complaint text!");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                Date date = new Date();
                Complaint newcomp = new Complaint();
                newcomp.date = date;
                newcomp.rollno = rollno;
                newcomp.text = text;

                myRef.child("Complaints").child(String.valueOf(student.Complaints.size())).setValue(newcomp);
                Log.i("xyxyx", "EnterComplaint prev: " + student.Complaints.size());
                student.Complaints.add(newcomp);
                Log.i("xyxyx", "EnterComplaint after: " + student.Complaints.size());
                myRef2.setValue(student.Complaints.size());
            }
            enterComplaint.setText("Enter Complaint");
            compl_text.setVisibility(View.INVISIBLE);
            tableLayout.setVisibility(View.VISIBLE);

        }
        else if(enterComplaint.getText().toString().equals("Enter Complaint")){
            compl_text.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.INVISIBLE);
            enterComplaint.setText("OK");
        }
    }

    public void logout() {
        appAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void Back(View view){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }
}
