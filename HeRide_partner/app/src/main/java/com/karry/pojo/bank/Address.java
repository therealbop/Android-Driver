package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable
{
  @SerializedName("city")
  @Expose
  private String city;

  public String getCity() { return this.city; }

  public void setCity(String city) { this.city = city; }

  @SerializedName("country")
  @Expose
  private String country;

  public String getCountry() { return this.country; }

  public void setCountry(String country) { this.country = country; }

  @SerializedName("line1")
  @Expose
  private String line1;

  public String getLine1() { return this.line1; }

  public void setLine1(String line1) { this.line1 = line1; }

  @SerializedName("postal_code")
  @Expose
  private String postal_code;

  public String getPostalCode() { return this.postal_code; }

  public void setPostalCode(String postal_code) { this.postal_code = postal_code; }

  @SerializedName("state")
  @Expose
  private String state;

  public String getState() { return this.state; }

  public void setState(String state) { this.state = state; }
}