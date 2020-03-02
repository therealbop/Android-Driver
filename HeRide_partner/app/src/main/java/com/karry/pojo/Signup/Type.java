package com.karry.pojo.Signup;

import com.karry.pojo.VehTypeSepecialities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 15/5/17.
 */

public class Type implements Serializable {

    private String Name;

    private String id;

    private boolean selected;
    private ArrayList<VehTypeSepecialities> sepecialities;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<VehTypeSepecialities> getSepecialities() {
        return sepecialities;
    }

    public void setSepecialities(ArrayList<VehTypeSepecialities> sepecialities) {
        this.sepecialities = sepecialities;
    }

}
