package com.vasmash.va_smash.login.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.ModelClass.Languages;

import java.util.ArrayList;

public class Language_popup_adapter extends BaseAdapter {

    private Context context;
    public static ArrayList<Languages> modelArrayList;
    ArrayList<String> lanarrayL;
    static public ArrayList<String> langcodeL;


    public Language_popup_adapter(Context context, ArrayList<Languages> modelArrayList) {

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
        langcodeL=new ArrayList<>();
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lang_model_pop, null, true);

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

        holder.checkBox.setChecked(modelArrayList.get(position).getSelected());



        //Log.e("adr",modelArrayList.get(position).getLang_name()+"  "+modelArrayList.get(position).getLang_code());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag( position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.checkBox.getTag(R.integer.btnplusview);
                TextView name = (TextView) tempview.findViewById(R.id.lang_mod);
                TextView ic = (TextView) tempview.findViewById(R.id.lang_mod);
                Integer pos = (Integer)  holder.checkBox.getTag();
                //Toast.makeText(context, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();

                if(modelArrayList.get(pos).getSelected()){
                    modelArrayList.get(pos).setSelected(false);

                }else {
                    modelArrayList.get(pos).setSelected(true);
                }

            }
        });
       /* holder.checkBox.setBackgroundResource(R.drawable.worngsliderbuttom);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    // do something

                    holder.checkBox.setBackgroundResource(R.drawable.correctsliderbuttom);
                }
                else
                {
                    // do another something
                    holder.checkBox.setBackgroundResource(R.drawable.worngsliderbuttom);

                }
            }
        });*/


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   holder.name.setTextColor(Color.parseColor("#66BD00"));
                // lanarrayL.add(modelArrayList.get(position).getLang_code());


                if (lanarrayL.contains(modelArrayList.get(position).getLang_code())){
                    Log.d("enter iff",":::");
                    int index=lanarrayL.indexOf(modelArrayList.get(position).getLang_code());
                    int codeindex=langcodeL.indexOf(modelArrayList.get(position).getLang_name());
                    lanarrayL.remove(index);
                    langcodeL.remove(codeindex);

                    holder.checkBox.setVisibility(View.GONE);

                }else {
                    Log.d("elseee",":::");
                    lanarrayL.add(modelArrayList.get(position).getLang_code());
                    langcodeL.add(modelArrayList.get(position).getLang_name());
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);                   // holder.name.setTextColor(Color.parseColor("#fff"));

                    //  holder.checkBox.setBackgroundResource(R.drawable.ic_selected_icon);
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
