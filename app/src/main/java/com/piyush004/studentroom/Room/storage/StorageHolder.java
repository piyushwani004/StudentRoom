package com.piyush004.studentroom.Room.storage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.piyush004.studentroom.R;

public class StorageHolder extends RecyclerView.ViewHolder {


    public TextView textViewSubject, textViewRoomName;
    public TextView title_SolutionName, UploadedName;

    public StorageHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewSubject = itemView.findViewById(R.id.title_Subject_Name);
        this.textViewRoomName = itemView.findViewById(R.id.Room_Name);

        this.title_SolutionName = itemView.findViewById(R.id.title_SolutionName);
        this.UploadedName = itemView.findViewById(R.id.UploadedName);
    }

    public void setTxtSubject(String string) {
        textViewSubject.setText(string);
    }


    public void setTxtRoomName(String string) {
        textViewRoomName.setText(string);
    }


    public void setTxtSolutionName(String string) {
        title_SolutionName.setText(string);
    }

    public void setTxtUploadedName(String string) {
        UploadedName.setText("Uploaded By : " + string);
    }

}
