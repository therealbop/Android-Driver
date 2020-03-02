package com.karry.service;

import android.content.Context;
import android.util.Log;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.karry.utility.SessionManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;

/** Created by DELL on 31-08-2017. */
public class CouchDbHandler {

  private static final String TAG = "CouchDB ";
  private Manager manager;
  private AndroidContext androidContext;
  private Database database;
  private SessionManager sessionManager;
  private String indexAppDocID;

  public CouchDbHandler(Context context) {
    this.androidContext = new AndroidContext(context);

    sessionManager = new SessionManager(context);
    mCreateManager();
    getDatabase();
    mGetDocument();
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////
  private void mCreateManager() {
    if (manager == null) {
      try {
        manager = new Manager(androidContext, Manager.DEFAULT_OPTIONS);
        Log.d(TAG, " mCreateManager ");
      } catch (IOException e) {
        Log.e(TAG, "Cannot create manager object");
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////
  private void getDatabase() {
    try {

      if (manager != null) {
        database = manager.getDatabase("couchdbdayrunner");
      }
      Log.d(TAG, "Database created");
      Log.d(TAG, " getDatabase ");

    } catch (CouchbaseLiteException e) {
      Log.e(TAG, "Cannot get database");
    }
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////

  private void mGetDocument() {
    Log.d(TAG, " mGetDocument ");
    indexAppDocID = sessionManager.getDocumentID();
    if (indexAppDocID.isEmpty()) {
      indexAppDocID = createAppDocumnent();
      sessionManager.setDocumentID(indexAppDocID);
      Log.d(TAG, " indexAppDocID ");
    }
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////

  private String createAppDocumnent() {
    Document latLngDoc = database.createDocument();
    Map<String, Object> mapLatLng = latLngDoc.getProperties();

    if (mapLatLng != null) {
      mapLatLng.clear();
    } else {
      mapLatLng = new HashMap<>();
    }

    mapLatLng.put("coordinates", new ArrayList<Map<String, Object>>());

    try {
      latLngDoc.putProperties(mapLatLng);
      return latLngDoc.getId();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  @SuppressWarnings("unchecked")
  public JSONArray retriveDocument() {
    JSONArray jsonArray = new JSONArray();

    Document retrievedDocument = database.getDocument(sessionManager.getDocumentID());

    if (retrievedDocument != null) {

      Map map = retrievedDocument.getProperties();
      ArrayList<Map<String, Object>> coordinates =
          (ArrayList<Map<String, Object>>) map.get("coordinates");
      if (coordinates != null) {
        Map<String, Object> coordinate;
        for (int i = 0; i < coordinates.size(); i++) {

          coordinate= coordinates.get(i);
          jsonArray.put(coordinate.get("latitude") + "," + coordinate.get("longitude") );
        }
      }
    }
    return jsonArray;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("unchecked")
  void updateDocument(double lat, double lng) {
    try {
      Document retrievedDocument = database.getDocument(sessionManager.getDocumentID());
      Map map = retrievedDocument.getProperties();
      if (map != null) {
        ArrayList<Map<String, Object>> coordinates =
            (ArrayList<Map<String, Object>>) map.get("coordinates");

        if (coordinates != null) {
          Map<String, Object> coordinate = new HashMap<>();
          coordinate.put("latitude", lat);
          coordinate.put("longitude", lng);
          coordinate.put("timestamp", System.currentTimeMillis());

          coordinates.add(coordinate);
          Map map1 = new HashMap(map);

          map1.put("coordinates", coordinates);

          retrievedDocument.putProperties(map1);
        }
      }

      // testPrintDistance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  public void deleteDocument() {
    Document retrievedDocument = database.getDocument(sessionManager.getDocumentID());

    Map map = retrievedDocument.getProperties();
    //ArrayList<Map<String,Object>> coordinates1 = (ArrayList<Map<String,Object>>) map.get("coordinates");
    //ArrayList longList = (ArrayList) map.get("longitude");
    //ArrayList timeStamp = (ArrayList) map.get("timeStamp");

    Map map1 = new HashMap(map);
    map1.put("coordinates", new ArrayList<HashMap<String,Object>>());
    //map1.put("longitude", longList);
    //map1.put("timeStamp", timeStamp);

    try {
      retrievedDocument.putProperties(map1);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
