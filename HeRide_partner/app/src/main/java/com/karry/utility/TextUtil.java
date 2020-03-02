package com.karry.utility;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>TextUtil</h1>
 * This class is used to check for texts related checks
 *@author  3Embed
 * @since on 23-11-2017.
 */

public class TextUtil
{
    /**
     * <h2>isEmpty</h2>
     * This method is used to check whether the text is empty
     * @param string The string to be validated
     * @return returns true if its empty else false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.replace(" ", "").length() == 0;
    }

    /**
     * <h2>emailValidation</h2>
     * <p>
     *     method to validate the input email format
     * </p>
     * @param email: input email
     * @return: flag: true if is valid
     */
    public static boolean emailValidation(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidUser(String userName)
    {
        return  Patterns.EMAIL_ADDRESS.matcher(userName).matches() || Patterns.PHONE.matcher(userName).matches();
    }

    /**
     * <h2>phoneNumberLengthValidation</h2>
     * This method is used to validate the length of the phone number
     * @param phoneNumber phone number to be validated
     * @param minLength minimum length according to country
     * @param maxLength maximum length according to country
     * @return returns true if valid else false
     */
    public static boolean phoneNumberLengthValidation(String phoneNumber, int minLength,
                                                      int maxLength)
    {
        int phoneLength = phoneNumber.length();
        Utility.printLog("the phone len : "+phoneNumber+"\t"+minLength+"\t"+maxLength);
        return phoneLength == minLength || phoneLength == maxLength;
    }
}
