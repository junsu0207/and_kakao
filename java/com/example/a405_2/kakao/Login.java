package com.example.a405_2.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
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
                // Validation 유효성 체크
                if(etID.getText().length()!=0 && etPass.getText().length()!=0){
                    String id = etID.getText().toString();
                    String pass = etPass.getText().toString();
                    final itemExist query = new itemExist(ctx);
                    query.id = id;
                    query.pw = pass;
                    new Main.ExecuteService(){
                       @Override
                       public void perfome() {
                           if(query.execute()){
                               startActivity(new Intent(ctx, MemberList.class));
                           }else{
                               startActivity(new Intent(ctx, Login.class));
                           }
                       }
                   }.perfome();
                }else{
                    startActivity(new Intent(ctx, Login.class));
                }

            }
        });
        findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private class LoginQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public LoginQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }

    }
    private class itemExist extends LoginQuery{
        String id, pw;

        public itemExist(Context ctx) {
            super(ctx);
        }
        public boolean execute(){
            return super.getDatabase().rawQuery(String.format("SELECT * FROM %s" +
                    " WHERE %s LIKE '%s' AND %s LIKE '%s'",
                    DBInfo.MBR_TABLE,DBInfo.MBR_SEQ,id,DBInfo.MBR_PASS, pw),null)
                    .moveToNext();
        }

    }



}
