package com.karry.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by ads on 11/05/17.
 */

public class CustomTextWatcher implements TextWatcher {

    private EditText emailPhone;
    private EditText password;
    private DisableError disableError;

    public CustomTextWatcher(EditText emailPhone, EditText password, DisableError disableError) {
        this.emailPhone = emailPhone;
        this.password = password;
        this.disableError = disableError;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        if (emailPhone.length() > 0) {
            disableError.setDisableError(emailPhone);
        }

        if (password.length() > 0) {
            disableError.setDisableError(password);
        }

        if (emailPhone.length() > 0 && password.length() > 0) {
            disableError.enableSighUp();
        } else {
            disableError.disableSighUp();
        }

    }
}
