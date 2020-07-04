package com.vasmash.va_smash.SettingClass.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.ModelClass.Languages;

import java.util.ArrayList;

import static com.vasmash.va_smash.SettingClass.ContentPreference.laungaddjavaL;
import static com.vasmash.va_smash.SettingClass.ContentPreference.settingscatL;
import static com.vasmash.va_smash.SettingClass.ContentPreference.settingscatnameL;

public class Adapter_Contentpreference extends BaseAdapter {

    private Context context;
    public static ArrayList<Languages> modelArrayList;
    int increment=1;
    boolean doubleclik=true;
    ArrayList<String> lanarrayL;
    static public ArrayList<String> catcodeL;


    public static ArrayList<String> addnameL;
    public static ArrayList<String> addcodeL;

    ArrayList<String> addcatL;
    String selctnamesring="null",selctcodesring="null";


    public Adapter_Contentpreference(Context context, ArrayList<Languages> modelArrayList) {

        this.context = context;
        this.modelArrayList = modelArrayList;

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
        return modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        lanarrayL=new ArrayList<>();
        catcodeL=new ArrayList<>();

        lanarrayL.clear();
        catcodeL.clear();

        addnameL = new ArrayList<>();
        addcodeL = new ArrayList<>();

        addcatL = new ArrayList<>();
        addcatL.clear();

        addnameL.clear();
        addcodeL.clear();


        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_model_pop, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.lang_checkbox);
            holder.name = (TextView) convertView.findViewById(R.id.lang_mod);
            holder.ic = (TextView) convertView.findViewById(R.id.lang_mod);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }


        holder.name.setText(modelArrayList.get(position).getLang_name());
        holder.ic.setText(modelArrayList.get(position).getLang_code());


        for (int i = 0; i < laungaddjavaL.size(); i++) {
            for (int j = 0; j < settingscatnameL.size(); j++) {
                if(settingscatnameL.get(j).equals(laungaddjavaL.get(position))){
                    holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                    selctnamesring=settingscatnameL.get(j);
                    selctcodesring=settingscatL.get(j);
                    holder.name.setTextColor(Color.parseColor("#66BD00"));
                }
            }
        }
        addnameL.add(selctnamesring);
        addcodeL.add(selctcodesring);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settingscatnameL.contains(modelArrayList.get(position).getLang_code())) {
                    Log.d("enter iff", ":::");
                    int index = settingscatnameL.indexOf(modelArrayList.get(position).getLang_code());
                    settingscatnameL.remove(index);
                    int catindex = settingscatL.indexOf(modelArrayList.get(position).getLang_name());
                    settingscatL.remove(catindex);
                    holder.checkBox.setVisibility(View.GONE);
                    holder.name.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    Log.d("elseee", ":::");
                    settingscatnameL.add(modelArrayList.get(position).getLang_code());
                    settingscatL.add(modelArrayList.get(position).getLang_name());
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                    // holder.name.setTextColor(Color.parseColor("#fff"));
                    holder.name.setTextColor(Color.parseColor("#66BD00"));
                   // Log.d("lanarrayLcat", ":::" + settingscatnameL);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        protected CheckBox checkBox;
        private TextView name,ic;
    }

}
