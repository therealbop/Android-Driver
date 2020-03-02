package com.karry.data.source.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.karry.utility.Utility;


import javax.inject.Inject;

import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_KEY_ID;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_TABLE;

/**
 * <h1>SQLiteDbHelper</h1>
 * This class is used for defining database and creating columns
 * @version v1.0
 */
public class SQLiteDbHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DatabaseHelper";            // Logcat tag
    private static final int DATABASE_VERSION = 1;                // Database Version
    private static final String DATABASE_NAME = "shyper_DB";    // Database Name
    private static final String TEXT_TYPE = " VARCHAR";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES_FOR_FAV = "CREATE TABLE IF NOT EXISTS " +
            FAV_DROP_ADRS_TABLE + "(" +
            FAV_DROP_ADRS_KEY_ID + TEXT_TYPE + COMMA_SEP +
            SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_NAME+ TEXT_TYPE + COMMA_SEP +
            SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_AREA + TEXT_TYPE + COMMA_SEP +
            SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LAT + TEXT_TYPE + COMMA_SEP +
            SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LONG + TEXT_TYPE +
            ")";

    static final String DROP_ADDRESS_TABLE = "Drop_Address_Table";         // New Add Drop Address Table, created on 17/03/17
    static final String DROP_ADDRESS_KEY_ID = "Drop_Address_KeyId";        // Common column names
    static final String DROP_ADDRESS_AREA = "Drop_Address_Area";
    static final String DROP_ADDRESS_LAT = "Drop_Address_Latitude";
    static final String DROP_ADDRESS_LONG = "Drop_Address_Longitude";
    static final String DROP_PLACE_ID = "Drop_place_ID";

    private static final String CREATE_DROP_ADDRESS_TABLE = "CREATE TABLE " + DROP_ADDRESS_TABLE + "(" + DROP_PLACE_ID + " VARCHAR PRIMARY KEY," +
            DROP_ADDRESS_AREA + " VARCHAR," + DROP_ADDRESS_LAT + " VARCHAR," + DROP_ADDRESS_LONG + " VARCHAR" /*+ DROP_PLACE_ID + " VARCHAR" */+ ")";


    /**
     * <h2>SQLiteDbHelper</h2>
     * This is constructor of this class
     * @param mContext  context from which this class is called
     */
    @Inject
    SQLiteDbHelper(Context mContext)
    {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Utility.printLog(TAG+"onCreate database ");
        createFavAddressTable(db);
        db.execSQL(CREATE_DROP_ADDRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_DROP_ADRS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        onUpgrade(db, 1, 2);
    }

    /**
     * <h2>createFavAddressTable</h2>
     * This method is used to create the fav address table
     * @param db database object
     */
    void createFavAddressTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_DROP_ADRS_TABLE);
        db.execSQL(SQL_CREATE_ENTRIES_FOR_FAV);
    }
}
