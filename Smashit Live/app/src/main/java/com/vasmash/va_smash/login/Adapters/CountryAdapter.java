package com.vasmash.va_smash.login.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.ModelClass.Languages;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Languages> {


    ArrayList<Languages> customers, tempCustomer, suggestions;
    public TextView countryName;
    protected TextView  id;
    ImageView countryimg;


    public CountryAdapter(Context context, ArrayList<Languages> objects) {
        super(context, R.layout.country_pop_model, R.id.country_name, objects);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.country_pop_model, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.country_pop_model, parent, false);
        }
        countryName= (TextView) convertView.findViewById(R.id.country_name);
        id= (TextView) convertView.findViewById(R.id.country_id);
        countryimg=convertView.findViewById(R.id.countryimg);

        countryName.setText(customer.getLang_code());
        id.setText(customer.getLang_name());


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
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Languages cust : tempCustomer) {
                    if (cust.getLang_code().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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




/*BaseAdapter {


    private Context context;
    private ArrayList<Languages> dataModelArrayList;

    public CountryAdapter(Context context, ArrayList<Languages> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    public CountryAdapter(List<Languages> dataitem, Context applicationContext) {
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.country_pop_model, null, true);

            holder. countryName= (TextView) convertView.findViewById(R.id.country_name);
            holder.id= (TextView) convertView.findViewById(R.id.country_id);
            holder.countryimg=convertView.findViewById(R.id.countryimg);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        holder.countryName.setText(dataModelArrayList.get(position).getLang_code());
        holder.id.setText(dataModelArrayList.get(position).getLang_name());

        return convertView;
    }

    private class ViewHolder {

        protected TextView countryName, id;
        ImageView countryimg;

    }



}*/