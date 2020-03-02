package com.karry.manager.booking;

import com.google.gson.Gson;
import com.karry.bookingFlow.model.RideBookingCancel;
import com.karry.bookingFlow.model.RideBookingDropUpdate;
import com.karry.mqttChat.ChatData;
import com.karry.mqttChat.ChatDataObervable;
import com.karry.pojo.QueuePosition;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>BookingManager</h1>
 * <p></p>
 * @author : 3embed
 * @since : 5/4/18
 */

public class BookingManager {

    private Gson gson;
    private RxDriverCancelledObserver rxDriverCancelledObserver;
    private RxDropLocationUpdateObserver rxDropLocationUpdateObserver;
    private static RxDriverQueuePosition rxDriverQueuePosition;
    private ChatDataObervable chatDataObervable;

    public BookingManager(Gson gson, RxDriverCancelledObserver rxDriverCancelledObserver,
                          RxDropLocationUpdateObserver rxDropLocationUpdateObserver,
                          ChatDataObervable chatDataObervable,RxDriverQueuePosition rxDriverQueuePosition) {

        this.gson = gson;
        this.rxDriverCancelledObserver = rxDriverCancelledObserver;
        this.rxDropLocationUpdateObserver = rxDropLocationUpdateObserver;
        this.rxDriverQueuePosition = rxDriverQueuePosition;
        this.chatDataObervable = chatDataObervable;
    }



    /**
     * <h2>handleBookingsStatus</h2>
     * This method is used to handle the user bookings details
     * @param bookingDetails booking details to be handled
     */
    public void handleBookingsStatus(String bookingDetails)
    {
        try
        {
            JSONObject object = new JSONObject(bookingDetails);
            if(object.has("status"))
            {
                switch (object.getInt("status"))
                {
                    case 2:
                        //update drop location from passenger
                        RideBookingDropUpdate rideBookingDropUpdate = gson.fromJson(bookingDetails,
                                RideBookingDropUpdate.class);
                        rxDropLocationUpdateObserver.publishDropUpdate(rideBookingDropUpdate);
                        break;
                    case 4:
                    case 5:
                    case 12:
                        //passenger cancel the booking
                        RideBookingCancel driverDetailsModel = gson.fromJson(bookingDetails,
                                RideBookingCancel.class);
                        rxDriverCancelledObserver.publishDriverCancelDetails(driverDetailsModel);
                        break;

                    case 16:
//                        JSONObject data = object.getJSONObject("data");
                        ChatData rideChat = gson.fromJson(object.toString(),ChatData.class);
                        ChatDataObervable.getInstance().emitData(rideChat);
                        break;

                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void queuePosition(int queuePosition, int queueLength){

        try {
            QueuePosition queuePositionData = new QueuePosition();
            queuePositionData.setQueueLength(queueLength);
            queuePositionData.setQueuePosition(queuePosition);

            rxDriverQueuePosition.publishDriverQueuePositon(queuePositionData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
