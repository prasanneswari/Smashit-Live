package com.vasmash.va_smash.login.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.ModelClass.Languages;

import java.util.ArrayList;


public class CountryCodeAdapter extends ArrayAdapter<Languages> {

    ArrayList<Languages> customers, tempCustomer, suggestions;
    public TextView countryName, code;

    public CountryCodeAdapter(Context context, ArrayList<Languages> objects) {
        super(context, R.layout.countrycodeadapter, R.id.country, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<Languages>(objects);
        this.suggestions = new ArrayList<Languages>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    private View initView(final int position, View convertView, ViewGroup parent) {
        Languages customer = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.countrycodeadapter, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.countrycodeadapter, parent, false);
        }
        countryName = (TextView) convertView.findViewById(R.id.country);
        code = convertView.findViewById(R.id.code);
        Log.d("code", "::::" + customers.get(position).getLang_name());

        countryName.setText(customer.getLang_code());
        code.setText(customer.getCallingcode());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Languages customer = (Languages) resultValue;
            return customer.getLang_code();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Languages cust : tempCustomer) {
                    if (cust.getLang_code().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(cust);
                    }else if (cust.getCallingcode().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(cust);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Languages> c = (ArrayList<Languages>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Languages cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };


}

