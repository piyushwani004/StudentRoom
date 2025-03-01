package com.piyush004.studentroom.Room.users;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class UserHolder extends RecyclerView.ViewHolder {


    TextView textViewEmail, textViewName;
    public ImageView imageView;
    public UserHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewEmail = itemView.findViewById(R.id.title_card_Email);
        this.textViewName = itemView.findViewById(R.id.title_card_Name);
        this.imageView = itemView.findViewById(R.id.imageViewUser);
    }

    public void setTxtEmail(String string) {
        textViewEmail.setText(string);
    }

    public void setTxtName(String string) {
        textViewName.setText(string);
    }


}
