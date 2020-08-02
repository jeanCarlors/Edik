package com.sinapse.eleve.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
import com.sinapse.eleve.R;

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
    public void onBindViewHolder(@NonNull final FreeContentAdapter.FreeContentViewHolder holder, int position) {
        String current = freeContentList.get(position);
        holder.freeContentTextView.setText(current);

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   onClickMenu(view.getContext(), holder.buttonViewOption, holder);
            }
        });

    }



    public void onClickMenu(Context context, View view, final FreeContentAdapter.FreeContentViewHolder holder) {

        //creating a popup menu
        PopupMenu popup = new PopupMenu(context, view);
        //inflating menu from xml resource
        popup.inflate(R.menu.content_recycler_view_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update:
                        onUpdateBtnClicked(holder.freeContentTextView);
                        break;
                    case R.id.delete:
                        onDeleteBtnClicked(holder.freeContentTextView);
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();

    }

    private void onDeleteBtnClicked(TextView textView){
        String[] topicPathBeginArray =  textView.getText().toString().split("-");
        final  String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");
        Log.d("pathTopicBegin", topicPathBegin);

        File rootPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File contentDirectory = new File(rootPath + "/Content/.content/free content/" + topicPathBegin + textView.getText().toString());
        if(contentDirectory.exists()){
            for (File child : contentDirectory.listFiles()){
                child.delete();
            }
            Toast.makeText(textView.getContext(), "Document supprime", Toast.LENGTH_LONG).show();
            contentDirectory.delete();
        }else{
            Toast.makeText(textView.getContext(), "Ce document n'existe pas", Toast.LENGTH_LONG).show();
        }
    }

    private void onUpdateBtnClicked(final TextView textView){
        onDeleteBtnClicked(textView);

        String[] topicPathBeginArray =  textView.getText().toString().split("-");
        final  String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");


        ProgressBar progressBar = textView.getRootView().findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();
        StorageReference rootContentTopic = storage.getReference().child("/Edik Content/Free Content");
        File rootPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File contentDirectory = new File(rootPath + "/Content/.content/free content");
        contentDirectory.mkdirs();

        final String topicPath = textView.getText().toString();
        final StorageReference topicReference = rootContentTopic.child(topicPath);

        final File topicDirectory = new File(contentDirectory + "/" + topicPathBegin + topicPath);
        topicDirectory.mkdirs();

        topicReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            File file = new File(topicDirectory +"/"+ item.getName());
                            try {
                                    file.createNewFile();
                                    downloadUpdateContents(item, file, textView);
                                } catch (IOException e) {
                                    e.printStackTrace();
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
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(textView.getContext(), "Mis a jour du document ...", Toast.LENGTH_LONG).show();

    }

    private void downloadUpdateContents(StorageReference storageReference, File file, final TextView textView){
        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(textView.getContext(), "Downloading...", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
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
        TextView buttonViewOption;

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
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            this.freeContentAdapter = freeContentAdapter;

            freeContentTextItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            progressBar.setVisibility(View.VISIBLE);
            final String topicPath = freeContentTextView.getText().toString();
            String[] topicPathBeginArray =  topicPath.split("-");
            final  String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");
            Log.d("pathTopicBegin", topicPathBegin);
            Log.d("pathTopic", topicPathBegin + topicPath);

            final StorageReference topicReference = rootContentTopic.child(topicPath);

            contentDirectory.mkdirs();
            final File topicDirectory = new File(contentDirectory + "/" + topicPathBegin + topicPath);
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
                            successIntent.putExtra("path", "/free content/"+ topicPathBegin + topicPath+"/index.html");
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
                }
            });

        }
    }
}
