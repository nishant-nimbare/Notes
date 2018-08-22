package com.example.mohan.notes.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohan.notes.model.Note;
import com.example.mohan.notes.R;

import java.util.ArrayList;

/**
 * Created by Mohan on 03-16-2018.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    private static final String TAG = "myAdapter";
    ArrayList<Note> notes;
    Context context;
    onItemClickListener listener;

    public myAdapter(ArrayList<Note> list, Context context, onItemClickListener listener) {
        this.notes = list;
        this.context = context;
        this.listener = listener;
        Log.e(TAG, "myAdapter: notes size" + notes.size());
    }

    @Override
    public void onBindViewHolder(myAdapter.myViewHolder holder, int position) {

        Note curNote =notes.get(position);
        int TITLE_SIZE=curNote.get_title().length();

        SpannableStringBuilder str = new SpannableStringBuilder(curNote.get_title()+"\n"+curNote.get_description());

        str.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),0,TITLE_SIZE, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.5f),0,TITLE_SIZE, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.title.setText(str);



    }

    @Override
    public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public int getItemCount() {

       return notes.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
       public TextView title;

        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener.onItemClick(getAdapterPosition());
                    }
                });
        }

    }
}
