package com.karry.countrypic.countrypic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import com.karry.countrypic.Constants;
import com.karry.countrypic.Country;
import com.karry.countrypic.CountryListAdapter;
import com.karry.countrypic.CountryPickerListener;
import com.heride.partner.R;
import com.karry.utility.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * <h>CountryPicker</h>
 * <p>Class to show an alert dialog which display
 * country code with flags to choose country code with </p>
 * @since  6/9/16.
 *
 */
public class CountryPicker extends DialogFragment implements Comparator<Country> {

    private static final String TAG = "CountryPicker";
    private EditText searchEditText;
    private ListView countryListView;
    private CountryListAdapter adapter;
    private List<Country> allCountriesList;
    private List<Country> selectedCountriesList;
    private CountryPickerListener listener;
    private Context context;

    public void setListener(com.karry.countrypic.CountryPickerListener listener) {
        this.listener = listener;
    }

    @Inject
    public CountryPicker() {
    }

    private List<Country> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<>();
                String allCountriesCode = readEncodedJsonString();
                JSONArray countryArray = new JSONArray(allCountriesCode);
                for (int i = 0; i < countryArray.length(); i++) {
                    JSONObject jsonObject = countryArray.getJSONObject(i);
                    String countryName = jsonObject.getString("name");
                    String countryDialCode = jsonObject.getString("dial_code");
                    String countryCode = jsonObject.getString("code");
                    int maxPhoneLength=jsonObject.getInt("max_digits");
                    int minPhoneLength=(!jsonObject.getString("min_digits").isEmpty())?jsonObject.getInt("min_digits"):5;
                    Country country = new Country();
                    country.setCode(countryCode);
                    country.setName(countryName);
                    country.setDialCode(countryDialCode);
                    country.setMinDigits(minPhoneLength);
                    country.setMaxDigits(maxPhoneLength);
                    allCountriesList.add(country);
                    Utility.printLog(TAG+" country codes ");
                }
                Collections.sort(allCountriesList, this);
                selectedCountriesList = new ArrayList<Country>();
                selectedCountriesList.addAll(allCountriesList);
                return allCountriesList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String readEncodedJsonString() throws java.io.IOException {
        byte[] data = Base64.decode(Constants.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    /**
     * To support show as dialog
     */
    public static CountryPicker newInstance(String dialogTitle)
    {
        CountryPicker picker = new CountryPicker();
        Bundle bundle = new Bundle();
        bundle.putString("dialogTitle", dialogTitle);
        picker.setArguments(bundle);
        return picker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_country_picker, null);
        Bundle args = getArguments();
        if (args != null) {
            String dialogTitle = args.getString("dialogTitle");
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(R.dimen.dimen_300dp);
            int height = getResources().getDimensionPixelSize(R.dimen.dimen_300dp);
            getDialog().getWindow().setLayout(width, height);
        }
        getAllCountries();
        searchEditText = (EditText) view.findViewById(R.id.country_code_picker_search);
        countryListView = (ListView) view.findViewById(R.id.country_code_picker_listview);

        adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    Country country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country.getName(), country.getCode(), country.getDialCode(),
                            country.getFlag(),country.getMinDigits(), country.getMaxDigits());
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        return view;
    }

    @SuppressLint("DefaultLocale") private void search(String text) {
        selectedCountriesList.clear();
        for (Country country : allCountriesList) {
            if (country.getName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }

    public Country getUserCountryInfo(Context context) {
        this.context = context;
        getAllCountries();
        String countryIsoCode;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            countryIsoCode = telephonyManager.getNetworkCountryIso();
            for (int i = 0; i < allCountriesList.size(); i++) {
                Country country = allCountriesList.get(i);
                if (country.getCode().equalsIgnoreCase(countryIsoCode)) {
                    country.setFlag(getFlagResId(country.getCode()));
                    return country;
                }
            }
        return afghanistan();
    }

    private Country afghanistan() {
        Country country = new Country();
        country.setCode("IN");
        country.setName("India");
        country.setDialCode("+91");
        country.setFlag(R.drawable.flag_in);
        country.setMaxDigits(10);
        country.setMinDigits(10);
        return country;
    }

    private int getFlagResId(String drawable) {
        try {
            return context.getResources()
                    .getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
