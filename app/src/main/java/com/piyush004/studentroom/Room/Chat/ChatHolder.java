package com.piyush004.studentroom.Room.Chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class ChatHolder extends RecyclerView.ViewHolder {

    public TextView TVMessage, TVUserName, TVDate, TVTime;


    public ChatHolder(@NonNull View itemView) {
        super(itemView);

        this.TVMessage = itemView.findViewById(R.id.textView12);
        this.TVUserName = itemView.findViewById(R.id.textView6);
        this.TVDate = itemView.findViewById(R.id.textView11);
        this.TVTime = itemView.findViewById(R.id.textView13);

    }

    public void setTxtMessage(String string) {
        TVMessage.setText(string);
    }

    public void setTxtUserName(String string) {
        TVUserName.setText(string);
    }

    public void setTxtDate(String string) {
        TVDate.setText(string);
    }

    public void setTxtTime(String string) {
        TVTime.setText(string);
    }

}
