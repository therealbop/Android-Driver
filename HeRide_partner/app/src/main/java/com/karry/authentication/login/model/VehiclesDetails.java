package com.karry.authentication.login.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karry.pojo.Signup.Services;

import java.io.Serializable;
import java.util.ArrayList;


public class VehiclesDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("selected")
    @Expose
    private boolean selected;

    @SerializedName("vehicleMapIcon")
    @Expose
    private String vehicleMapIcon;

    @SerializedName("services")
    @Expose
    private ArrayList<Services> services;

    @SerializedName("vehicleMake")
    @Expose
    private String vehicleMake;

    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("vehicleModel")
    @Expose
    private String vehicleModel;

    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    @SerializedName("goodTypes")
    @Expose
    private String[] goodTypes;

    @SerializedName("vehicleImage")
    @Expose
    private String vehicleImage;

    @SerializedName("typeId")
    @Expose
    private String typeId;

    @SerializedName("plateNo")
    @Expose
    private String plateNo;

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getVehicleMapIcon ()
    {
        return vehicleMapIcon;
    }

    public void setVehicleMapIcon (String vehicleMapIcon)
    {
        this.vehicleMapIcon = vehicleMapIcon;
    }



    public String getVehicleMake ()
    {
        return vehicleMake;
    }

    public void setVehicleMake (String vehicleMake)
    {
        this.vehicleMake = vehicleMake;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getVehicleModel ()
    {
        return vehicleModel;
    }

    public void setVehicleModel (String vehicleModel)
    {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType ()
    {
        return vehicleType;
    }

    public void setVehicleType (String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String[] getGoodTypes ()
    {
        return goodTypes;
    }

    public void setGoodTypes (String[] goodTypes)
    {
        this.goodTypes = goodTypes;
    }

    public String getVehicleImage ()
    {
        return vehicleImage;
    }

    public void setVehicleImage (String vehicleImage)
    {
        this.vehicleImage = vehicleImage;
    }

    public String getTypeId ()
    {
        return typeId;
    }

    public void setTypeId (String typeId)
    {
        this.typeId = typeId;
    }

    public String getPlateNo ()
    {
        return plateNo;
    }

    public void setPlateNo (String plateNo)
    {
        this.plateNo = plateNo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", vehicleMapIcon = "+vehicleMapIcon+", services = "+services+", vehicleMake = "+vehicleMake+", _id = "+_id+", vehicleModel = "+vehicleModel+", vehicleType = "+vehicleType+", goodTypes = "+goodTypes+", vehicleImage = "+vehicleImage+", typeId = "+typeId+", plateNo = "+plateNo+"]";
    }

}
