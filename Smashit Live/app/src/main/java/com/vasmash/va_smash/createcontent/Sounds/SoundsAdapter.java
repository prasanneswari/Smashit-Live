package com.vasmash.va_smash.createcontent.Sounds;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasmash.va_smash.R;

import java.util.ArrayList;

public class SoundsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Sound_modelclass> soundlist;
    int sound=0;
    boolean array[];
    Activity activity;
    LayoutInflater inflater;

    public SoundsAdapter(Activity activity) {
        this.activity = activity;
    }

    public SoundsAdapter(Activity activity, ArrayList<Sound_modelclass> soundlist) {
        this.activity   = activity;
        this.soundlist      = soundlist;

        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return soundlist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SoundsAdapter.ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.sound_list_item, viewGroup, false);

            holder = new SoundsAdapter.ViewHolder();

            holder.soundname = (TextView)view.findViewById(R.id.sound_name);
            holder.soundid = (TextView)view.findViewById(R.id.sound_name_id);
            holder.soundCheckBox = (ImageView) view.findViewById(R.id.sounds_checkBox_img);

            view.setTag(holder);
        }else
            holder = (SoundsAdapter.ViewHolder)view.getTag();

        Sound_modelclass model = soundlist.get(i);

        holder.soundname.setText(model.getSound_name());
        holder.soundid.setText(model.getSound_code());
       // Log.e("soundadapter",model.getSound_name()+"  "+model.getSound_code());

        if (model.isSelected())
            holder.soundCheckBox.setBackgroundResource(R.drawable.check_box_custom);

        else
            holder.soundCheckBox.setBackgroundResource(R.drawable.sound_uncheck);

        return view;

    }

    public void updateRecords(ArrayList<Sound_modelclass>  soundlist){
        this.soundlist = soundlist;

        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView soundname,soundid;
          ImageView soundCheckBox;

    }































//    public SoundsAdapter(Context context, ArrayList<SoundsAdapter> soundlist) {
//
//        this.context = context;
//        this.soundlist = soundlist;
//    }
//
//
//
//    @Override
//    public int getViewTypeCount() {
//        return getCount();
//    }
//    @Override
//    public int getItemViewType(int position) {
//
//        return position;
//    }
//
//    @Override
//    public int getCount() {
//        return soundlist.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return soundlist.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View view, ViewGroup viewGroup) {
//        SoundsAdapter.ViewHolder holder = null;
//
//        if (view == null){
//
//            view = inflater.inflate(R.layout.sound_list_item, viewGroup, false);
//
//            holder = new SoundsAdapter.ViewHolder();
//
//            holder.soundname = (TextView)view.findViewById(R.id.post_name);
//            holder.soundid = (TextView)view.findViewById(R.id.post_id);
//            holder.soundCheckBox = (ImageView) view.findViewById(R.id.post_checkBox_img);
//
//            view.setTag(holder);
//        }else
//            holder = (SoundsAdapter.ViewHolder)view.getTag();
//
//        SoundsAdapter model = soundlist.get(position);
//
//        holder.tvUserName.setText(model.getLang_code());
//        holder.tvid.setText(model.getLang_name());
//
//        if (model.getSelected())
//            holder.ivCheckBox.setBackgroundResource(R.drawable.check_box_custom);
//
//        else
//            holder.ivCheckBox.setBackgroundResource(R.drawable.uncheck_box_custom);
//
//        return view;
//    }
//
//    private class ViewHolder {
//
//        TextView soundname,soundid;
//        ImageView soundCheckBox;
//
//    }

}
