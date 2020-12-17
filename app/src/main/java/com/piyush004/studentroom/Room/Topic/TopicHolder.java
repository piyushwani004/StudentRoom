package com.piyush004.studentroom.Room.Topic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.piyush004.studentroom.R;

public class TopicHolder extends RecyclerView.ViewHolder {

    public TextView textViewTitle_Topic_Name, textViewRoom_Subject_Name;
    public ImageView circleImageView;

    public TopicHolder(@NonNull View itemView) {
        super(itemView);

        this.textViewTitle_Topic_Name = itemView.findViewById(R.id.title_Topic_Name);
        this.textViewRoom_Subject_Name = itemView.findViewById(R.id.Room_Subject_Name);
        this.circleImageView = itemView.findViewById(R.id.imageViewRoom_Topic);

    }


    public void setTxtTopicName(String string) {
        textViewTitle_Topic_Name.setText(string);
    }


    public void setTxtSubjectName(String string) {
        textViewRoom_Subject_Name.setText(string);
    }

}
