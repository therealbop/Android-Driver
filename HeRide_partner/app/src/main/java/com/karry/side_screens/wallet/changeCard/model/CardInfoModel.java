package com.karry.side_screens.wallet.changeCard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CardInfoModel implements Serializable {

	@SerializedName("card_image")
	@Expose
	private String card_image;

	@SerializedName("card_numb")
	@Expose
	private String card_numb;

	@SerializedName("exp_month")
	@Expose
	private String exp_month;

	@SerializedName("exp_year")
	@Expose
	private String exp_year;

	@SerializedName("card_id")
	@Expose
	private String card_id;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("funding")
	@Expose
	private String funding;

	@SerializedName("brand")
	@Expose
	private String brand;

	@SerializedName("defaultCard")
	@Expose
	private boolean defaultCard;

	public String getCard_image() {
		return card_image;
	}

	public CardInfoModel(String card_image, String card_numb,
                         String exp_month, String exp_year, String id, boolean defaultCard, String name,
                         String funding, String brand) {
		super();
		this.card_image = card_image;
		this.card_numb = card_numb;
		this.exp_month = exp_month;
		this.exp_year = exp_year;
		this.card_id=id;
		this.defaultCard = defaultCard;
		this.name = name;
		this.funding = funding;
		this.brand = brand;
	}

	public String getCard_numb() {
		return card_numb;
	}
	public String getExp_month() {
		return exp_month;
	}
	public String getExp_year() {
		return exp_year;
	}
	public String getCard_id() {
		return card_id;
	}
	public boolean getDefaultCard() {
		return defaultCard;
	}
	public String getName() {
		return name;
	}
	public String getFunding() {
		return funding;
	}

	public String getBrand() {
		return brand;
	}

	/*@Override
	public String toString() {
		return "CardInfoModel{" +
				"card_image='" + card_image + '\'' +
				", card_numb='" + card_numb + '\'' +
				", exp_month='" + exp_month + '\'' +
				", exp_year='" + exp_year + '\'' +
				", card_id='" + card_id + '\'' +
				", name='" + name + '\'' +
				", funding='" + funding + '\'' +
				", defaultCard=" + defaultCard +
				'}';
	}*/
}
