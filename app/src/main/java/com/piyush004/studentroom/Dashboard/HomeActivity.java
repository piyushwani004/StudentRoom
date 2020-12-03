package com.piyush004.studentroom.Dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.piyush004.studentroom.Auth.LoginActivity;
import com.piyush004.studentroom.R;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private EditText name, pass;
    private FloatingActionButton buttonCreateRoom;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Model> options;
    private FirebaseRecyclerAdapter<Model, Holder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        buttonCreateRoom = findViewById(R.id.buttonCreateRoom);
        recyclerView = (RecyclerView) findViewById(R.id.RecycleViewHome);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle("Home");
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
                            Toast.makeText(getApplicationContext(), "Room Created...", Toast.LENGTH_SHORT).show();
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

                        snapshot.child("RoomName").getValue().toString()

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        searchView.setQueryHint("Search Rooms...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do the search here
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
