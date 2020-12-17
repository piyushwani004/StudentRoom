package com.piyush004.studentroom.Room.Chat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.URoom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private EditText editTextEnter;
    private CircleImageView circleImageView;
    private String Message;
    private SimpleDateFormat simpleDateFormat;
    private String time, date, userName;

    int animationList = R.anim.layout_animation_up_to_down;


    private FirebaseRecyclerOptions<ChatModel> options;
    private FirebaseRecyclerAdapter<ChatModel, ChatHolder> adapter;


    public RoomChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomChatFragment newInstance(String param1, String param2) {
        RoomChatFragment fragment = new RoomChatFragment();
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
        View view = inflater.inflate(R.layout.fragment_room_chat, container, false);


        swipeRefreshLayout = view.findViewById(R.id.swipeRoomChat);
        recyclerView = view.findViewById(R.id.RoomChatRecyView);
        editTextEnter = view.findViewById(R.id.RoomChatEditText);
        circleImageView = view.findViewById(R.id.RoomChatEnterText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Chat");

        options = new FirebaseRecyclerOptions.Builder<ChatModel>().setQuery(df, new SnapshotParser<ChatModel>() {

            @NonNull
            @Override
            public ChatModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new ChatModel(


                        snapshot.child("Message").getValue(String.class),
                        snapshot.child("Date").getValue(String.class),
                        snapshot.child("UserName").getValue(String.class),
                        snapshot.child("Time").getValue(String.class)
                );

            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<ChatModel, ChatHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull final ChatModel model) {

                holder.setTxtMessage(model.getMessage());
                holder.setTxtUserName(model.getUserName());
                holder.setTxtDate(model.getDate());
                holder.setTxtTime(model.getTime());

            }

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_chat_card, parent, false);

                return new ChatHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message = editTextEnter.getText().toString();

                userName = URoom.UserName;
                Date data = new Date();
                final Calendar calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                time = simpleDateFormat.format(calendar.getTime());
                System.out.println(time);

                simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                date = simpleDateFormat.format(data);
                System.out.println(date);
                System.out.println(userName);

                if (Message.isEmpty()) {
                    editTextEnter.setError("Type a message");
                    editTextEnter.requestFocus();
                } else if (!(Message.isEmpty())) {

                    String key = df.push().getKey();
                    df.child(key).child("UserName").setValue(userName);
                    df.child(key).child("Message").setValue(Message);
                    df.child(key).child("Date").setValue(date);
                    df.child(key).child("Time").setValue(time);
                    editTextEnter.setText(" ");
                    editTextEnter.setHint("Type a message");
                    runAnimationAgain();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runAnimationAgain();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        return view;
    }

    private void runAnimationAgain() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), animationList);
        recyclerView.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}