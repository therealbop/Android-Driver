package com.karry.pojo.bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Verification implements Serializable
{
  @SerializedName("document")
  @Expose
  private String document;

  public String getDocument() { return this.document; }

  public void setDocument(String document) { this.document = document; }

  @SerializedName("status")
  @Expose
  private String status;

  public String getStatus() { return this.status; }

  public void setStatus(String status) { this.status = status; }
}