package com.example.recitewords.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DateBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DateBaseHelper(@Nullable Context context) {
        super(context, Constants.DATEBASE_NAME, null, Constants.VERSION_CODE);
    }

    //创建数据库(这只会在程序第一次运行时生效，db文件会被储存在移动设备中，下次运行不会重复创建)
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "创建数据库...");
        //创建数据库中的字段，这里只创建了一个name字段和一个password字段
        String sql = "create table " + Constants.TABLE_NAME + "(name varchar(20),password varchar(20))";
        db.execSQL(sql);
    }

    //实现增
    public void insert(SQLiteDatabase sqLiteDatabase, String name, String password) {
        //声明键值对values
        ContentValues values = new ContentValues();
        //分别插入名为name和password的值到键值对values的"name"和"password"字段中
        values.put("name", name);
        values.put("password", password);
        //将键值对values插入到数据库中
        sqLiteDatabase.insert(Constants.TABLE_NAME, null, values);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级数据库时的回调（升级前需要更改版本号）
        Log.d(TAG, "升级数据库...");
    }
}