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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context ctx = MemberList.this;
        ListView mbrList = findViewById(R.id.mbrList);
        final itemList query = new itemList(ctx);
        mbrList.setAdapter(new MemberAdapter(ctx, (ArrayList<Member>)new Main.ListService() {
            @Override
            public List<?> perfome() {
                return query.execute();
            }
        }.perfome()));
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx, MemberDetail.class));
            }
        });

    }

    private class ListQuery extends Main.QueryFactory{
    SQLiteOpenHelper helper;
        public ListQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }
        @Override
        public SQLiteDatabase getDatabase() {return helper.getReadableDatabase();}
    }

    private class itemList extends ListQuery{

        public itemList(Context ctx) {super(ctx);}

        public ArrayList<Member> execute(){
            ArrayList<Member> ls = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(" SELECT * FROM MEMBER ", null);
            // Cursor -> ResultSet 과 같음 list 받을때
            Member m = null;
            if(c!=null){
                while (c.moveToNext()){
                    m = new Member();
                    m.setSeq(Integer.parseInt(c.getString(c.getColumnIndex(DBInfo.MBR_SEQ))));
                    m.setName(c.getString(c.getColumnIndex(DBInfo.MBR_NAME)));
                    m.setPass(c.getString(c.getColumnIndex(DBInfo.MBR_PASS)));
                    m.setEmail(c.getString(c.getColumnIndex(DBInfo.MBR_EMAIL)));
                    m.setAddr(c.getString(c.getColumnIndex(DBInfo.MBR_ADDR)));
                    m.setPhone(c.getString(c.getColumnIndex(DBInfo.MBR_PHONE)));
                    m.setPhoto(c.getString(c.getColumnIndex(DBInfo.MBR_PHOTO)));
                    ls.add(m);
                    Log.d("등록된 회원수는 ",ls.size()+"");
                }
            }else{Log.d("등록된 회원은 ","없음");}
            return ls;
        }

    }

    // 아이템 관련 파트
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Member> ls;
        Context ctx;
        LayoutInflater inflater;
        public MemberAdapter(Context ctx, ArrayList<Member> ls) {
            this.ctx = ctx;
            this.ls = ls;
            this.inflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return ls.size();
        }

        @Override
        public Object getItem(int i) {
            return ls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v=inflater.inflate(R.layout.mbr_item,null);
                holder = new ViewHolder();
//                holder.photo = v.findViewById(R.id.photo);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else{
                holder = (ViewHolder)v.getTag();
            }
            holder.name.setText(ls.get(i).getName());
            holder.phone.setText(ls.get(i).getPhone());
            // 포토 불러오는 코드
            return v;
        }
    }
    static class ViewHolder{
        ImageView photo;
        TextView name, phone;
    }
    private class PhotoQuery extends Main.QueryFactory{
        Main.SqliteHelper helper;
        public PhotoQuery(Context ctx) {
            super(ctx);
            helper = new Main.SqliteHelper(ctx);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class PhotoExist extends PhotoQuery{
        String seq;
        public PhotoExist(Context ctx) {
            super(ctx);
        }
        public Object execute(){
            return getDatabase().rawQuery(String.format(" SELECT %s FROM %s WHERE %s=%s ",
                    DBInfo.MBR_PHOTO,DBInfo.MBR_TABLE,DBInfo.MBR_SEQ,seq),null)
                    .moveToNext();
        }
    }

}
