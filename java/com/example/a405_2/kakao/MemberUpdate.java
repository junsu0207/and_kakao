package com.example.a405_2.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context ctx = MemberUpdate.this;
        final String[] arr = this.getIntent().getStringExtra("spec").split("/");
//        m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.phone+"/"+m.photo+"/"+m.pass
        final EditText name = findViewById(R.id.name);
        name.setHint(arr[3]);
        final EditText email = findViewById(R.id.email);
        email.setHint(arr[2]);
        final EditText phone = findViewById(R.id.phone);
        phone.setHint(arr[4]);
        final EditText addr = findViewById(R.id.addr);
        addr.setHint(arr[1]);
        final EditText pass = findViewById(R.id.pass);
        pass.setHint(arr[6]);
        final ImageView photo = findViewById(R.id.photo);
        photo.setImageDrawable(
                getResources().getDrawable(getResources().getIdentifier(
                        this.getPackageName()+":drawable/"+arr[5].toLowerCase(),null,null
                ), ctx.getTheme())
        );
        // 수정 이후
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member();
                final ItemUpdate query = new ItemUpdate(ctx);
              /*  String name2="";
                if(name.getText().equals("")){
                    name2 = arr[3];
                }else{
                    name2 = name.getText().toString();
                }member.setName(name2);
                */
                member.setAddr((addr.getText().equals(""))?arr[1]:addr.getText().toString());
                member.setEmail((email.getText().equals(""))?arr[2]:email.getText().toString());
                member.setName((name.getText().equals(""))?arr[3]:name.getText().toString());
                member.setPhone((phone.getText().equals(""))?arr[4]:phone.getText().toString());
                //member.setPhoto((photo.getText().equals(""))?arr[5]:photo.getText().toString());
                member.setPass((pass.getText().equals(""))?arr[6]:pass.getText().toString());
                query.member = member;
                new Main.ExecuteService() {
                    @Override
                    public void perfome() {
                        query.execute();
                    }
                }.perfome();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberDetail.class));
            }
        });

    }
    private class UpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public UpdateQuery(Context ctx) {
            super(ctx);
            this.helper = new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends UpdateQuery{
        Member member;
        public ItemUpdate(Context ctx) {
            super(ctx);
            member = new Member();
            // 인스턴스 변수는 반드시 생성자 내부에서 초기화 한다
            // 로직은 반드시 에어리어 내부에서 이루어 진다.
            // 에어리어 내부는 CPU를 뜻한다.
            // 필드는 RAM영역을 뜻한다.
        }
        //        m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.phone+"/"+m.photo+"/"+m.pass
        public void execute(){
            String sql = String.format(" UPDATE %s SET " +
                    "%s = '%s',"+
                    "%s = '%s',"+
                    "%s = '%s',"+
                    "%s = '%s' " +
                    "WHERE %s LIKE '%s' ",DBInfo.MBR_TABLE,
                    DBInfo.MBR_ADDR, member.addr,
                    DBInfo.MBR_EMAIL, member.email,
                    DBInfo.MBR_NAME, member.name,
                    DBInfo.MBR_PASS, member.pass,
                    DBInfo.MBR_PHOTO, member.photo,
                    DBInfo.MBR_PHONE, member.phone);
            Log.d(" SQL :::: ",sql);
            getDatabase().execSQL(sql);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
    }



}
