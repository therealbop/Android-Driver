package com.karry.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karry.authentication.signup.GenericListActivity;
import com.heride.partner.R;
import com.karry.pojo.Signup.City;
import com.karry.pojo.Signup.ColorData;
import com.karry.pojo.Signup.Gender;
import com.karry.pojo.Signup.MakeData;
import com.karry.pojo.Signup.ModelData;
import com.karry.pojo.Signup.Services;
import com.karry.pojo.Signup.StateData;
import com.karry.pojo.Signup.TypeAndSpecialitiesData;
import com.karry.pojo.Signup.YearData;
import com.karry.pojo.SignupZonedata;
import com.karry.pojo.VehTypeSepecialities;
import com.karry.pojo.bookingAssigned.TowtrayServiceSelectData;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCountryList;
import com.karry.side_screens.bankDetails.pojoforBank.ConnectAccountCurrencyListSelection;

import java.util.ArrayList;

import static com.karry.utility.ServiceUrl.ZONE;
import static com.karry.utility.VariableConstant.CITY;
import static com.karry.utility.VariableConstant.COLOR;
import static com.karry.utility.VariableConstant.COUNTRY_SELECT;
import static com.karry.utility.VariableConstant.CURRENCY_SELECT;
import static com.karry.utility.VariableConstant.GENDER;
import static com.karry.utility.VariableConstant.STATE;
import static com.karry.utility.VariableConstant.VEHICLE_MAKE;
import static com.karry.utility.VariableConstant.VEHICLE_MODEL;
import static com.karry.utility.VariableConstant.VEHICLE_SERVICE;
import static com.karry.utility.VariableConstant.VEHICLE_SPECIALITIES;
import static com.karry.utility.VariableConstant.VEHICLE_TYPE;
import static com.karry.utility.VariableConstant.YEAR;


