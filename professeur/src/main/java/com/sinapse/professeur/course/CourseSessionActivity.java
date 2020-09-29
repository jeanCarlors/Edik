package com.sinapse.professeur.course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.UploadTask;
import com.sinapse.libmodule.beans.CourseSession;
import com.sinapse.libmodule.beans.CourseSessionMessage;
import com.sinapse.libmodule.beans.Session;
import com.sinapse.professeur.R;
import com.sinapse.professeur.databinding.ActivityCourseSessionBinding;
import com.sinapse.professeur.utils.Utils;
import com.sinapse.professeur.viewholders.ClassViewHolder;
import com.sinapse.professeur.viewholders.CourseSessionMessageViewHolder;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CourseSessionActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final int PICKFILE_RESULT_CODE = 101;

    ActivityCourseSessionBinding binding;
    SimpleDateFormat dtFormat;

    boolean _details = false;
    boolean _list = false;

    CollectionReference chatRef;

    FirestoreRecyclerAdapter<CourseSessionMessage, CourseSessionMessageViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//R.layout.activity_course_session
        setSupportActionBar(binding.toolbar);

        final String path = getIntent().getStringExtra("PATH");
        if (path == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        binding.btnSend.setOnClickListener(this);
        binding.btnAdd.setOnClickListener(this);

        setTitle("Session");
        dtFormat = new SimpleDateFormat("EEE, dd MMM, hh:mm", Locale.FRANCE);

        FirebaseFirestore.getInstance()
                .document(path)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        binding.sessionDate.setText(dtFormat.format(doc.getTimestamp("created_date").toDate()));
                        String status = doc.getString("status");
                        binding.sessionStatus.setText(status);
                        binding.sessionStatus.setTextColor(
                                "EN COURS".equals(status)
                                        ? Color.GREEN
                                        : Color.RED);

                        _details = true;
                        updateProgressBar();
                    }
                });

        chatRef = FirebaseFirestore.getInstance()
                .document(path)
                .collection("messages");

        Query query = chatRef
                .orderBy("created_date");

        FirestoreRecyclerOptions<CourseSessionMessage> recyclerOptions = new FirestoreRecyclerOptions.Builder<CourseSessionMessage>()
                .setQuery(query, new SnapshotParser<CourseSessionMessage>() {
                    @NonNull
                    @Override
                    public CourseSessionMessage parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        CourseSessionMessage msg = new CourseSessionMessage();
                        msg.setUid(snapshot.getId());
                        msg.setDocPath(snapshot.getReference().getPath());
                        msg.setFromUID(snapshot.getString("from_uid"));
                        msg.setFromName(snapshot.getString("from_name"));
                        msg.setType(snapshot.getString("type"));
                        msg.setContent(snapshot.getString("message"));
                        msg.setContentUrl(snapshot.getString("media_url"));
                        msg.setCreated_date(snapshot.getTimestamp("created_date").toDate());
                        return msg;
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<CourseSessionMessage, CourseSessionMessageViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final CourseSessionMessageViewHolder holder, int position, @NonNull final CourseSessionMessage model) {
                holder.tvFrom.setText(model.getFromName());
                holder.tvDate.setText(dtFormat.format(model.getCreated_date()));
                holder.tvContent.setText(model.getContent());

                holder.imgType.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);

                if (!"TEXT".equals(model.getType())) {
                    holder.imgType.setVisibility(View.VISIBLE);

                    final File rootPath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
                    final File file = new File(rootPath + "/EDIK/" + model.getContentUrl());

                    if (model.getType().contains("image")) {
                        if (!file.exists())
                            holder.imgType.setImageResource(R.drawable.ic_baseline_image_48);
                        else {
                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            holder.imgType.setImageBitmap(myBitmap);
                        }

                    } else if (model.getType().contains("video") || model.getType().contains("audio")) {
                        if (!file.exists())
                            holder.imgType.setImageResource(R.drawable.ic_baseline_ondemand_video_48);
                        else {
                            holder.imgType.setVisibility(View.GONE);
                        }
                    } else {
                        holder.imgType.setImageResource(R.drawable.ic_baseline_insert_drive_file_48);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!"TEXT".equals(model.getType())) {

                                if (file.exists()) {
                                    if (!"TEXT".equals(model.getType())) {
                                        Uri path = FileProvider.getUriForFile(
                                                getApplicationContext(),
                                                getApplicationContext().getApplicationContext().getPackageName() + ".provider",
                                                file
                                        );
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(path, model.getType());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(intent);
                                    }
                                } else {

                                    try {
                                        File dir = new File(rootPath + "/EDIK/sessions/" + chatRef.getParent().getId());
                                        if (!dir.exists()) {
                                            boolean dir_created = dir.mkdirs();
                                            if (!dir_created) {
                                                Log.d("MKDIR", "dir not created : " + dir.getAbsolutePath());
                                                return;
                                            }
                                        }

                                        boolean created = file.createNewFile();
                                        if (!created) {
                                            Log.d("File creation", "File not created : " + file.getAbsolutePath());
                                            return;
                                        }

                                        Log.d("DOWNLOAD URL", model.getContentUrl());
                                        Log.d("LOCAL URL", file.getAbsolutePath());

                                        holder.progressBar.setVisibility(View.VISIBLE);
                                        holder.imgType.setVisibility(View.GONE);

                                        FirebaseStorage.getInstance()
                                                .getReference()
                                                .child(model.getContentUrl())
                                                .getFile(file)
                                                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                                                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                        String msgLog = "Upload is " + progress + "% done";
                                                        Log.d("LOG", msgLog);
                                                        holder.progressBar.setProgress((int) progress);
                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                        holder.progressBar.setVisibility(View.GONE);
                                                        holder.imgType.setVisibility(View.VISIBLE);

                                                        if (model.getType().contains("image")) {
                                                            if (!file.exists())
                                                                holder.imgType.setImageResource(R.drawable.ic_baseline_image_48);
                                                            else {
                                                                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                                holder.imgType.setImageBitmap(myBitmap);
                                                            }

                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        holder.progressBar.setVisibility(View.GONE);
                                                        holder.imgType.setVisibility(View.VISIBLE);
                                                        e.printStackTrace();
                                                    }
                                                });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                        }
                    });
                }

                _list = true;
                updateProgressBar();

            }

            @NonNull
            @Override
            public CourseSessionMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.course_session_message_view_item, parent, false);
                return new CourseSessionMessageViewHolder(view);
            }
        };

        binding.recycleViewCourseSession.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleViewCourseSession.setAdapter(adapter);

    }

    private void updateProgressBar() {
        binding.progressCircular.setVisibility(
                (_details && _list)
                        ? View.GONE : View.VISIBLE
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {

            String msg = binding.msgBox.getText().toString();
            if (msg.length() == 0)
                return;

            final CourseSessionMessage csm = new CourseSessionMessage();
            csm.setContent(msg);
            csm.setFromName(Session.currentUser.getName());
            csm.setFromUID(Session.currentUser.getUid());
            csm.setType("TEXT");
            csm.setCreated_date(new Date());

            chatRef.document().set(Utils.CourseSessionMessaageToMap(csm))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            binding.msgBox.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });

        } else if (v.getId() == R.id.btn_add) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    if (data != null && data.getData() != null)
                        sendFile(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("MESSAGE", "File not load!");
            }
        }
    }

    private void sendFile(Uri fileUri) {

        File file = new File(fileUri.getPath());

        String filePath = "sessions/" + chatRef.getParent().getId();
        filePath += String.format(
                Locale.US,
                "/%s_%s_%s",
                String.valueOf((new Date()).getTime()), Session.currentUser.getUid(), file.getName()
        );

        Log.d("FILE_NAME", filePath);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCustomMetadata("ORIGINAL_NAME", file.getName())
                .build();

        FirebaseStorage.getInstance()
                .getReference()
                .child(filePath)
                .putFile(fileUri, metadata)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        String msgLog = "Upload is " + progress + "% done";
                        Log.d("LOG", msgLog);
                        binding.msgBox.setText(msgLog);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        completeFileMessageSending(taskSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();
                        binding.msgBox.setText("");
                        e.printStackTrace();
                    }
                });
    }

    private void completeFileMessageSending(UploadTask.TaskSnapshot taskSnapshot) {
        final CourseSessionMessage csm = new CourseSessionMessage();
        csm.setContent(taskSnapshot.getMetadata().getCustomMetadata("ORIGINAL_NAME"));
        csm.setContentUrl(taskSnapshot.getMetadata().getPath());
        csm.setFromName(Session.currentUser.getName());
        csm.setFromUID(Session.currentUser.getUid());
        csm.setType(taskSnapshot.getMetadata().getContentType());
        csm.setCreated_date(new Date());

        chatRef.document().set(Utils.CourseSessionMessaageToMap(csm))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        binding.msgBox.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}