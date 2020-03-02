package com.karry.mqttChat;

import android.content.Intent;
import java.util.ArrayList;

/**
 * Created by DELL on 28-03-2018.
 */

public interface ChattingContract {

        interface ViewOperations
        {
                void setActionBar(String custName);

                void setViews(String bid);

                void setRecyclerView();

                void updateData(ArrayList<ChatData> chatDataArry);

                void showProgress();
                void hideProgress();
                void networkError(String message);
        }

        interface PresenterOperations {

                void getData(Intent intent);

                void setActionBar();

                void initViews();

                void getChattingData(int pageNo);

                void message(String message);
        }
}


