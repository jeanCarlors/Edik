package com.sinapse.eleve.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sinapse.eleve.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CourseProcessingAdapter extends RecyclerView.Adapter<CourseProcessingAdapter.CourseProcessingViewHolder> {
    private List<String> courseContentList;
    private LayoutInflater courseContentInflater;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    public CourseProcessingAdapter(Context context, List<String> courseContentList) {
        this.courseContentList = courseContentList;
        courseContentInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CourseProcessingAdapter.CourseProcessingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cItemView = courseContentInflater.inflate(R.layout.course_item_layout, parent, false);
        return new CourseProcessingAdapter.CourseProcessingViewHolder(cItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseProcessingAdapter.CourseProcessingViewHolder holder, int position) {
        final String sCurrent = courseContentList.get(position);
        String test = sCurrent.split(">")[0];
        String owner = test.split("%")[1];
        holder.owner.setText(owner);
        holder.content.setText(sCurrent.split(">")[1]);
        if(sCurrent.contains("/School Management/")){
            Log.d("Change color", "change cole");
            holder.content.setText("MultimediaDocs"+holder.content.getText().toString());
            holder.content.setText(Html.fromHtml("<u>"+holder.content.getText().toString()+"</u>"));
            holder.content.setTextColor(Color.parseColor("#0000ff"));
        }

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sCurrent.contains("/School Management/")){
                    //storageRef = storage.getReferenceFromUrl(sCurrent.split(">")[1]);
                    storageRef = storage.getReference().child((sCurrent.split(">")[1]).substring(2));
                    File rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String courseDirectoryPathString = storageRef.getParent().getPath();
                    File contentDirectory = new File(rootPath,"/Content/.content");
                    contentDirectory.mkdirs();

                    //File courseDirectory = new File(contentDirectory + courseDirectoryPathString);
                    //courseDirectory.mkdirs();
                    //Date date = new Date();
                    String fileName = "/"+storageRef.getName();
                    File courseContent;
                    if(fileName.contains("video")){
                        courseContent = new File(contentDirectory+"/video.mp4");
                    }else{
                        courseContent = new File(contentDirectory+"/image.jpeg");
                    }

                    Log.d("pathTest", courseContent.getPath());

                    try { courseContent.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        downloadCourseMedia(storageRef, courseContent, v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseContentList.size();
    }

    public class CourseProcessingViewHolder extends RecyclerView.ViewHolder {
        CourseProcessingAdapter courseProcessingAdapter;
        final TextView owner;
        final TextView content;

        public CourseProcessingViewHolder(@NonNull View itemView, CourseProcessingAdapter courseProcessingAdapter) {
            super(itemView);
            owner = itemView.findViewById(R.id.text_owner);
            content = itemView.findViewById(R.id.content);
        }
    }

    private void downloadCourseMedia(final StorageReference storageReference, final File file, final View view) throws IOException {
        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("view.getContext()", file.getPath());
                Intent intent = new Intent(view.getContext(), CourseContentViewer.class);
                //intent.setType("image/jpeg");
                Uri contentUri = Uri.fromFile(file);
                intent.setData(contentUri);
                intent.putExtra("path", file.getPath());
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }
}
