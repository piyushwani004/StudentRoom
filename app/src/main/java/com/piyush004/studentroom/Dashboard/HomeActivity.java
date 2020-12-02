package com.piyush004.studentroom.Dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.piyush004.studentroom.Auth.LoginActivity;
import com.piyush004.studentroom.R;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

       /* System.out.println("UID "+firebaseAuth.getCurrentUser().getUid());
        System.out.println("email "+firebaseAuth.getCurrentUser().getEmail());
        System.out.println("disname "+firebaseAuth.getCurrentUser().getDisplayName());
        System.out.println("tokent "+firebaseAuth.getCurrentUser().getIdToken(true).toString());
        System.out.println("tokenf " +firebaseAuth.getCurrentUser().getIdToken(false).toString());
        System.out.println("phone "+firebaseAuth.getCurrentUser().getPhoneNumber());
        System.out.println("pid "+firebaseAuth.getCurrentUser().getProviderId());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        searchView.setQueryHint("Search Classes...");
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
            case R.id.action_profile:

                Toast.makeText(this, "You clicked Profile", Toast.LENGTH_SHORT).show();
                break;

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
