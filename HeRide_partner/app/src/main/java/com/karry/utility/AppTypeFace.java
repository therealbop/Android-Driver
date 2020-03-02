package com.karry.utility;

import android.content.Context;
import android.graphics.Typeface;

import javax.inject.Inject;

/**************************************************************************************************/
/**
 * <h2>AppTypeface</h2>
 * This class contains several methods that are used for setting and getting methods for typeFace.
 */

public class AppTypeFace {

    private static AppTypeFace setTypeface = null;
    private Typeface pro_narMedium;
    private Typeface pro_News;
    private Typeface SFAutomaton;
    private Typeface OrbitronBold;
    private Typeface OrbitronMedium;
    private Typeface OrbitronRegular;
    private Typeface ClanaproNarrBook;

    /**
     * <h2>AppTypeface</h2>
     * @param context: calling activity reference
     * @return: Single instance of this class
     */
    public static AppTypeFace getInstance(Context context)
    {
        if (setTypeface == null)
        {
            setTypeface = new AppTypeFace(context.getApplicationContext());
        }
        return setTypeface;
    }


    @Inject
    public AppTypeFace(Context context) {
        initTypefaces(context);
    }

    /**
     * <h2>initTypefaces</h2>
     * <p>method to initializes the typefaces of the app</p>
     * @param context Context of the activity from where it is called
     */
    private void initTypefaces(Context context)
    {
        this.pro_narMedium = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrMedium.otf");
        this.pro_News = Typeface.createFromAsset(context.getAssets(),"fonts/ClanPro-NarrNews.otf");
        this.SFAutomaton=Typeface.createFromAsset(context.getAssets(),"fonts/SF Automaton.ttf");
        this.OrbitronBold=Typeface.createFromAsset(context.getAssets(),"fonts/Orbitron-Bold.ttf");
        this.OrbitronMedium=Typeface.createFromAsset(context.getAssets(),"fonts/Orbitron-Medium.ttf");
        this.OrbitronRegular=Typeface.createFromAsset(context.getAssets(),"fonts/Orbitron-Regular.ttf");
        this.ClanaproNarrBook=Typeface.createFromAsset(context.getAssets(),"fonts/CLANPRO-NARRBOOK.OTF");
    }

    /**********************************************************************************************/
    public Typeface getPro_narMedium() {
        return pro_narMedium;
    }

    public Typeface getClanaproNarrBook() {
        return ClanaproNarrBook;
    }

    public Typeface getPro_News() {
        return pro_News;
    }

    public Typeface getSFAutomaton() {
        return SFAutomaton;
    }

    public Typeface getOrbitronBold() {
        return OrbitronBold;
    }

    public Typeface getOrbitronMedium() {
        return OrbitronMedium;
    }

    public Typeface getOrbitronRegular() {
        return OrbitronRegular;
    }
}
