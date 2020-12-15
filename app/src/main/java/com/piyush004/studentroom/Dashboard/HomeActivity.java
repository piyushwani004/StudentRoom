package com.piyush004.studentroom.Dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piyush004.studentroom.Auth.LoginActivity;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.RoomActivity;
import com.piyush004.studentroom.URoom;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private EditText name, pass;
    private FloatingActionButton buttonCreateRoom;
    private RecyclerView recyclerView;
    public Holder holder;
    private FirebaseRecyclerOptions<Model> options;
    private FirebaseRecyclerAdapter<Model, Holder> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    int[] animationList = {R.anim.layout_animation_up_to_down,
            R.anim.layout_animation_right_to_left,
            R.anim.layout_animation_down_to_up,
            R.anim.layout_animation_left_to_right};
    int i = 0;

    private String AdminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        buttonCreateRoom = findViewById(R.id.buttonCreateRoom);
        recyclerView = (RecyclerView) findViewById(R.id.RecycleViewHome);
        swipeRefreshLayout = findViewById(R.id.swipeRoomHome);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (isConnected()) {
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Internet Connection Alert")
                    .setMessage("GO to Setting ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(HomeActivity.this, "Go Back TO HomePage!", Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        URoom uRoom = new URoom();
        URoom.UserEmail = firebaseAuth.getCurrentUser().getEmail();
        String SplitEmail = uRoom.emailSplit(firebaseAuth.getCurrentUser().getEmail());

        DatabaseReference dfn = FirebaseDatabase.getInstance().getReference().child("AppUsers").child(SplitEmail);
        dfn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("Name").getValue(String.class);
                toolbar.setTitle(Name);
                URoom.UserName = Name;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setSupportActionBar(toolbar);

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Room");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        buttonCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.create_room_dialog, null);
                name = dialogLayout.findViewById(R.id.editTextRoomId);
                pass = dialogLayout.findViewById(R.id.editTextRoomPassword);
                builder.setTitle("Create Room");
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sname = name.getText().toString();
                        String spass = pass.getText().toString();

                        if (sname.isEmpty()) {
                            name.setError("Please Enter Name");
                            name.requestFocus();
                        } else if (spass.isEmpty()) {
                            pass.setError("Please Enter Password");
                            pass.requestFocus();
                        } else if (!(sname.isEmpty() && spass.isEmpty())) {
                            String key = df.push().getKey();
                            df.child(key).child("RoomName").setValue(sname);
                            df.child(key).child("RoomPass").setValue(spass);

                            DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedAdmin");
                            df.child(sname).child("Admin").setValue(firebaseAuth.getCurrentUser().getEmail());
                            URoom.RoomAdmin = firebaseAuth.getCurrentUser().getEmail();
                            Toast.makeText(getApplicationContext(), "Room Created...", Toast.LENGTH_SHORT).show();
                            if (i < animationList.length - 1) {
                                i++;
                            } else {
                                i = 0;
                            }
                            runAnimationAgain();
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


        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(df, new SnapshotParser<Model>() {

            @NonNull
            @Override
            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new Model(

                        snapshot.child("RoomName").getValue(String.class),
                        snapshot.child("RoomPass").getValue(String.class)
                );

            }
        }).build();
        adapter = new FirebaseRecyclerAdapter<Model, Holder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull final Model model) {

                holder.setTxtTitle(model.getName());

                holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String roompassword = model.getPassword();

                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.room_password_dialog, null);
                        final TextView roomName_d = dialogLayout.findViewById(R.id.textViewRoomId_d);
                        final EditText roomPassword_d = dialogLayout.findViewById(R.id.editTextRoomPassword_d);

                        roomName_d.setText(model.getName());
                        builder.setTitle("Authentication");
                        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String dPass = roomPassword_d.getText().toString();

                                if (dPass.isEmpty()) {
                                    roomPassword_d.setError("Please Enter Password");
                                    roomPassword_d.requestFocus();
                                } else if (!(dPass.isEmpty())) {
                                    if (dPass.equals(roompassword)) {

                                        System.out.println("Admin : " + URoom.RoomAdmin);

//                                        Toast.makeText(getApplicationContext(), "Password Match", Toast.LENGTH_SHORT).show();
                                        URoom.UserRoom = model.getName();
                                        DatabaseReference dff = FirebaseDatabase.getInstance().getReference().child("ManagedRoom");
                                        String EmailResult = emailSplit(firebaseAuth.getCurrentUser().getEmail());
                                        dff.child(model.getName()).child("Users").child(EmailResult).setValue(firebaseAuth.getCurrentUser().getEmail());

                                        Intent intent = new Intent(HomeActivity.this, RoomActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password Does Not Match", Toast.LENGTH_SHORT).show();
                                    }
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


            }

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_card, parent, false);

                return new Holder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setQueryHint("Search Rooms...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //holder.getFilter().filter(newText);
                System.out.println(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Logout...");
                alertDialogBuilder.setMessage("Do You Want To Exit ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
        return true;
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void runAnimationAgain() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, animationList[i]);
        recyclerView.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        runAnimationAgain();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        runAnimationAgain();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runAnimationAgain();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public String emailSplit(String str) {
        String resultStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 64 && str.charAt(i) <= 122) {
                resultStr = resultStr + str.charAt(i);
            }
        }
        return resultStr;
    }


}
