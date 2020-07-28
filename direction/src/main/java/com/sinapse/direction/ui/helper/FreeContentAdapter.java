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

public class FreeContentAdapter extends RecyclerView.Adapter<FreeContentAdapter.FreeContentViewHolder> {
    private List<String> freeContentList;
    private LayoutInflater freeContentInflater;

    public FreeContentAdapter(Context context, ArrayList<String> freeContentList) {
        freeContentInflater = LayoutInflater.from(context);
        this.freeContentList = freeContentList;
    }


    @NonNull
    @Override
    public FreeContentAdapter.FreeContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View freeContentItemView = freeContentInflater.inflate(R.layout.free_content_item_layout, parent, false);
        return new FreeContentViewHolder(freeContentItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeContentAdapter.FreeContentViewHolder holder, int position) {
        String current = freeContentList.get(position);
        holder.freeContentTextView.setText(current);
        //holder.freeContentTextItemView.setText("Cliquer pour avoir les contenus de "+ current);
        //holder.freeContentImageItemView.setImageResource(R.drawable.edik_content);
    }

    @Override
    public int getItemCount() {
        return freeContentList.size();
    }

    public class FreeContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView freeContentTextItemView;
        ImageButton freeContentImageItemView;
        TextView freeContentTextView;
        ProgressBar progressBar;
        FreeContentAdapter freeContentAdapter;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference rootContentTopic = storage.getReference().child("/Edik Content/Free Content");

        File rootPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File contentDirectory = new File(rootPath + "/Content/.content/free content");

        public FreeContentViewHolder(@NonNull View itemView, FreeContentAdapter freeContentAdapter) {
            super(itemView);
            freeContentTextItemView = (TextView) itemView.findViewById(R.id.free_content_text_item);
            freeContentTextView = (TextView) itemView.findViewById(R.id.free_content_text);
            freeContentImageItemView = (ImageButton) itemView.findViewById(R.id.free_content_img_item);
            progressBar = itemView.findViewById(R.id.progressbar);
            this.freeContentAdapter = freeContentAdapter;

            freeContentTextItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            progressBar.setVisibility(View.VISIBLE);
            final String topicPath = freeContentTextView.getText().toString();
            Log.d("pathTopic", "/NS I/"+ topicPath);

            final StorageReference topicReference = rootContentTopic.child("NS I/" + topicPath);

            contentDirectory.mkdirs();
            final File topicDirectory = new File(contentDirectory +"/NS I/"+ topicPath);
            topicDirectory.mkdirs();

            final Intent successIntent = new Intent(v.getContext(), ContentViewer.class);
            successIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);

            topicReference.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            int size = listResult.getItems().size();
                            //Log.d("free content",topicReference.getPath());
                            Log.d("free content", topicDirectory.getPath());
                            successIntent.putExtra("path", "/free content/NS I/" +topicPath+"/index.html");
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
