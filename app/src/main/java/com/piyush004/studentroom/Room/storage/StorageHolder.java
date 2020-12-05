package com.piyush004.studentroom.Room.storage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class StorageHolder extends RecyclerView.ViewHolder {


    TextView textViewSubject, textViewRoomName;

    public StorageHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewSubject = itemView.findViewById(R.id.title_Subject_Name);
        this.textViewRoomName = itemView.findViewById(R.id.Room_Name);
    }

    public void setTxtSubject(String string) {
        textViewSubject.setText(string);
    }


    public void setTxtRoomName(String string) {
        textViewRoomName.setText(string);
    }

}
