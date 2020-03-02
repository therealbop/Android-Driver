package com.karry.pojo;

import com.karry.pojo.TripsPojo.Result_Google_Pojo;

/**
 * Created by embed on 27/3/17.
 */

public class DropLocationGooglePojo
{
    private Result_Google_Pojo result;

    private String[] html_attributions;

    private String status;

    public Result_Google_Pojo getResult ()
    {
        return result;
    }

    public void setResult (Result_Google_Pojo result)
    {
        this.result = result;
    }

    public String[] getHtml_attributions ()
    {
        return html_attributions;
    }

    public void setHtml_attributions (String[] html_attributions)
    {
        this.html_attributions = html_attributions;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", html_attributions = "+html_attributions+", status = "+status+"]";
    }
}
