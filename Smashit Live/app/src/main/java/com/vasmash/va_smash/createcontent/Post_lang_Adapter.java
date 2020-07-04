package com.vasmash.va_smash.createcontent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.ModelClass.Languages;

import java.util.ArrayList;

public class Post_lang_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Languages> categ;

    boolean array[];
    Activity activity;
    LayoutInflater inflater;

    //short to create constructer using command+n for mac & Alt+Insert for window


    public Post_lang_Adapter(Activity activity) {
        this.activity = activity;
    }

    public Post_lang_Adapter(Activity activity, ArrayList<Languages> users) {
        this.activity   = activity;
        this.categ      = users;

        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return categ.size();
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

        Post_lang_Adapter.ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.post_cate_lang_item, viewGroup, false);

            holder = new Post_lang_Adapter.ViewHolder();

            holder.tvUserName = (TextView)view.findViewById(R.id.post_name);
            holder.tvid = (TextView)view.findViewById(R.id.post_id);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.post_checkBox_img);

            view.setTag(holder);
        }else
            holder = (Post_lang_Adapter.ViewHolder)view.getTag();

        Languages model = categ.get(i);

        holder.tvUserName.setText(model.getLang_code());
        holder.tvid.setText(model.getLang_name());

        if (model.getSelected())
            holder.ivCheckBox.setBackgroundResource(R.drawable.check_box_custom);

        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.uncheck_box_custom);

        return view;

    }

    public void updateRecords(ArrayList<Languages>  users){
        this.categ = users;

        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView tvUserName,tvid;
        ImageView ivCheckBox;

    }

//    public Post_lang_Adapter(Context context, ArrayList<Languages> categ) {
//        super();
//        this.context = context;
//        this.categ = categ;
//        array =new boolean[categ.size()];
//    }
//
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return categ.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return categ.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        View v=convertView;
//        final int pos=position;
//        if(v==null)
//        {
//            v= LayoutInflater.from(context).inflate(R.layout.post_cate_lang_item,null);
//        }
//
//        TextView name=(TextView) v.findViewById(R.id.post_name);
//        TextView id=(TextView) v.findViewById(R.id.post_id);
//        final CheckBox chkbox=(CheckBox) v.findViewById(R.id.post_checkBox);
//
//        name.setText(categ.get(position).getLang_code());
//        id.setText(categ.get(position).getLang_name());
//        Log.e("lang",categ.get(position).getLang_name()+"  "+categ.get(position).getLang_code());
//        int selectedindexitem=0;
//        chkbox.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                if(chkbox.isChecked())
//                {
//                    array[pos]=true;
//
//
//                }else{
//                    array[pos]=false;
//                }
//            }
//        });
//        chkbox.setChecked(array[pos]);
//
//
//
//
//
//        return v;
//    }




}
