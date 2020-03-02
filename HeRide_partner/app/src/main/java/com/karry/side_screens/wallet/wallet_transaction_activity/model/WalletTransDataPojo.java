package com.karry.side_screens.wallet.wallet_transaction_activity.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletTransDataPojo implements Serializable {

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"WalletTransDataPojo{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}