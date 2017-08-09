package com.example.jonatas.fileexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jonatas.fileexplorer.R;
import com.example.jonatas.fileexplorer.interfaces.ClickListener;
import com.example.jonatas.fileexplorer.model.FileItem;

import java.util.List;

/**
 * Created by jonatas on 09/04/2017.
 */

public class FileAdapter extends SelectableAdapter<FileAdapter.ViewHolder>   {

    private List<FileItem> files;
    private ClickListener clickListener;
    private boolean isActionModeEnabled;


    public FileAdapter(List<FileItem> files, ClickListener clickListener) {
        this.files = files;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewFile = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewFile, clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(files.get(position).isFolder())
            holder.imageFile.setImageResource(R.drawable.folder);
        else
            holder.imageFile.setImageResource(R.drawable.file);
        holder.nameFile.setText(files.get(position).getNome());
        holder.relativeLayout.setSelected(isSelected(position));

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private ImageView imageFile;
        private TextView nameFile;
        private RelativeLayout relativeLayout;
        private ClickListener clickListener;


        public ViewHolder(View itemView, ClickListener clickListener) {
            super(itemView);

            this.clickListener = clickListener;

            imageFile = (ImageView) itemView.findViewById(R.id.image_file);
            nameFile = (TextView) itemView.findViewById(R.id.text_file);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_file);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null) {
                clickListener.onItemClicked(getAdapterPosition());
                if (isActionModeEnabled) {
                    if (isSelected(getAdapterPosition())) {
                        relativeLayout.setSelected(false);
                    } else {
                        relativeLayout.setSelected(true);
                    }
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(clickListener != null) {
                return clickListener.onItemLongClicked(getAdapterPosition());
            }
            return false;
        }
    }

    public void setActionModeEnabled(boolean isEnabled) {
        this.isActionModeEnabled = isEnabled;
    }


}
