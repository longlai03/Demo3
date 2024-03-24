package com.example.demo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    EditText Name, Username, Password, Date;
    Button Signup;
    FirebaseDatabase database;
    DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AnhXa();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckValid()) {
                    DangKy();
                }
            }
        });
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
            }
        });
    }

    private void AnhXa() {
        Name = findViewById(R.id.signup_editText_name);
        Username = findViewById(R.id.signup_editText_username);
        Password = findViewById(R.id.signup_editText_password);
        Date = findViewById(R.id.signup_editText_date);
        Signup = findViewById(R.id.signup_button_signup);
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference("User");
    }

    private void DangKy() {
        String name = Name.getText().toString();
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        String date = Date.getText().toString();

        user_ref.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    User user = new User(name, username, password, date);
                    user_ref.child(username).setValue(user);
                    Toast.makeText(SignupActivity.this, "Dang ky thanh cong", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Username.setError("Tai khoan nay da co san");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean CheckValid() {
        boolean flag = true;
        EditText e[] = {Name, Username, Password, Date};
        for (int i = 0; i < e.length; i++) {
            if (isEmpty(e[i])) {
                e[i].setError("Khong duoc bo trong!");
                flag = false;
            }
        }
        return flag;
    }
    private boolean isEmpty(EditText e) {
        return TextUtils.isEmpty(e.getText().toString());
    }
    private void DatePicker(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date.setText(dayOfMonth +"/"+ (month+1) + "/" + year);
            }
        }, day, month, year);
        datePickerDialog.show();
    }


}