package com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.vasmash.va_smash.HomeScreen.ModelClass.Model_commentuserlst;
import com.vasmash.va_smash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_commentlist.usericon;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentat;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commentatlist;
import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commenttxt;

public class Adapter_commentusrlist extends ArrayAdapter<Model_commentuserlst> {

    ArrayList<Model_commentuserlst> customers, tempCustomer, suggestions;
    CircleImageView userpic;
    TextView userlst;
    static public String userlist;
    static public String clikuserpos="null";

    public Adapter_commentusrlist(Context context, ArrayList<Model_commentuserlst> objects) {
        super(context, R.layout.adapter_comment_userlist, R.id.username, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<Model_commentuserlst>(objects);
        this.suggestions = new ArrayList<Model_commentuserlst>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    private View initView(final int position, View convertView, ViewGroup parent) {
        Model_commentuserlst customer = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_comment_userlist, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_comment_userlist, parent, false);
        }
        userpic=convertView.findViewById(R.id.userpic);
        userlst=convertView.findViewById(R.id.username);



        userlst.setText(customer.getUsername());
        // holder.commentlistprofile.setImageResource(mainmodels.get(position).getProfile());
        commenttxt.setTextColor(Color.parseColor("#FFFFFF"));
        Picasso.with(getContext()).load(usericon).into(userpic);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("clik values",":::"+customers.get(position).getUsername());

                clikuserpos=customers.get(position).getUsername();
                commenttxt.setText(clikuserpos);
                addImageBetweentext(commentat.getDrawable());
                commenttxt.setTextColor(Color.parseColor("#27AAE1"));
                commentatlist.setVisibility(View.GONE);
            }
        });


        return convertView;
    }
    private void addImageBetweentext(Drawable drawable) {

        drawable .setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = commenttxt.getSelectionStart();
        commenttxt.getText().insert(selectionCursor, ".");
        selectionCursor = commenttxt.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(commenttxt.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        commenttxt.setText(builder);
        commenttxt.setSelection(selectionCursor);
    }
    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter =new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Model_commentuserlst customer =(Model_commentuserlst) resultValue ;
            return customer.getUsername();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Model_commentuserlst cust : tempCustomer) {
                    if (cust.getUsername().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(cust);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Model_commentuserlst> c =  (ArrayList<Model_commentuserlst> )results.values ;
            if (results != null && results.count > 0) {
                clear();
                for (Model_commentuserlst cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
            else{
                clear();
                notifyDataSetChanged();
            }
        }
    };









/*extends RecyclerView.Adapter<Adapter_commentusrlist.ViewHolder> {

    ArrayList<String> mainmodels;
    Context context;
    static public String userlist;
    static public String clikuserpos="null";

    public Adapter_commentusrlist(ArrayList<String> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment_userlist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.userlst.setText(mainmodels.get(position));
        // holder.commentlistprofile.setImageResource(mainmodels.get(position).getProfile());
        commenttxt.setTextColor(Color.parseColor("#FFFFFF"));
        Picasso.with(context).load(usericon).into(holder.userpic);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clik values",":::"+mainmodels.get(position));

                clikuserpos=mainmodels.get(position);
                commenttxt.setText(clikuserpos);
                addImageBetweentext(commentat.getDrawable());
                commenttxt.setTextColor(Color.parseColor("#27AAE1"));
                commentatlist.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userpic;
        TextView userlst;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userpic=itemView.findViewById(R.id.userpic);
            userlst=itemView.findViewById(R.id.username);
        }
    }
    private void addImageBetweentext(Drawable drawable) {

        drawable .setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = commenttxt.getSelectionStart();
        commenttxt.getText().insert(selectionCursor, ".");
        selectionCursor = commenttxt.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(commenttxt.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        commenttxt.setText(builder);
        commenttxt.setSelection(selectionCursor);
    }
*/
}
