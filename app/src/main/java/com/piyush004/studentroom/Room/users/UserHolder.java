package com.piyush004.studentroom.Room.users;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class UserHolder extends RecyclerView.ViewHolder {


    TextView textViewEmail;

    public UserHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewEmail = itemView.findViewById(R.id.title_card_Email);
    }

    public void setTxtEmail(String string) {
        textViewEmail.setText(string);
    }

}
