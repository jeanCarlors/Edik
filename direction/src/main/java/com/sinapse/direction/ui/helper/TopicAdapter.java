package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<String> topicList = new ArrayList<>();
    private LayoutInflater topicInflater;

    public TopicAdapter(Context context, ArrayList<String> topicList){
        topicInflater = LayoutInflater.from(context);
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View topicItemView = topicInflater.inflate(R.layout.topic_item_layout, parent, false);
        return new TopicViewHolder(topicItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.TopicViewHolder holder, int position) {
        String current = topicList.get(position);
        holder.topicTextView.setText(current);
        holder.topicTextItemView.setText("Cliquer pour avoir les contenus de "+ current);
        holder.topicImageItemView.setImageResource(R.drawable.edik_content);
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView topicTextItemView;
        ImageButton topicImageItemView;
        TextView topicTextView;
        TopicAdapter topicAdapter;
        ProgressBar progressBar;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference rootContentTopic = storage.getReference().child("/Edik Content");

        File rootPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File contentDirectory = new File(rootPath+"/Content/.content");

        public TopicViewHolder(@NonNull View itemView, TopicAdapter topicAdapter) {
            super(itemView);
            topicTextItemView = (TextView) itemView.findViewById(R.id.topic_text_item);
            topicTextView = (TextView) itemView.findViewById(R.id.topic_text);
            topicImageItemView = (ImageButton) itemView.findViewById(R.id.topic_img_item);
            progressBar = itemView.findViewById(R.id.progressbar);
            this.topicAdapter = topicAdapter;

            topicTextItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            progressBar.setVisibility(View.VISIBLE);
            TextView text = v.getRootView().findViewById(R.id.header_topic_text_view);
            String partOne = text.getText().toString().substring(28);
            String partTwo = topicTextView.getText().toString();
            final String topicPath = "/"+partOne.replaceFirst("-","/")+"/"+partTwo;
            Log.d("pathTopic", topicPath);

            StorageReference topicReference = rootContentTopic.child(topicPath);

            contentDirectory.mkdirs();
            final File topicDirectory = new File(contentDirectory+topicPath);
            topicDirectory.mkdirs();

            final Intent successIntent = new Intent(v.getContext(), ContentViewer.class);
            successIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);

            topicReference.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            int size = listResult.getItems().size();

                            successIntent.putExtra("path", topicPath+"/index.html");
                            int itemsNumber = 0;
                            for (StorageReference item : listResult.getItems()) {
                                itemsNumber++;
                            File file = new File(topicDirectory +"/"+ item.getName());
                                Log.d("inTopic", file.getName());
                            if(file.exists() && file.length() != 0L){
                                if(itemsNumber == size){
                                    progressBar.setVisibility(View.INVISIBLE);
                                   v.getContext().startActivity(successIntent);
                                }else{
                                   continue;
                                }
                            }else{
                                    try {
                                        file.createNewFile();
                                        downloadContents(item, file, size, itemsNumber, successIntent, v, progressBar);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                           }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Uh-oh, an error occurred!
                        }
                    });
        }

        private void downloadContents(StorageReference storageReference, File file, final int resultItemsSize, final int itemsNumber, final Intent intent, final View view, final ProgressBar progressBar) throws IOException {
            //StorageReference gsReference = storage.getReferenceFromUrl("rootUrl" + storageReference.getPath());
            //final Intent successIntent = new Intent(this, PdfViewer.class);
            //final Intent failureIntent = new Intent(this, Home.class);

            //Toast.makeText(getApplicationContext(), "Downloading ...", Toast.LENGTH_LONG).show();
        /*File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File content = new File(path+"/Content");
        final File test = new File(content + "/.content");
        final File file = new File(test + storageReference.getPath());
        file.createNewFile();*/

            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(itemView.getContext(), "Downloading...", Toast.LENGTH_LONG).show();
                    if( itemsNumber == resultItemsSize){
                      Toast.makeText(itemView.getContext(), "Download complete", Toast.LENGTH_LONG).show();
                      progressBar.setVisibility(View.INVISIBLE);
                      view.getContext().startActivity(intent);
                        }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    exception.printStackTrace();
                   // startActivity(failureIntent);
                }
            });

        }
    }
}
