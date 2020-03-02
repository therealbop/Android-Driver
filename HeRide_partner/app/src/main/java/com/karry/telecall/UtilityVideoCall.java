package com.karry.telecall;

import java.util.ArrayList;

/**
 * Created by Ali on 1/7/2019.
 */
public class UtilityVideoCall
{

    private boolean callMinimized = false;
    private ArrayList<String> colors;
    private boolean activeOnACall = false;
    private boolean firstTimeAfterCallMinimized = false;
    private int activeActivitiesCount;


    public static UtilityVideoCall utilityVideoCall;
    private String activeCallId, activeCallerId;


    public static UtilityVideoCall getInstance()
    {
        if(utilityVideoCall == null)
        {
            utilityVideoCall = new UtilityVideoCall();
            return utilityVideoCall;
        }else
            return utilityVideoCall;
    }

    public String getActiveCallId() {
        return activeCallId;
    }

    private UtilityVideoCall()
    {
        if(colors==null)
            setBackgroundColorArray();

    }

    private void setBackgroundColorArray() {

        colors = new ArrayList<>();

        colors.add("#FFCDD2");
        colors.add("#D1C4E9");
        colors.add("#B3E5FC");
        colors.add("#C8E6C9");
        colors.add("#FFF9C4");
        colors.add("#FFCCBC");
        colors.add("#CFD8DC");
        colors.add("#F8BBD0");
        colors.add("#C5CAE9");
        colors.add("#B2EBF2");
        colors.add("#DCEDC8");
        colors.add("#FFECB3");
        colors.add("#D7CCC8");
        colors.add("#F5F5F5");
        colors.add("#FFE0B2");
        colors.add("#F0F4C3");
        colors.add("#B2DFDB");
        colors.add("#BBDEFB");
        colors.add("#E1BEE7");


    }

    public String getColorCode(int position) {
        return colors.get(position);

    }

    public boolean isCallMinimized() {
        return callMinimized;
    }

    public void setCallMinimized(boolean callMinimized) {
        this.callMinimized = callMinimized;


    }

    public boolean isActiveOnACall() {
        return activeOnACall;
    }

    public void setActiveOnACall(boolean activeOnACall, boolean notCallCut) {
        this.activeOnACall = activeOnACall;


        if (!activeOnACall && notCallCut) {

            this.callMinimized = false;

        }
    }

    public void setActiveCallId(String callId)
    {
        activeCallId = callId;
    }


    public void setActiveCallerId(String activeCallerId) {

        this.activeCallerId = activeCallerId;
    }

    public boolean isFirstTimeAfterCallMinimized() {
        return firstTimeAfterCallMinimized;
    }

    public void setFirstTimeAfterCallMinimized(boolean firstTimeAfterCallMinimized) {
        this.firstTimeAfterCallMinimized = firstTimeAfterCallMinimized;
    }

    public int getActiveActivitiesCount() {
        return activeActivitiesCount;
    }



//    /**
//     * To find the document id of the receiver on receipt of new message,if exists or create a new document for chat with that receiver and return its document id
//     */
//    @SuppressWarnings("unchecked")
//    public static String findDocumentIdOfReceiver(String receiverUid, String timestamp, String receiverName,
//                                                  String receiverImage, String secretId, boolean invited,
//                                                  String receiverIdentifier, String chatId, boolean groupChat)
//
//    {
//
//        CouchDbController db = AppController.getInstance().getDbController();
//        Map<String, Object> chatDetails = db.getAllChatDetails(AppController.getInstance().getChatDocId());
//
//        if (chatDetails != null) {
//
//            ArrayList<String> receiverUidArray = (ArrayList<String>) chatDetails.get("receiverUidArray");
//
//
//            ArrayList<String> receiverDocIdArray = (ArrayList<String>) chatDetails.get("receiverDocIdArray");
//
//
//            ArrayList<String> secretIdArray = (ArrayList<String>) chatDetails.get("secretIdArray");
//
//            for (int i = 0; i < receiverUidArray.size(); i++) {
//
//
//                if (receiverUidArray.get(i).equals(receiverUid) && secretIdArray.get(i).equals(secretId)) {
//
//                    return receiverDocIdArray.get(i);
//
//                }
//
//            }
//        }
//
//
//        /*  here we also need to enter receiver name*/
//
//
//        String docId = db.createDocumentForChat(timestamp, receiverUid, receiverName, receiverImage, secretId, invited,
//                receiverIdentifier, chatId, groupChat);
//
//
//        db.addChatDocumentDetails(receiverUid, docId, AppController.getInstance().getChatDocId(), secretId);
//
//        return docId;
//    }
}
