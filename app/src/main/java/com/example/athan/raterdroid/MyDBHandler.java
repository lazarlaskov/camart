package com.example.athan.raterdroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by athan on 27.8.16.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CamartDB";
    private static final String TABLE_LOGIN = "logincred";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SESKEY = "seskey";
    private static final String[] COLUMNS = {COLUMN_ID,COLUMN_SESKEY};

    public MyDBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE logincred ( id TEXT, seskey TEXT )";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS logincred");
        this.onCreate(sqLiteDatabase);
    }

    public void addLogin(LoginInformation loginInformation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", loginInformation.get_id());
        values.put("seskey", loginInformation.get_skey());
        db.insert("logincred", null, values);
        db.close();
    }

    public LoginInformation getLoginInformation(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("logincred",COLUMNS,null,null,null,null,null,null);

        if(c != null){
            c.moveToFirst();
        }

        String username = "_NEMA_";
        String seskey = "_NEMA_";

        if(!c.isAfterLast()) {
            username = c.getString(0);
            seskey = c.getString(1);
        }
            LoginInformation li = new LoginInformation(username,seskey);

        return li;
    }

    public void deleteLogin(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM logincred");
    }

}
