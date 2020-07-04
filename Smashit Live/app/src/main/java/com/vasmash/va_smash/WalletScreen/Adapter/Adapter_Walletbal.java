package com.vasmash.va_smash.WalletScreen.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.WalletScreen.ModelClass.Model_Wallet;

import java.util.ArrayList;

public class Adapter_Walletbal extends RecyclerView.Adapter<Adapter_Walletbal.ViewHolder> {

    ArrayList<Model_Wallet> mainmodels;
    Context context;

    public Adapter_Walletbal(ArrayList<Model_Wallet> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter__walletbal, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.baltxt.setText(mainmodels.get(position).getBalance());
        holder.amount.setText(mainmodels.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView baltxt,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            baltxt = itemView.findViewById(R.id.balencetxt);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
