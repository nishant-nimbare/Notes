package com.example.mohan.notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Mohan on 03-16-2018.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {
    Note note;
    ArrayList<Note> list;
    Context context;
    onItemClickListener listener;
    int size;


    public interface onItemClickListener {
        public void onItemClick(Note item);
    }

    public myAdapter(ArrayList<Note> list, Context context, onItemClickListener listener,int size) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.size=size;
    }

    @Override
    public void onBindViewHolder(myAdapter.myViewHolder holder, int position) {
     try {
         note = list.get(position);
         holder.bind(note, listener);
     }catch (NullPointerException e){
         Toast.makeText(context,"no notes added",Toast.LENGTH_SHORT).show();
     }

    }

    @Override
    public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public int getItemCount() {

       return size;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;

        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
        }

        public void bind(final Note note, final onItemClickListener listener) {
            title.setText(note.get_title());
            description.setText(note.get_description());

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listener.onItemClick(note);
                }
            });

        }

    }
}
