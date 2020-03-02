package com.karry.app.meterBooking.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>ResultGoogleAddressModel</h1>
 * used to hold the address model
 * @author embed
 * @since on 27/3/17.
 */
public class ResultGoogleAddressModel implements Serializable
{
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("geometry")
    @Expose
    private GeometryGoogleModel geometry;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("types")
    @Expose
    private String[] types;

    public String getIcon ()
    {
        return icon;
    }
    public void setIcon (String icon)
    {
        this.icon = icon;
    }
    public String getUrl ()
    {
        return url;
    }
    public void setUrl (String url)
    {
        this.url = url;
    }
    public String getReference ()
    {
        return reference;
    }
    public void setReference (String reference)
    {
        this.reference = reference;
    }
    public GeometryGoogleModel getGeometry ()
    {
        return geometry;
    }
    public String getId ()
    {
        return id;
    }
    public void setId (String id)
    {
        this.id = id;
    }
    public String getName ()
    {
        return name;
    }
    public void setName (String name)
    {
        this.name = name;
    }
    public String[] getTypes ()
    {
        return types;
    }
    public void setTypes (String[] types)
    {
        this.types = types;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [icon = "+icon+", url = "+url+", reference = "+reference+", geometry = "+geometry+", id = "+id+", photos = "+", address_components = "+", name = "+name+", types = "+types+"]";
    }
}


