package com.karry.side_screens.wallet.wallet_transaction_activity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable {

	@SerializedName("transctions")
	private ArrayList<TransctionsItem> transctions;

	@SerializedName("totalCount")
	private int totalCount;

	public void setTransctions(ArrayList<TransctionsItem> transctions){
		this.transctions = transctions;
	}

	public ArrayList<TransctionsItem> getTransctions(){
		return transctions;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"transctions = '" + transctions + '\'' + 
			",totalCount = '" + totalCount + '\'' + 
			"}";
		}
}