public class GenericListAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final Typeface ClanaproNarrNews;
    private ArrayList data;
    private Activity context;
    private String type;

    private ArrayList<SignupZonedata> signupZonedata;
    private ArrayList<City> cityData;
    private ArrayList<TypeAndSpecialitiesData> typeAndSpecialitiesData;
    private ArrayList<Services> services;
    private ArrayList<VehTypeSepecialities> vehTypeSepecialities;
    private ArrayList<MakeData> vehMakeDatas;
    private ArrayList<ModelData> vehicleMakeModels;
    private ArrayList<StateData> stateData;
    private ArrayList<YearData> yearData;
    private ArrayList<ColorData> colorData;
    private ArrayList<Gender> genderData;
    ArrayList<ConnectAccountCountryList> countryListforSelect;
    ArrayList<ConnectAccountCurrencyListSelection> currencyListforSelect;
    ArrayList<TowtrayServiceSelectData> towtrayServiceSelectData;
    private int lastCheckedPosition = 0;


    public GenericListAdapter(Activity context, ArrayList data, String type) {
        this.context = context;
        this.data = data;
        this.type = type;
        ClanaproNarrNews = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-NarrNews.otf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewsd = LayoutInflater.from(context).inflate(R.layout.single_select_operator, parent, false);
        return new MyViewHolder(viewsd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder viewHolder = (MyViewHolder) holder;

            switch (type) {

                case GENDER:
                    genderData = data;
                    viewHolder.operator.setText(genderData.get(position).getGender());
                    if (genderData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case COLOR:
                    colorData = data;
                    viewHolder.operator.setText(colorData.get(position).getColor());
                    if (colorData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case YEAR:
                    yearData = data;
                    viewHolder.operator.setText(yearData.get(position).getYear());
                    if (yearData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case STATE:
                    stateData = data;
                    viewHolder.operator.setText(stateData.get(position).getState());
                    if (stateData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case ZONE:
                    signupZonedata = data;
                    viewHolder.operator.setText(signupZonedata.get(position).getName());
                    if (signupZonedata.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case CITY:
                    cityData = data;
                    viewHolder.operator.setText(cityData.get(position).getCity());
                    if (cityData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case VEHICLE_TYPE:
                    typeAndSpecialitiesData = data;
                    viewHolder.operator.setText(typeAndSpecialitiesData.get(position).getName());
                    if (typeAndSpecialitiesData.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case VEHICLE_SERVICE:
                    services = data;
                    viewHolder.operator.setText(services.get(position).getServiceName());
                    if (services.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);

                    }
                    break;

                case VEHICLE_SPECIALITIES:

                    vehTypeSepecialities = data;
                    viewHolder.operator.setText(vehTypeSepecialities.get(position).getName());
                    if (vehTypeSepecialities.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case VEHICLE_MAKE:
                    vehMakeDatas = data;
                    viewHolder.operator.setText(vehMakeDatas.get(position).getMakeName());
                    if (vehMakeDatas.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case VEHICLE_MODEL:

                    vehicleMakeModels = data;
                    viewHolder.operator.setText(vehicleMakeModels.get(position).getModelName());
                    if (vehicleMakeModels.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

                case COUNTRY_SELECT:
                    countryListforSelect = data;
                    viewHolder.operator.setText(countryListforSelect.get(position).getCountry());
                    if (countryListforSelect.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;
                case CURRENCY_SELECT:
                    currencyListforSelect = data;
                    viewHolder.operator.setText(currencyListforSelect.get(position).getCurrencyID());
                    if (currencyListforSelect.get(position).isSelected()) {
                        viewHolder.operator.setChecked(true);
                    } else {
                        viewHolder.operator.setChecked(false);
                    }
                    break;

            }






            if(position == lastCheckedPosition && !type.equals(VEHICLE_SERVICE)) {
                viewHolder.operator.setChecked(true);
                setView(position, true);
            } else if (!type.equals(VEHICLE_SERVICE)) {
                viewHolder.operator.setChecked(false);
                setView(position, false);
            } else if (type.equals(VEHICLE_SERVICE)) {
                viewHolder.operator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setView(viewHolder.getAdapterPosition(),false);
                        notifyDataSetChanged();
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        switch (type) {

            case GENDER:
                ((GenericListActivity) context).sendResult(genderData, type);

            case COLOR:
                ((GenericListActivity) context).sendResult(colorData, type);

                break;
            case YEAR:
                ((GenericListActivity) context).sendResult(yearData, type);

                break;

            case STATE:
                ((GenericListActivity) context).sendResult(stateData, type);

                break;

            case ZONE:
                ((GenericListActivity) context).sendResult(signupZonedata, type);

                break;

            case CITY:
                ((GenericListActivity) context).sendResult(cityData, type);

                break;

            case VEHICLE_TYPE:
                ((GenericListActivity) context).sendResult(typeAndSpecialitiesData, type);

                break;

            case VEHICLE_SERVICE:
                ((GenericListActivity) context).sendResult(services, type);

                break;

            case VEHICLE_SPECIALITIES:
                ((GenericListActivity) context).sendResult(vehTypeSepecialities, type);
                break;

            case VEHICLE_MAKE:
                ((GenericListActivity) context).sendResult(vehMakeDatas, type);

                break;

            case VEHICLE_MODEL:
                ((GenericListActivity) context).sendResult(vehicleMakeModels, type);
                break;
            case COUNTRY_SELECT:
                ((GenericListActivity) context).sendResult(countryListforSelect, type);
                break;

            case CURRENCY_SELECT:
                ((GenericListActivity) context).sendResult(currencyListforSelect, type);
                break;
        }

    }

    public void setView(int position, boolean isSelected) {
        switch (type) {

            case GENDER:
                if (isSelected) {
                    genderData.get(position).setSelected(true);
                } else {
                    genderData.get(position).setSelected(false);
                }
                break;

            case COLOR:
                if (isSelected) {
                    colorData.get(position).setSelected(true);
                } else {
                    colorData.get(position).setSelected(false);
                }
                break;

            case YEAR:
                if (isSelected) {
                    yearData.get(position).setSelected(true);

                } else {
                    yearData.get(position).setSelected(false);
                }
                break;

            case STATE:
                if (isSelected) {
                    stateData.get(position).setSelected(true);
                } else {
                    stateData.get(position).setSelected(false);

                }
                break;

            case ZONE:
                if (signupZonedata.get(position).isSelected()) {
                    signupZonedata.get(position).setSelected(false);

                } else {
                    signupZonedata.get(position).setSelected(true);

                }
                break;

            case CITY:
                if (isSelected) {
                    cityData.get(position).setSelected(true);

                } else {
                    cityData.get(position).setSelected(false);
                }
                break;

            case VEHICLE_TYPE:
                if (isSelected) {
                    typeAndSpecialitiesData.get(position).setSelected(true);
                } else {

                    typeAndSpecialitiesData.get(position).setSelected(false);
                }

                break;

            case VEHICLE_SERVICE:
                if (services.get(position).isSelected()) {
                    services.get(position).setSelected(false);
                } else {
                    services.get(position).setSelected(true);
                    /*removeOtherSelectedViews(services, position);*/
                }

                break;

            case VEHICLE_SPECIALITIES:
                if (vehTypeSepecialities.get(position).isSelected()) {
                    vehTypeSepecialities.get(position).setSelected(false);
                } else {
                    vehTypeSepecialities.get(position).setSelected(true);
                }
                break;

            case VEHICLE_MAKE:
                if (isSelected) {
                    vehMakeDatas.get(position).setSelected(true);
                } else {
                    vehMakeDatas.get(position).setSelected(false);
                }

                break;

            case VEHICLE_MODEL:
                if (isSelected) {
                    vehicleMakeModels.get(position).setSelected(true);
                } else {
                    vehicleMakeModels.get(position).setSelected(false);
                }
                break;

            case COUNTRY_SELECT:
                if (isSelected) {
                    countryListforSelect.get(position).setSelected(true);
                } else {
                    countryListforSelect.get(position).setSelected(false);
                }
                break;

            case CURRENCY_SELECT:
                if (isSelected) {
                    currencyListforSelect.get(position).setSelected(true);
                } else {
                    currencyListforSelect.get(position).setSelected(false);
                }
                break;
        }
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatRadioButton operator;

        MyViewHolder(View itemView) {
            super(itemView);
            operator = itemView.findViewById(R.id.rb_single_operator);
            operator.setTypeface(ClanaproNarrNews);

            if(!type.equals(VEHICLE_SERVICE))
                operator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lastCheckedPosition = getAdapterPosition();

                        notifyDataSetChanged();

                    }
                });
        }


    }
}
