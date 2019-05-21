package com.example.dell.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {

    private DBHelper dbHelper;
    private String TBNAME;
    public RateManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();   //可写数据库
        ContentValues values = new ContentValues();
        values.put("curname", item.getCurName());
        values.put("currate", item.getCurRate());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void addAll(List<RateItem> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (RateItem item : list) {
            ContentValues values = new ContentValues();
            values.put("curname", item.getCurName());
            values.put("currate", item.getCurRate());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }
    public List<RateItem> listAll(){    //显示所有
        List<RateItem> rateList = null;    //定义返回数据 列表
        SQLiteDatabase db = dbHelper.getReadableDatabase();    //只读数据库
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){   //光标
            rateList = new ArrayList<RateItem>();
            while(cursor.moveToNext()){   //下一行
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));    //光标取数据 根据“id”判断
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
            cursor.close();  //关闭光标
        }
        db.close();  //关闭数据库
        return rateList;   //返回列表对象
    }

    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void update(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("curname", item.getCurName());
        values.put("currate", item.getCurRate());
        db.update(TBNAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }
    public RateItem findById(int id){    //查询
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null,
                null, null);
        RateItem rateItem = null;
        if(cursor!=null && cursor.moveToFirst()){
            rateItem = new RateItem();
            rateItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            rateItem.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            rateItem.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }
        db.close();
        return rateItem;
    }

}
