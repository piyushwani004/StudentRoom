package com.piyush004.studentroom.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

            /*case R.id.action_RoomExit:
                Intent intent = new Intent(RoomActivity.this, HomeActivity.class);
                startActivity(intent);
                break;*/

            case R.id.action_RoomDelete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;
    }

}