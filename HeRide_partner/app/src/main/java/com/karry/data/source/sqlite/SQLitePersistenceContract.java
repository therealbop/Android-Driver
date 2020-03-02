package com.karry.data.source.sqlite;

import android.provider.BaseColumns;

/**
 * <h1>SQLitePersistenceContract</h1>
 * The contract used for the db to save the fav address locally.
 */
final class SQLitePersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private SQLitePersistenceContract() {}

    /* Inner class that defines the table contents */
    static abstract class FavAddressEntry implements BaseColumns
    {
        static final String FAV_DROP_ADRS_TABLE = "Fav_Drop_Adrs_Table";         // New Add Drop Address Table, created on 17/03/17
        static final String FAV_DROP_ADRS_KEY_ID = "Fav_Drop_Adrs_KeyId";        // Common column names
        static final String FAV_DROP_ADRS_NAME = "Fav_Drop_Adrs_Name";
        static final String FAV_DROP_ADRS_AREA = "Fav_Drop_Adrs_Area";
        static final String FAV_DROP_ADRS_LAT = "Fav_Drop_Adrs_Lat";
        static final String FAV_DROP_ADRS_LONG = "Fav_Drop_Adrs_Long";
    }
}