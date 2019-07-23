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
import android.widget.CalendarView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveEntry extends AppCompatActivity {

    private FirebaseAuth appAuth;
    private FirebaseUser user;
    private TextView textView;
    private Button button, enterLeave, col1, col2, from, to;
    private Student student;
    private TableLayout tableLayout;
    private CalendarView calendarView;

    private String strDateFormat = "dd/MM";
    private DateFormat dateFormat;
    private String rollno;

    private FirebaseDatabase database;
    private DatabaseReference myRef, myRef2;

    private ViewGroup.LayoutParams params1, params2;
    private Date from_date, to_date;
    private static Date cal_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_entry);

        textView = findViewById(R.id.tv3);
        button = findViewById(R.id.button2);
        col1 = findViewById(R.id.btn5);
        col2 = findViewById(R.id.button4);
        enterLeave = findViewById(R.id.btn3);
        tableLayout = findViewById(R.id.tablelayout);
        calendarView = findViewById(R.id.calendarView);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);

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

        dateFormat = new SimpleDateFormat(strDateFormat);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                String d = String.valueOf(date) + "/" + String.valueOf(month+1);
                try {
                    cal_date = dateFormat.parse(d);
                } catch (ParseException e) {
                    Log.i("xyxyx", "Tosss: error ");
                    e.printStackTrace();
                }
                Log.i("xyxyx", "Tosss: " + dateFormat.format(cal_date)+ " "+ d+ cal_date);
                Toast.makeText(getApplicationContext(),date+ "/"+month+"/"+year,Toast.LENGTH_SHORT).show();
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

        enterLeave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Enterleave();
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
        myRef2 = database.getReference("Managers/Leaves/"+rollno);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                textView.setText("Welcome, " + student.name + " !");
                tableLayout.removeAllViews();
                for(int i=student.Leaves.size()-1 ; i>=0; i--){
                    Button b = new Button(getApplicationContext());
                    b.setText("from : "+dateFormat.format(student.Leaves.get(i).from)+" to : "+
                    dateFormat.format(student.Leaves.get(i).to));
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
                                student.Leaves.remove(-view.getId());
                                myRef.child("Leaves/").setValue(student.Leaves);
                                if(student.Leaves.size()==0){
                                    myRef2.removeValue();
                                }
                                else{
                                    myRef2.setValue(student.Leaves.size());
                                }
                            }
                            return false;
                        }
                    });
                    tr.addView(b);
                    tableLayout.addView(tr);
                }
                Log.i("xyxyx", "Enterleave: " + student.Leaves.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void From(View view){
        if(calendarView.getVisibility() == View.INVISIBLE){
            calendarView.setVisibility(View.VISIBLE);
            col1.setVisibility(View.INVISIBLE);
            col2.setVisibility(View.INVISIBLE);
            from.setText("OK");
        }
        else{
            from_date = cal_date;
            Log.i("xyxyx", "To: " + dateFormat.format(from_date));
            calendarView.setVisibility(View.INVISIBLE);
            col1.setVisibility(View.VISIBLE);
            col2.setVisibility(View.VISIBLE);
            from.setText("From");
        }
    }

    public void To(View view){
        if(calendarView.getVisibility() == View.INVISIBLE){
            calendarView.setVisibility(View.VISIBLE);
            col1.setVisibility(View.INVISIBLE);
            col2.setVisibility(View.INVISIBLE);
            to.setText("OK");
        }
        else{
            to_date = cal_date;
            Log.i("xyxyx", "To: " + dateFormat.format(to_date));
            calendarView.setVisibility(View.INVISIBLE);
            col1.setVisibility(View.VISIBLE);
            col2.setVisibility(View.VISIBLE);
            to.setText("To");
        }
    }


    public void Enterleave() {
        if(from_date != null && to_date != null){
            Date date = new Date();
            String formattedDate= dateFormat.format(date);
            Log.i("xyxyx","Current time of the day using Date - 12 hour format: " + formattedDate);

            Leave newleave = new Leave();
            newleave.date = date;
            newleave.from = from_date;
            newleave.to = to_date;
            newleave.rollno = rollno;

            myRef.child("Leaves").child(String.valueOf(student.Leaves.size())).setValue(newleave);
            student.Leaves.add(newleave);
            myRef2.setValue(student.Leaves.size());

            from_date = null;
            to_date = null;
        }
        else{
            Toast toast = Toast.makeText(this, "message", Toast.LENGTH_SHORT);
            toast.setText("Please Enter Dates");
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
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

