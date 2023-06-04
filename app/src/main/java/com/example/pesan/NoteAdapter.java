package com.example.pesan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    ArrayList<note> notelist;

    OnItemClickListener onItemClickListener;

    public NoteAdapter(Context context, ArrayList<note> notelist){
        this.context = context;
        this.notelist = notelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(notelist.get(position).getTitle());
        holder.ayat.setText(notelist.get(position).getAyat());
        holder.content.setText(notelist.get(position).getContent());
        holder.itemView.setOnClickListener(view -> onItemClickListener.OnClick(notelist.get(position)));
    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,  ayat, content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_item_title);
            ayat = itemView.findViewById(R.id.ayat);
            content = itemView.findViewById(R.id.list_item_content);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnClick(note note);
    }
}
