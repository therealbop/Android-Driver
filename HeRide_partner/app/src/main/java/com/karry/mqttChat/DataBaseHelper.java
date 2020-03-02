package com.karry.mqttChat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * <h>DataBaseHelper</h>
 * Created by Ali on 12/22/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME= "ChatDB";

    /*DataBase for the Chat */
    private static final String TABLE_NAME = "chatResult";
    //Coulomn name
    private static final String KEY_ID = "proId";
    private static final String CHAT_CONTENT = "chat_content";
    private static final String CHAT_FORM_ID = "chat_fromID";
    private static final String CHAT_TAGET_ID = "chat_targetId";
    private static final String CHAT_TIMESTAMP = "chat_timestamp";
    private static final String CHAT_BID = "chat_bid";
    private static final String CHAT_TYPE = "chat_type";
    private static final String CHAT_CUST_PRO_TYPE = "chat_cust_pro_type";


    /*DataBase for the searchAddres */
    private static final String TABLE_NAME_ADDRESS = "Address";
    //Coulomn name
    private static final String KEY = "id";
    private static final String ADDRESS_NAME = "address_name";
    private static final String ADDRESS_FORMATED = "formated_address";
    private static final String ADDRESS_LAT = "address_lat";
    private static final String ADDRESS_LNG = "address_lng";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + CHAT_CONTENT + " TEXT," + CHAT_FORM_ID + " TEXT," + CHAT_TAGET_ID + " TEXT," + CHAT_TIMESTAMP + " INTEGER," + CHAT_BID + " INTEGER," + CHAT_TYPE + " INTEGER," + CHAT_CUST_PRO_TYPE + " INTEGER" + ")";//REAL


        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_NAME_ADDRESS + "(" + KEY + " INTEGER PRIMARY KEY," + ADDRESS_NAME + " TEXT," + ADDRESS_FORMATED + " TEXT," + ADDRESS_LAT + " REAL," +
                ADDRESS_LNG + " REAL" + ")";

        sqLiteDatabase.execSQL(CREATE_CHAT_TABLE);
        sqLiteDatabase.execSQL(CREATE_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ADDRESS);
        onCreate(sqLiteDatabase);
    }

    public void addNewChat(String chat_content, String chat_fromID, String chat_targetId, long chat_timestamp,int chat_type,int chat_cust_proType,long bid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHAT_CONTENT, chat_content);
        contentValues.put(CHAT_FORM_ID, chat_fromID);
        contentValues.put(CHAT_TAGET_ID, chat_targetId);
        contentValues.put(CHAT_TIMESTAMP, chat_timestamp);
        contentValues.put(CHAT_BID, bid);
        contentValues.put(CHAT_TYPE, chat_type);
        contentValues.put(CHAT_CUST_PRO_TYPE, chat_cust_proType);

        db.insert(TABLE_NAME,null,contentValues);
        db.close();
    }

    public ArrayList<ChatData> fetchData(String proId,String custId,String bid)
    {
        SQLiteDatabase db=getWritableDatabase();
     //   String no=null;
        ArrayList<ChatData> dbSavedChats = new ArrayList<>();

        String[] columns={CHAT_CONTENT,CHAT_FORM_ID,CHAT_TAGET_ID,CHAT_TIMESTAMP,CHAT_BID,CHAT_TYPE,CHAT_CUST_PRO_TYPE};
        String[] nm={proId,custId,bid};
        Cursor cursor = db.query(TABLE_NAME,columns,CHAT_TAGET_ID +"=?" +" AND " + CHAT_FORM_ID +"=?" +" AND " + CHAT_BID +"=?",nm,null,null,null);
        if(cursor.moveToFirst())
        {
            do
            {
                ChatData dbSavedChat = new ChatData();
                dbSavedChat.setContent(cursor.getString(0));
                dbSavedChat.setFromID(cursor.getString(1));
                dbSavedChat.setTargetId(cursor.getString(2));
                dbSavedChat.setTimestamp(cursor.getString(3));
                dbSavedChat.setBid(cursor.getLong(4));
                dbSavedChat.setType(cursor.getInt(5));
                dbSavedChat.setCustProType(cursor.getInt(6));


                dbSavedChats.add(dbSavedChat);
            }while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return dbSavedChats;
    }

    public void deleteChat(long bid)
    {

        SQLiteDatabase db=getWritableDatabase();
        System.out.println("Comment deleted with id: " + bid);
        db.delete(TABLE_NAME, CHAT_TAGET_ID
                + " = " + bid, null);

        db.close();

    }
}
