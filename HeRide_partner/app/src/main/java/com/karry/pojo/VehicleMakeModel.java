package com.karry.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by embed on 16/5/17.
 */

public class VehicleMakeModel implements Serializable, Parcelable {

    public static final Creator<VehicleMakeModel> CREATOR = new Creator<VehicleMakeModel>() {
        @Override
        public VehicleMakeModel createFromParcel(Parcel in) {
            return new VehicleMakeModel(in);
        }

        @Override
        public VehicleMakeModel[] newArray(int size) {
            return new VehicleMakeModel[size];
        }
    };
    private String _id;
    private String Name;
    private boolean selected;

    protected VehicleMakeModel(Parcel in) {
        _id = in.readString();
        Name = in.readString();
        selected = in.readByte() != 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(Name);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
