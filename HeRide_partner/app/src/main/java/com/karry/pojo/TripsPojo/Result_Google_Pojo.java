package com.karry.pojo.TripsPojo;

import com.karry.pojo.GeometryGooglePojo;

/**
 * Created by embed on 27/3/17.
 */

public class Result_Google_Pojo
{
    private String icon;

    private String place_id;

    private String scope;

    private String adr_address;

    private String url;

    private String reference;

    private GeometryGooglePojo geometry;

    private String utc_offset;

    private String id;

    private String vicinity;

    private String name;

    private String formatted_address;

    private String[] types;

    public String getIcon ()
    {
        return icon;
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String getScope ()
    {
        return scope;
    }

    public void setScope (String scope)
    {
        this.scope = scope;
    }

    public String getAdr_address ()
    {
        return adr_address;
    }

    public void setAdr_address (String adr_address)
    {
        this.adr_address = adr_address;
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

    public GeometryGooglePojo getGeometry ()
    {
        return geometry;
    }

    public void setGeometry (GeometryGooglePojo geometry)
    {
        this.geometry = geometry;
    }

    public String getUtc_offset ()
    {
        return utc_offset;
    }

    public void setUtc_offset (String utc_offset)
    {
        this.utc_offset = utc_offset;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }


    public String getVicinity ()
    {
        return vicinity;
    }

    public void setVicinity (String vicinity)
    {
        this.vicinity = vicinity;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getFormatted_address ()
    {
        return formatted_address;
    }

    public void setFormatted_address (String formatted_address)
    {
        this.formatted_address = formatted_address;
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
        return "ClassPojo [icon = "+icon+", place_id = "+place_id+", scope = "+scope+", adr_address = "+adr_address+", url = "+url+", reference = "+reference+", geometry = "+geometry+", utc_offset = "+utc_offset+", id = "+id+", photos = "+", vicinity = "+vicinity+", address_components = "+", name = "+name+", formatted_address = "+formatted_address+", types = "+types+"]";
    }
}


