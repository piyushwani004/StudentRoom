package com.piyush004.studentroom.Dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class Holder extends RecyclerView.ViewHolder {

    public TextView textViewTitle;
    public ImageView imageView;

    public Holder(@NonNull View itemView) {
        super(itemView);

        this.textViewTitle = itemView.findViewById(R.id.title_card);
        this.imageView = itemView.findViewById(R.id.imageViewRoom);

    }

    public void setTxtTitle(String string) {
        textViewTitle.setText(string);
    }

}
