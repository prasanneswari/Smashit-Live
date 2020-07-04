package com.vasmash.va_smash.SettingClass.AdapterClass;

import android.content.Context;
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
import static com.vasmash.va_smash.SettingClass.ContentPreference.selecedlaungcode;
import static com.vasmash.va_smash.SettingClass.ContentPreference.selecedlaungname;

public class AdapterlaungContent extends BaseAdapter {

    private Context context;
    public static ArrayList<Languages> modelArrayList;
    ArrayList<String> lanarrayL;
    static public ArrayList<String> langcodeL;

    public static ArrayList<String> addnameL;
    public static ArrayList<String> langaddcodeL;

    ArrayList<String> addcatL;
    String selctnamesring="null",selctcodesring="null";
    public AdapterlaungContent(Context context, ArrayList<Languages> modelArrayList) {

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
        lanarrayL = new ArrayList<>();
        langcodeL = new ArrayList<>();

        addnameL = new ArrayList<>();
        langaddcodeL = new ArrayList<>();

        addcatL = new ArrayList<>();


        addcatL.clear();
        lanarrayL.clear();
        langcodeL.clear();
        addnameL.clear();
        langaddcodeL.clear();


        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lang_model_pop, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.lang_checkbox);
            holder.name = (TextView) convertView.findViewById(R.id.lang_mod);
            holder.ic = (TextView) convertView.findViewById(R.id.lang_mod);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(modelArrayList.get(position).getLang_name());
        holder.ic.setText(modelArrayList.get(position).getLang_code());

        for (int i = 0; i < laungaddjavaL.size(); i++) {
            for (int j = 0; j < selecedlaungname.size(); j++) {
                if(selecedlaungname.get(j).equals(laungaddjavaL.get(position))){
                    holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                     selctnamesring=selecedlaungname.get(j);
                     selctcodesring=selecedlaungcode.get(j);

                }
            }
        }
        addnameL.add(selctnamesring);
        langaddcodeL.add(selctcodesring);
        //Log.d("addnameLlangggg", ":::"+addnameL);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selecedlaungname.contains(modelArrayList.get(position).getLang_code())) {
                    Log.d("enter iff", ":::");
                    int index = selecedlaungname.indexOf(modelArrayList.get(position).getLang_code());
                    int codeindex = selecedlaungcode.indexOf(modelArrayList.get(position).getLang_name());
                    selecedlaungname.remove(index);
                    selecedlaungcode.remove(codeindex);
                    holder.checkBox.setVisibility(View.GONE);
                } else {
                    Log.d("elseee", ":::");
                    selecedlaungname.add(modelArrayList.get(position).getLang_code());
                    selecedlaungcode.add(modelArrayList.get(position).getLang_name());
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                    //Log.d("addnamelang", ":::" + selecedlaungname);
                }

            }
        });
        return convertView;
    }

    private class ViewHolder {

        protected CheckBox checkBox;
        private TextView name, ic;

    }
}

