package com.piyush004.studentroom.Room.storage.StorageHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.RoomActivity;
import com.piyush004.studentroom.Room.storage.StorageHandler.PDFView.ViewPDF;
import com.piyush004.studentroom.Room.storage.StorageHolder;
import com.piyush004.studentroom.Room.storage.StorageModel;
import com.piyush004.studentroom.URoom;

import java.io.File;

public class RoomStorageHandler extends AppCompatActivity {

    private Toolbar toolbar;
    private Uri uri;
    private static int SELECT_Question = 1;
    private static int SELECT_Solution = 2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextView textViewFileName, textViewTopic;
    private RecyclerView recyclerViewSolution;
    private FirebaseRecyclerOptions<StorageModel> options;
    private FirebaseRecyclerAdapter<StorageModel, StorageHolder> adapter;
    private String FileName;
private SwipeRefreshLayout swipeRefreshLayout;
    int[] animationList = {R.anim.layout_animation_up_to_down,
            R.anim.layout_animation_right_to_left,
            R.anim.layout_animation_down_to_up,
            R.anim.layout_animation_left_to_right};
    int i = 0;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_storage_handler);

        toolbar = findViewById(R.id.toolbarStorageHandler);
        textViewFileName = findViewById(R.id.title_File_Name);
        textViewTopic = findViewById(R.id.textViewTopicTitle);
        recyclerViewSolution = findViewById(R.id.RoomStoHandRecyViewSolu);
        swipeRefreshLayout = findViewById(R.id.swipeRoomStorageHandler);

        recyclerViewSolution.setHasFixedSize(true);
        recyclerViewSolution.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle(URoom.RoomSubject);
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


        textViewTopic.setText(URoom.SubjectTopic);

        final URoom room = new URoom();

        final String UName = room.emailSplit(URoom.UserEmail);
        DatabaseReference dfName = FirebaseDatabase.getInstance().getReference().child("AppUsers");
        dfName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UNAME = snapshot.child(UName).child("Name").getValue(String.class);
                URoom.UserName = UNAME;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference dff = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child(URoom.SubjectTopic).child("Solutions");
        options = new FirebaseRecyclerOptions.Builder<StorageModel>().setQuery(dff, new SnapshotParser<StorageModel>() {

            @NonNull
            @Override
            public StorageModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new StorageModel(

                        snapshot.getValue(String.class),
                        snapshot.getKey()
                );

            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<StorageModel, StorageHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final StorageHolder holder, int position, @NonNull final StorageModel model) {

                showQuestionPDF();
                holder.setTxtSolutionName("Solution");
                holder.setTxtUploadedName(model.getURIName());

                holder.title_SolutionName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(RoomStorageHandler.this, model.getSubject(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(v.getContext(), ViewPDF.class);
                        intent.putExtra("link", model.getSubject());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public StorageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_storage_solutions_card, parent, false);

                return new StorageHolder(view);
            }
        };

        adapter.startListening();
        recyclerViewSolution.setAdapter(adapter);

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
        menuInflater.inflate(R.menu.storage_handler_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_RoomQuestion:
                Intent intentQue = new Intent(Intent.ACTION_GET_CONTENT);
                intentQue.addCategory(Intent.CATEGORY_OPENABLE);
                intentQue.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intentQue, "Select PDF File"), SELECT_Question);
                break;

            case R.id.action_RoomAnswer:
                Intent intentAns = new Intent(Intent.ACTION_GET_CONTENT);
                intentAns.addCategory(Intent.CATEGORY_OPENABLE);
                intentAns.setType("application/pdf");
                startActivityForResult(Intent.createChooser(intentAns, "Select PDF File"), SELECT_Solution);
                break;

            /*case R.id.action_RoomExitStorage:
                Intent intent2 = new Intent(RoomStorageHandler.this, RoomActivity.class);
                startActivity(intent2);
                break;*/

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 1:

                if (resultCode == RESULT_OK) {

                    uri = data.getData();

                    File f = new File("" + uri);

                    FileName = f.getName();

                    FileName = FileName.replaceAll("[^ .,a-zA-Z0-9]", "");

                    FileName = FileName.replaceAll("[0123456789]", "");

                    textViewFileName.setText(FileName);

                    uploadQuestion();

                }
                break;

            case 2:

                if (resultCode == RESULT_OK) {

                    uri = data.getData();

                    File file = new File("" + uri);

                    FileName = file.getName();

                    FileName = FileName.replaceAll("[^ .,a-zA-Z0-9]", "");

                    FileName = FileName.replaceAll("[0123456789]", "");

                    uploadSolutions();

                }
                break;


        }
    }


    private void uploadQuestion() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Upload" + System.currentTimeMillis() + ".PDF");
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String FileUri = uri.toString();
                                    final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child(URoom.SubjectTopic).child("Question");
                                    df.child("Question").setValue(FileUri);
                                    df.child("FileName").setValue(FileName);
                                }
                            });

                            Toast.makeText(RoomStorageHandler.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RoomStorageHandler.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    private void uploadSolutions() {
//        final URoom uRoom = new URoom();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("Upload" + System.currentTimeMillis() + ".PDF");
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String FileUri = uri.toString();
                                    final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child(URoom.SubjectTopic).child("Solutions");
                                    df.child(URoom.UserName).setValue(FileUri);
                                }
                            });

                            Toast.makeText(RoomStorageHandler.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RoomStorageHandler.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    public void showQuestionPDF() {
        DatabaseReference dff = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child(URoom.SubjectTopic).child("Question");

        dff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String FirebaseFileName = snapshot.child("FileName").getValue(String.class);
                final String FileURI = snapshot.child("Question").getValue(String.class);
                textViewFileName.setText(FirebaseFileName);

                textViewFileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ViewPDF.class);
                        intent.putExtra("link", FileURI);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void runAnimationAgain() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, animationList[i]);
        recyclerViewSolution.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
        recyclerViewSolution.scheduleLayoutAnimation();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter.startListening();
        recyclerViewSolution.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        recyclerViewSolution.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}