package com.example.a405_2.kakao;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context ctx = Main.this;

        findViewById(R.id.moveLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(ctx,Login.class);
                startActivity(intent);
                // 가독성 높이기 위해 밑에 줄로 변경
                */
                startActivity(new Intent(ctx,Login.class));
                // intent 이동하는 객체 new 로 생성후 여기 ctx 에서 가고자 하는 곳으로 class로 이동
            }
        });




    }

    static interface ExecuteService{
        public void porfome();
    }
    static  interface ListService{
        public List<?> perfome();
    }
    static interface ObjectService{
        public Object perfome();
    }
    static abstract class QueryFactory{ //추상팩토리 패턴
        Context ctx;
        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }
        public abstract SQLiteDatabase getDatabase();
    }
    static class SqliteHelper extends SQLiteOpenHelper{

        public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DBInfo.DBNAME, null
                    , 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(
                    " CREATE TABLE IF NOT EXISTS %s " +
                    " ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "   %s TEXT,  " +
                    "   %s TEXT,  " +
                    "   %s TEXT,  " +
                    "   %s TEXT,  " +
                    "   %s TEXT,  " +
                    "   %s TEXT  " +
                    ") ",
                    DBInfo.MBR_TABLE,
                    DBInfo.MBR_SEQ,
                    DBInfo.MBR_NAME,
                    DBInfo.MBR_EMAIL,
                    DBInfo.MBR_PASS,
                    DBInfo.MBR_ADDR,
                    DBInfo.MBR_PHONE,
                    DBInfo.MBR_PHOTO
            );
            Log.d("실행할 쿼리 ::", sql);
            db.execSQL(sql);
            Log.d("================================", "쿼리실행");
            String[] names = {"전지현","수지","원빈","장동건","박보영"};
            String[] emails = {"jjh@jjh.com","sj@sj.com","ob@ob.com","jdk@jdk.com","pby@pby.com"};
            String[] addrs = {"강남구","분당","종로구","서초구","판교"};
            for(int i =0; i<names.length; i++){
                Log.d("입력하는 이름 :: ", names[i]);
                db.execSQL(String.format(
                        " INSERT INTO %s " +
                        " ( %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s ," +
                        "   %s " +
                        ")VALUES( "+
                        "'%s',"  +
                        "'%s',"  +
                        "'%s',"  +
                        "'%s',"  +
                        "'%s',"  +
                        "'%s' "  +
                        ")",
                        DBInfo.MBR_TABLE,
                        DBInfo.MBR_NAME,DBInfo.MBR_EMAIL,DBInfo.MBR_PASS,
                        DBInfo.MBR_ADDR,DBInfo.MBR_PHONE,DBInfo.MBR_PHOTO,
                        names[i],emails[i],"1",addrs[i],"010-1234-547"+i,"PHOTO_"+(i+1)
                ));
            }
            Log.d("********************************", "친구목록완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
