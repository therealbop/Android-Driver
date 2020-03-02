package com.karry.data.source.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.karry.bookingFlow.model.AddressDataModel;
import com.karry.bookingFlow.model.FavAddressDataModel;
import com.karry.utility.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.karry.data.source.sqlite.SQLiteDbHelper.DROP_ADDRESS_AREA;
/*import static com.flagit.data.source.sqlite.SQLiteDbHelper.DROP_ADDRESS_KEY_ID;*/
import static com.karry.data.source.sqlite.SQLiteDbHelper.DROP_ADDRESS_LAT;
import static com.karry.data.source.sqlite.SQLiteDbHelper.DROP_ADDRESS_LONG;
import static com.karry.data.source.sqlite.SQLiteDbHelper.DROP_ADDRESS_TABLE;
import static com.karry.data.source.sqlite.SQLiteDbHelper.DROP_PLACE_ID;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_AREA;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_KEY_ID;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LAT;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_LONG;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_NAME;
import static com.karry.data.source.sqlite.SQLitePersistenceContract.FavAddressEntry.FAV_DROP_ADRS_TABLE;


/**
 * <h1>SQLiteLocalDataSource</h1>
 * This method is used to do the address database operations
 * @author 3Embed
 * @since on 18-01-2018.
 */
public class SQLiteLocalDataSource implements SQLiteDataSource
{

    private static final String TAG = "SQLiteLocalDataSource";
    private SQLiteDbHelper SQLiteDbHelper;

    @Inject
    SQLiteLocalDataSource(SQLiteDbHelper SQLiteDbHelper)
    {
        this.SQLiteDbHelper = SQLiteDbHelper;
    }

