package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonalAddress implements Serializable
{
  @SerializedName("country")
  @Expose
  private String country;

  public String getCountry() { return this.country; }

  public void setCountry(String country) { this.country = country; }

}