package com.example.demo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText Username, Password;
    Button Login, Signup;
    FirebaseDatabase database;
    DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckValid()) {
                    DangNhap();
                }
            }
        });
    }

    private void AnhXa() {
        Username = findViewById(R.id.login_editText_username);
        Password = findViewById(R.id.login_editText_password);
        Login = findViewById(R.id.login_button_login);
        Signup = findViewById(R.id.login_button_signup);
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference("User");
    }

    private void SignUpActivity() {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }

    private boolean CheckValid() {
        boolean flag = true;
        EditText e[] = {Username, Password};
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

    private void DangNhap() {
        String username = Username.getText().toString();
        String password = Password.getText().toString();

        user_ref.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Query query = user_ref.orderByChild("password").equalTo(password);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user : snapshot.getChildren()) {
                                User u = user.getValue(User.class);
                                if (password.equals(u.getPassword())) {
                                    Username.setError(null);
                                    Toast.makeText(LoginActivity.this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }
                            Username.setError("Sai tai khoan hoac mat khau");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Username.setError("Sai tai khoan hoac mat khau");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}