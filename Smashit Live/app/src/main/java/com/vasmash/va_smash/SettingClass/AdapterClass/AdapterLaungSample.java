package com.vasmash.va_smash.SettingClass.AdapterClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vasmash.va_smash.R;

import java.util.List;

import static com.vasmash.va_smash.SettingClass.ContentPreference.laungaddjavaL;
import static com.vasmash.va_smash.SettingClass.ContentPreference.selecedlaungname;

public class AdapterLaungSample extends ArrayAdapter<String> {
    Context context;
    List<String> names;
    List<String> selectednames;

    protected CheckBox checkBox;
    private TextView name, ic;

    public AdapterLaungSample(Context context, List<String> names ,List<String> selectednames) {
        super(context, R.layout.lang_model_pop,names);
        this.context = context;
        this.names = names;
        this.selectednames=selectednames;
        //this.images = images;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.lang_model_pop,null);
        checkBox = (CheckBox) row.findViewById(R.id.lang_checkbox);
        name = (TextView) row.findViewById(R.id.lang_mod);
        ic = (TextView) row.findViewById(R.id.lang_mod);
        name.setText(names.get(position));
        //i1.setImageResource(images[position]);

        Log.d("selectednames","::::"+selectednames);

        for (int i = 0; i < laungaddjavaL.size(); i++) {
            for (int j = 0; j < selecedlaungname.size(); j++) {
                if(selectednames.get(j).equals(laungaddjavaL.get(position))){
                    checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                }

            }

        }

        /*for (int k = 0; k < names.size(); k++) {
            if(selectednames.contains(names.get(k))){
                Log.d("This language is select","::::"+names.get(k));
                if (position==k){
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
                }
                else{
                    checkBox.setVisibility(View.GONE);
                }
            }
            else{
                Log.d("This not is select","::::"+names.get(k));
                checkBox.setVisibility(View.GONE);
            }
        }*/
        return row;
    }
}