package com.vasmash.va_smash.WalletScreen.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.WalletScreen.ModelClass.ModelTransaction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Adapter_AllTransactions extends RecyclerView.Adapter<Adapter_AllTransactions.ViewHolder> {

    ArrayList<ModelTransaction> mainmodels;
    ArrayList<String> todoarray=new ArrayList<>();
    ArrayList<String> userarray=new ArrayList<>();

    ArrayList<String> todoarraypic=new ArrayList<>();
    ArrayList<String> userarraypic=new ArrayList<>();


    Context context;
    public Adapter_AllTransactions(ArrayList<ModelTransaction> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter__all_transactions, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.date.setText(mainmodels.get(position).getDate());
        holder.amount.setText(mainmodels.get(position).getBalance());
        //holder.name.setText(mainmodels.get(position).getTodousername());


        String gettype=mainmodels.get(position).getType();
        Log.d("usertype",":::::"+gettype);

        if (gettype.equals("1")){
            Log.d("1 name",":::::"+mainmodels.get(position).getTodousername());
            holder.name.setText(mainmodels.get(position).getTodousername());
            Picasso.with(context)
                    .load(mainmodels.get(position).getTodoprofilepic())
                    .placeholder(R.drawable.uploadpictureold)
                    .into(holder.transpic);

        }else if (gettype.equals("2")){
            holder.name.setText(mainmodels.get(position).getName());
            Log.d("2 name",":::::"+mainmodels.get(position).getName());
            Picasso.with(context)
                    .load(mainmodels.get(position).getProfilepic())
                    .placeholder(R.drawable.uploadpictureold)
                    .into(holder.transpic);

        }else if (gettype.equals("0")){
            //Log.d("0 name",":::::"+mainmodels.get(position).getTodousername());
            todoarray.add(mainmodels.get(position).getTodousername());
            userarray.add(mainmodels.get(position).getName());
            userarray.addAll(todoarray);
            for (int i=0;i<userarray.size();i++) {
                if (userarray.get(i)!=null) {
                    holder.name.setText(userarray.get(i));
                }
            }


            todoarraypic.add(mainmodels.get(position).getTodoprofilepic());
            userarraypic.add(mainmodels.get(position).getProfilepic());
            userarraypic.addAll(todoarraypic);
            for (int i=0;i<userarray.size();i++) {

                for (int k=0;k<userarraypic.size();k++) {
                    if (userarraypic.get(i)!=null) {
                        Picasso.with(context)
                                .load(userarraypic.get(i))
                                .placeholder(R.drawable.uploadpictureold)
                                .into(holder.transpic);
                    }
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,amount;
        ImageView transpic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            transpic=itemView.findViewById(R.id.transpic);

        }
    }
}
