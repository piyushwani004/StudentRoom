package com.piyush004.studentroom.Room;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.storage.RoomStorageFragment;
import com.piyush004.studentroom.Room.users.RoomUsersFragment;
import com.piyush004.studentroom.URoom;

public class RoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        String ROOMID = intent.getStringExtra("RoomID");

        URoom.UserRoom = ROOMID;

        toolbar = findViewById(R.id.toolbarRoom);
        tabLayout = findViewById(R.id.tabLayoutRoom);
        viewPager = findViewById(R.id.ViewPagerRoom);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new RoomStorageFragment(), " ");
        viewPagerAdapter.addFragment(new RoomUsersFragment(), " ");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_storage_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_people_24);

        toolbar.setTitle(ROOMID);
        setSupportActionBar(toolbar);

    }
}