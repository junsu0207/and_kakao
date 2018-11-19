package com.example.a405_2.kakao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Member;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        final Context ctx = Login.this;

        final EditText etID = findViewById(R.id.etID);
        final EditText etPass = findViewById(R.id.etPass);

        findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etID.getText().toString();
                String pass = etPass.getText().toString();
                Toast.makeText(ctx,"로그인성공\n"+"아이디 : "+id+"\n비밀번호 : "+pass,Toast.LENGTH_LONG).show();
                startActivity(new Intent(ctx, MemberList.class));
            }
        });
        findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
