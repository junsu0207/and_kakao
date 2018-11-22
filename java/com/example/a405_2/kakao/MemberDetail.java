package com.example.a405_2.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context ctx = MemberDetail.this;
            // 선택한 멤버정보
            Intent intent = this.getIntent();
            String seq = intent.getExtras().getString("seq");

            final DetailList query = new DetailList(ctx);
            query.seq = seq;
            Member m = (Member) new Main.ObjectService() {
                @Override
                public Object perfome() {
                    return query.execute();
                }
             }.perfome();


        // 선택한 멤버 로그출력
        Log.d("선택한 멤버 정보", m.toString());
        final String spec = m.seq+"/"+m.addr+"/"+m.email+"/"+m.name+"/"+m.phone+"/"+m.photo+"/"+m.pass;
        // 토큰으로 끊어서 꺼내기
//        String[] arr = spec.split("/");
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MemberUpdate.class);
                intent.putExtra("spec",spec);
                startActivity(intent);
            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberList.class));
            }
        });
    }

    private class DetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public DetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }
        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }

    private class DetailList extends DetailQuery{
        String seq;
        public DetailList(Context ctx) {super(ctx);}

        public Member execute(){
            Member m = null;
            Cursor c = this.getDatabase().rawQuery(String.format(" SELECT * FROM %s" +
                            " WHERE %s LIKE '%s'",DBInfo.MBR_TABLE,DBInfo.MBR_SEQ,seq),null);
            if (c != null) {
                if(c.moveToNext()) {
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    Log.d("검색된 회원은 ",m.getName());
                }
            } else {
                Log.d("검색된 회원은 ", "없음");
            }
           return m;
        }
    }

    private class MemberAdapter extends BaseAdapter{
        Member m;
        Context ctx;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, Member m) {
            this.ctx = ctx;
            this.m = m;
            this.inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {return 1;}

        @Override
        public Object getItem(int i) {return i;}

        @Override
        public long getItemId(int i) {return i;}

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v == null){
                v = inflater.inflate(R.layout.member_detail, null);
                holder = new ViewHolder();
                holder.photo = v.findViewById(R.id.photo);
                holder.name = findViewById(R.id.name);
                holder.email = findViewById(R.id.email);
                holder.phone = findViewById(R.id.phone);
                holder.addr = findViewById(R.id.addr);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.name.setText(m.name);
            holder.email.setText(m.email);
            holder.phone.setText(m.phone);
            holder.addr.setText(m.addr);
            // 포토 불러오는 코드
            final ItemPhoto query = new ItemPhoto(ctx);
            query.seq = m.seq+"";
            String s = ((String) new Main.ObjectService() {
                @Override
                public Object perfome() {
                    return query.execute();
                }
            }.perfome()).toLowerCase();
            Log.d("파일명:",s);
            holder.photo.setImageDrawable(getResources().getDrawable(
                    getResources().getIdentifier(ctx.getPackageName()+":drawable/"+s,
                            null,null),ctx.getTheme() ));
            return v;
        }
    }

    static class ViewHolder{
        ImageView photo;
        TextView name,email,phone,addr;
    }

    private class PhotoQuery extends Main.QueryFactory{
        Main.SqliteHelper helper;
        public PhotoQuery(Context ctx) {
            super(ctx);
            helper =new Main.SqliteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemPhoto extends PhotoQuery {
        String seq;
        public ItemPhoto(Context ctx) {
            super(ctx);
        }
        public String execute(){
            Cursor c= getDatabase()
                    .rawQuery(String.format(
                            " SELECT %s FROM %s WHERE %s LIKE '%s' ",
                            DBInfo.MBR_PHOTO,
                            DBInfo.MBR_TABLE,
                            DBInfo.MBR_SEQ,
                            seq
                    ),null);
            String result = "";
            if(c!= null){
                if(c.moveToNext()){
                    result = c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO));
                }
            }
            return result;
        }
    }


}
