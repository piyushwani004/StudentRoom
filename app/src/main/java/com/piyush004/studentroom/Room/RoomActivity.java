package com.piyush004.studentroom.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.piyush004.studentroom.Dashboard.HomeActivity;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.Chat.RoomChatFragment;
import com.piyush004.studentroom.Room.storage.RoomStorageFragment;
import com.piyush004.studentroom.Room.users.RoomUsersFragment;
import com.piyush004.studentroom.URoom;

public class RoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    private String RoomAdminEmail;
    private AlertDialog.Builder builderDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        toolbar = findViewById(R.id.toolbarRoom);
        tabLayout = findViewById(R.id.tabLayoutRoom);
        viewPager = findViewById(R.id.ViewPagerRoom);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new RoomStorageFragment(), " ");
        viewPagerAdapter.addFragment(new RoomChatFragment(), " ");
        viewPagerAdapter.addFragment(new RoomUsersFragment(), " ");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_storage_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_chat_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_people_24);


        toolbar.setTitle(URoom.UserRoom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.room_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_RoomExit:
                onRoomExit();
                break;

            case R.id.action_RoomDelete:
                onRoomDelete();
                break;


        }
        return true;
    }


    public void onRoomExit() {
        URoom room = new URoom();
        final DatabaseReference Exit = FirebaseDatabase.getInstance().getReference();
        final String CurrentUser = URoom.UserEmail;
        final String SplitEmail = room.emailSplit(CurrentUser);

        builderDelete = new AlertDialog.Builder(RoomActivity.this);
        builderDelete.setMessage("Do You Want To Exit Room ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Exit.child("ManagedRoom").child(URoom.UserRoom).child("Users").child(SplitEmail).removeValue();
                        finish();
                        startActivity(new Intent(RoomActivity.this, HomeActivity.class));
                        Toast.makeText(RoomActivity.this, "Exit Room Successfully", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builderDelete.create();
        alert.setTitle("Room Exit Alert");
        alert.show();


    }

    public void onRoomDelete() {
        final DatabaseReference Delete = FirebaseDatabase.getInstance().getReference();
        final String CurrentUser = URoom.UserEmail;
        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedAdmin").child(URoom.UserRoom);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                RoomAdminEmail = snapshot.child("Admin").getValue(String.class);
                if (RoomAdminEmail == null) {
                    Toast.makeText(RoomActivity.this, " Admin Not Found ", Toast.LENGTH_SHORT).show();
                } else if (!(CurrentUser.equals(RoomAdminEmail))) {
                    Toast.makeText(RoomActivity.this, "" + CurrentUser + ",You are not Admin of that " + URoom.UserRoom + "", Toast.LENGTH_LONG).show();
                } else if (CurrentUser.equals(RoomAdminEmail)) {

                    builderDelete = new AlertDialog.Builder(RoomActivity.this);
                    builderDelete.setMessage("Do You Want To Delete Current Room ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Delete.child("ManagedAdmin").child(URoom.UserRoom).child("Admin").removeValue();
                                    Delete.child("ManagedRoom").child(URoom.UserRoom).removeValue();

                                    Query RoomQuery = Delete.child("Room").orderByChild("RoomName").equalTo(URoom.UserRoom);

                                    RoomQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            System.out.println("On Canceled");
                                        }

                                    });

                                    startActivity(new Intent(RoomActivity.this, HomeActivity.class));
                                    Toast.makeText(RoomActivity.this, "Remove Room Successfully", Toast.LENGTH_LONG).show();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builderDelete.create();
                    alert.setTitle("Room Delete Alert");
                    alert.show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}