    @Override
    public ArrayList<FavAddressDataModel> getFavAddresses()
    {
        Utility.printLog(TAG+ "getFavAddresses called() ");
        ArrayList<FavAddressDataModel> favAddressDataModels = new ArrayList<>();
        SQLiteDatabase db = SQLiteDbHelper.getReadableDatabase();

        String[] projection =
                {
                        FAV_DROP_ADRS_KEY_ID,
                        FAV_DROP_ADRS_NAME,
                        FAV_DROP_ADRS_AREA,
                        FAV_DROP_ADRS_LAT,
                        FAV_DROP_ADRS_LONG
                };

        Cursor mCursor = db.query(
                FAV_DROP_ADRS_TABLE, projection,
                null, null, null, null, null);

        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst())
        {
            do
            {
                FavAddressDataModel favDropAdrsData = new FavAddressDataModel();
                favDropAdrsData.setAddressId(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_KEY_ID)));
                favDropAdrsData.setName(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_NAME)));
                favDropAdrsData.setAddress(mCursor.getString(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_AREA)));
                favDropAdrsData.setLatitude(mCursor.getDouble(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_LAT)));
                favDropAdrsData.setLongitude(mCursor.getDouble(mCursor.getColumnIndexOrThrow(FAV_DROP_ADRS_LONG)));
                favAddressDataModels.add(favDropAdrsData);
            }
            while (mCursor.moveToNext());
            mCursor.close();
        }

        db.close();
        Utility.printLog(TAG+ "getFavAddresses size(): " + favAddressDataModels.size());
        return favAddressDataModels;
    }

    @Override
    public long insertFavAddress(FavAddressDataModel favAddressDataModel)
    {
        Utility.printLog(TAG+ "insertFavDropAdrssData favDropAdrsData: "+ favAddressDataModel.toString());
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_DROP_ADRS_KEY_ID, favAddressDataModel.getAddressId());
        contentValues.put(FAV_DROP_ADRS_NAME, favAddressDataModel.getName());
        contentValues.put(FAV_DROP_ADRS_AREA, favAddressDataModel.getAddress());
        contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favAddressDataModel.getLatitude()));
        contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favAddressDataModel.getLongitude()));

        long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
        Utility.printLog(TAG+ "insertFavDropAdrssData id: "+id);
        db.close();
        return id;
    }

    @Override
    public void insertAllFavAddresses(ArrayList<FavAddressDataModel> favAddressDataModels)
    {
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
        SQLiteDbHelper.createFavAddressTable(db);

        for(FavAddressDataModel favAddressDataModel: favAddressDataModels)
        {
            Utility.printLog(TAG+ "insertAllFavAddresses size: "+favAddressDataModel.getAddressId()
            +" "+favAddressDataModel.get_id());
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAV_DROP_ADRS_KEY_ID, favAddressDataModel.get_id());
            contentValues.put(FAV_DROP_ADRS_NAME, favAddressDataModel.getName());
            contentValues.put(FAV_DROP_ADRS_AREA, favAddressDataModel.getAddress());
            contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favAddressDataModel.getLatitude()));
            contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favAddressDataModel.getLongitude()));

            long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
            Utility.printLog(TAG+" insertFavDropAdrssData id: " + id);
        }
    }

    @Override
    public ArrayList<AddressDataModel> extractAllNonFavAddresses()
    {
        ArrayList<AddressDataModel> addressDataModels = new ArrayList<>();
        SQLiteDatabase db = SQLiteDbHelper.getReadableDatabase();
        String[] projection =
                {
                        /*DROP_ADDRESS_KEY_ID,*/
                        DROP_ADDRESS_AREA,
                        DROP_ADDRESS_LAT,
                        DROP_ADDRESS_LONG,
                        DROP_PLACE_ID
                };

        Cursor mCursor = db.query(
                DROP_ADDRESS_TABLE, projection,
                null, null, null, null, null);

        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst())
        {
            do {
                AddressDataModel addressDataModel = new AddressDataModel();
                /*addressDataModel.setAddressId(mCursor.getInt(mCursor.getColumnIndex(DROP_ADDRESS_KEY_ID)));*/
                addressDataModel.setAddress(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_AREA)));
                addressDataModel.setLat(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LAT)));
                addressDataModel.setLng(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LONG)));
                addressDataModel.setPaceId(mCursor.getString(mCursor.getColumnIndex(DROP_PLACE_ID)));

                Utility.printLog("DB extractFrmAdrsTable: latitude: " + addressDataModel.getLat() + " ,logitude: " + addressDataModel.getLng()+" ,address: " + addressDataModel.getAddress()+" ,addressid: " + addressDataModel.getAddressId());
                addressDataModels.add(addressDataModel);
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        Utility.printLog("DB extractAllFrmAdrsTable rcntlyVisitedAL.size(): " + addressDataModels.size());
        return addressDataModels;
    }

    @Override
    public long insertNonFavAddressData(String area, String lat, String log,String placeID)
    {
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DROP_ADDRESS_AREA, area);
        contentValues.put(DROP_ADDRESS_LAT, lat);
        contentValues.put(DROP_ADDRESS_LONG, log);
        contentValues.put(DROP_PLACE_ID, placeID);
        long id = db.insert(DROP_ADDRESS_TABLE, null, contentValues);
        Utility.printLog(TAG+" INSERTED ROW NUMBER IS :InsertDropData: " + id );
        return id;
    }

    @Override
    public int deleteFavAddress(String id)
    {
        Utility.printLog(TAG+ "deleteFavDropAdrs id: "+id);
        SQLiteDatabase db = SQLiteDbHelper.getReadableDatabase();
        return db.delete(FAV_DROP_ADRS_TABLE,FAV_DROP_ADRS_KEY_ID + "=? ",new String[]{id});
    }

    @Override
    public void deleteRecentAddressTable()
    {
        SQLiteDatabase db = SQLiteDbHelper.getWritableDatabase();
        db.delete(DROP_ADDRESS_TABLE,null,null);
        Utility.printLog("DataBase Is CLeared" );
    }
}
