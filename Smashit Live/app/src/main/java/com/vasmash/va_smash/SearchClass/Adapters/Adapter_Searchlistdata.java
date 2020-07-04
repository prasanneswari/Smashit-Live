package com.vasmash.va_smash.SearchClass.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlist;
import com.vasmash.va_smash.SearchClass.SearchData;

import java.util.ArrayList;

public class Adapter_Searchlistdata extends RecyclerView.Adapter<Adapter_Searchlistdata.ViewHolder>{

    ArrayList<Model_Searchlist> dataObjectList;
    Context context;
    View view1;
    ViewHolder viewHolder1;
    static public String searchlistdata="null";
    static public boolean searchadapterlist;

    public Adapter_Searchlistdata(Context context1,ArrayList<Model_Searchlist> dataObjectList){

        this.dataObjectList = dataObjectList;
        this.context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;


        public ViewHolder(View v){

            super(v);
            name = v.findViewById(R.id.name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.activity_adapter__searchlistdata,parent,false);

        viewHolder1 = new ViewHolder(view1);



        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        holder.name.setText(dataObjectList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SearchData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                searchlistdata=dataObjectList.get(position).getName();
                searchadapterlist=true;

            }
        });


    }

    @Override
    public int getItemCount(){
        return dataObjectList.size();
    }


}
