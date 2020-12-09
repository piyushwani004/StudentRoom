package com.piyush004.studentroom.Room.storage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.Topic.SubjectTopicActivity;
import com.piyush004.studentroom.Room.storage.StorageHandler.RoomStorageHandler;
import com.piyush004.studentroom.URoom;


public class RoomStorageFragment extends Fragment {

    private View view;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private EditText editTextSubName;
    private FirebaseRecyclerOptions<StorageModel> options;
    private FirebaseRecyclerAdapter<StorageModel, StorageHolder> adapter;

    public RoomStorageFragment() {
        // Required empty public constructor
    }

    public static RoomStorageFragment newInstance(String param1, String param2) {
        RoomStorageFragment fragment = new RoomStorageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room_storage, container, false);

        floatingActionButton = view.findViewById(R.id.floatingActionButtonRoomStorage);
        recyclerView = view.findViewById(R.id.RecycleViewRoomStorage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Subject");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.create_subject_dialog, null);
                editTextSubName = dialogLayout.findViewById(R.id.editTextRoomSubjectName);
                builder.setTitle("Subject Name");
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sname = editTextSubName.getText().toString();

                        if (sname.isEmpty()) {
                            editTextSubName.setError("Please Enter Subject Name");
                            editTextSubName.requestFocus();
                        } else if (!(sname.isEmpty())) {
                            String key = df.push().getKey();
                            df.child(key + sname).setValue(sname);
                            Toast.makeText(getContext(), "Subject Created...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Closed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setView(dialogLayout);
                builder.show();

            }
        });

        options = new FirebaseRecyclerOptions.Builder<StorageModel>().setQuery(df, new SnapshotParser<StorageModel>() {

            @NonNull
            @Override
            public StorageModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new StorageModel(

                        snapshot.getValue(String.class)
                );

            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<StorageModel, StorageHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull StorageHolder holder, int position, @NonNull final StorageModel model) {

                holder.setTxtSubject(model.getSubject());
                holder.setTxtRoomName(URoom.UserRoom);

                holder.textViewSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        URoom.RoomSubject = model.getSubject();
                        Intent intent = new Intent(getContext(), SubjectTopicActivity.class);
                        startActivity(intent);
//                        Toast.makeText(getContext(), "clicked...", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public StorageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_create_subject_card, parent, false);

                return new StorageHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


        return view;
    }
}