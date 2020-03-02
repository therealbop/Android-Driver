package com.karry.pojo;

/**
 * Created by ads on 11/05/17.
 */

public class SigninDriverVehicle {
    private boolean selected;
    private String id;
    private String typeId;
    private String vehicleModel;
    private String plateNo;
    private String operator;
    private String vehicleType;
    private String[] goodTypes;

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String[] getGoodTypes() {
        return goodTypes;
    }

    public void setGoodTypes(String[] goodTypes) {
        this.goodTypes = goodTypes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + "\"" + id + "\"" +
                ", \"typeId\":" + "\"" + typeId + "\"" +
                ", \"vehicleModel\":" + "\"" + vehicleModel + "\"" +
                ", \"plateNo\":" + "\"" + plateNo + "\"" +
                ", \"vehicleType\":" + "\"" + vehicleType + "\"" +
                ", \"operator\":" + "\"" + operator + "\"" +
                "}";
    }
}
