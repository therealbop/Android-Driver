package com.karry.data.source.sqlite;



import com.karry.bookingFlow.model.AddressDataModel;
import com.karry.bookingFlow.model.FavAddressDataModel;

import java.util.ArrayList;

/**
 * <h1>SQLiteDataSource</h1>
 * @author by 3Embed
 * @since on 18-01-2018.
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface SQLiteDataSource
{
    /**
     * <h2><getFavAddresses</h2>
     * extract all favorite address stored
     * @return list of fav address
     */
    ArrayList<FavAddressDataModel> getFavAddresses();

    /**
     * Only store newly added fav address.
     * @param favAddressDataModel favAddressDataModel: to be inserted into database
     * @return fav address data model id
     */
    long insertFavAddress(FavAddressDataModel favAddressDataModel);

    /**
     * <h2>insertAllFavAddresses</h2>
     * This method is used for storing all fav address
     * @param favAddressDataModels list of fav address
     */
    void insertAllFavAddresses(ArrayList<FavAddressDataModel> favAddressDataModels);

    /**
     * <h2>extractAllNonFavAddresses</h2>
     * extract all recent addresses stored
     * @return list of address
     */
    ArrayList<AddressDataModel> extractAllNonFavAddresses();

    /**
     * Only store the recent address name and lat-longs
     * @param area drop address area
     * @param lat drop address latitude
     * @param log drop address longitude
     * @return returns the database id
     */
    long insertNonFavAddressData(String area, String lat, String log, String placeID) ;

    /**
     * <h2>deleteFavAddress</h2>
     * This method is used to delete the fav address from database
     * @param id id of address to be deleted
     * @return the number of rows affected if a whereClause is passed in, 0
     *         otherwise. To remove all rows and get a count pass "1" as the
     *         whereClause.
     */
    int deleteFavAddress(String id);

    /**
     * <h2>deleteRecentAddressTable</h2>
     * This method is used to delete the recent address table
     */
    void deleteRecentAddressTable();
}
