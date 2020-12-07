package com.piyush004.studentroom.Room.storage.StorageHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.piyush004.studentroom.R;
import com.piyush004.studentroom.Room.RoomActivity;
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
    private TextView textViewFileName;
    private RecyclerView recyclerViewSolution;
    private FirebaseRecyclerOptions<StorageModel> options;
    private FirebaseRecyclerAdapter<StorageModel, StorageHolder> adapter;
    public int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_storage_handler);

        toolbar = findViewById(R.id.toolbarStorageHandler);
        textViewFileName = findViewById(R.id.title_File_Name);
        recyclerViewSolution = findViewById(R.id.RoomStoHandRecyViewSolu);
        recyclerViewSolution.setHasFixedSize(true);
        recyclerViewSolution.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle(URoom.UserRoom);
        setSupportActionBar(toolbar);


        final DatabaseReference dff = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child("Solutions");
        options = new FirebaseRecyclerOptions.Builder<StorageModel>().setQuery(dff, new SnapshotParser<StorageModel>() {

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

                holder.setTxtSolutionName("Solution" + i);
                i++;
                holder.setTxtUploadedName(URoom.UserRoom);

                holder.title_SolutionName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


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
                intentQue.setType("*/*");
                startActivityForResult(intentQue, SELECT_Question);
                break;

            case R.id.action_RoomAnswer:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, SELECT_Solution);
                break;

            case R.id.action_RoomExitStorage:
                Intent intent2 = new Intent(RoomStorageHandler.this, RoomActivity.class);
                startActivity(intent2);
                break;

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

                    String FileName = f.getName();

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

                    String FileName = file.getName();

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

            final StorageReference ref = storageReference.child("Files/").child(URoom.UserRoom).child(URoom.RoomSubject).child("Question");
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String FileUri = uri.toString();
                                    final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child("Question");
                                    df.child("Question").setValue(FileUri);
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
        final URoom uRoom = new URoom();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("Files/").child(URoom.UserRoom).child(URoom.RoomSubject).child("Solution");
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String FileUri = uri.toString();
                                    final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("ManagedRoom").child(URoom.UserRoom).child("Storage").child(URoom.RoomSubject).child("Solutions");
                                    String userKey = uRoom.emailSplit(URoom.UserEmail);
                                    df.child(userKey).setValue(FileUri);
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


}