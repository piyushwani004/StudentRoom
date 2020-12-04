package com.piyush004.studentroom.Room.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.URoom;


public class RoomUsersFragment extends Fragment {


    private View view;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<UserModel> options;
    private FirebaseRecyclerAdapter<UserModel, UserHolder> adapter;

    public RoomUsersFragment() {
        // Required empty public constructor
    }

    public static RoomUsersFragment newInstance(String param1, String param2) {
        RoomUsersFragment fragment = new RoomUsersFragment();
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

        view = inflater.inflate(R.layout.fragment_room_users, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewRoomUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        System.out.println(URoom.UserRoom);
        final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Users");

        options = new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(df, new SnapshotParser<UserModel>() {

            @NonNull
            @Override
            public UserModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new UserModel(

                        snapshot.getValue(String.class)
                );

            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<UserModel, UserHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull final UserModel model) {

                holder.setTxtEmail(model.getEmail());

                holder.textViewEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

            }

            @NonNull
            @Override
            public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_users_card, parent, false);

                return new UserHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return view;
    }
}