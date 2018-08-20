package com.example.mohan.notes.Util;

import android.app.Notification;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.title.setText(curNote.get_title());
        holder.description.setText(curNote.get_description());

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
       public TextView description;

        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        listener.onItemClick(getAdapterPosition());
                    }
                });
        }

    }
}
