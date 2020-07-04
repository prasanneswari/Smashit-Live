package com.vasmash.va_smash.createcontent.Sounds;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.CameraActivity;

import java.util.List;

public class Sounds_Cate_Adapter extends RecyclerView.Adapter<Sounds_Cate_Adapter.ViewHolder> {

    private Context context;
    private List<Sound_catemodel> personUtils11;
    private Sounds_Cate_Adapter.OnSoundcateClickListener onSoundcateClickListener;
    int preSelectedIndex = -1;
    CameraActivity cameraActivity;
    String code;

    public Sounds_Cate_Adapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils11 = personUtils;

    }

    @Override
    public Sounds_Cate_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sounds_cate_model, parent, false);
        Sounds_Cate_Adapter.ViewHolder viewHolder = new Sounds_Cate_Adapter.ViewHolder(v);
//        Log.e("dfadfa",code);
//        cameraActivity.soundlist(code);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Sounds_Cate_Adapter.ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils11.get(position));

        Sound_catemodel pu = personUtils11.get(position);
        Log.e("filter",pu.getSound_cate()+" "+pu.getSound_code()+" "+pu.isSelected());
        holder.catename.setText(pu.getSound_cate());



        if (pu.isSelected()==true) {
            holder.cateimg.setBackgroundResource(R.drawable.continubuttom);
        }
        else {
            holder.cateimg.setBackgroundResource(R.drawable.sound_normal);
        }

        holder.catename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personUtils11.get(position);
                Sound_catemodel model = personUtils11.get(position);
                code =pu.getSound_code();

                Log.e("filter",code);

                personUtils11.get(position).setSelected(true);

                personUtils11.set(position, model);
//                cameraActivity.soundlist(code);
                if (preSelectedIndex > -1){

                    Sound_catemodel preRecord = personUtils11.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    personUtils11.set(preSelectedIndex, preRecord);
                    if (onSoundcateClickListener != null)
                        onSoundcateClickListener.onSoundcateClickListener(personUtils11.get(position));
                    updateRecords(personUtils11);

                }

                preSelectedIndex = position;

                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method



            }
        });
    }

    @Override
    public int getItemCount() {
        return personUtils11.size();
    }

    public void setonSoundcateClickListener(Sounds_Cate_Adapter.OnSoundcateClickListener onSoundcateClickListener) {
        this.onSoundcateClickListener = onSoundcateClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView catename,catecode;
        public ImageView cateimg;


        public ViewHolder(View itemView) {
            super(itemView);


            catename = (TextView) itemView.findViewById(R.id.sound_cate);
            catecode = (TextView) itemView.findViewById(R.id.sound_cate_id);
            cateimg = (ImageView) itemView.findViewById(R.id.sound_cate_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (onSoundcateClickListener != null)
//                        onSoundcateClickListener.onSoundcateClickListener(personUtils11.get(getAdapterPosition()));
//                    updateRecords(personUtils11);
                }
            });

        }
    }
    public interface OnSoundcateClickListener {
        void onSoundcateClickListener(Sound_catemodel code);
    }

    public void updateRecords(List<Sound_catemodel> personUtils11 ){
        this.personUtils11 = personUtils11;

        notifyDataSetChanged();
    }
}
