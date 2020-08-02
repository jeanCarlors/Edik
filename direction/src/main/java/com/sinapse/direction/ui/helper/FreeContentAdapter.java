package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FreeContentAdapter extends RecyclerView.Adapter<FreeContentAdapter.FreeContentViewHolder> {
    private List<String> freeContentList;
    private LayoutInflater freeContentInflater;
    private Context context;

    public FreeContentAdapter(Context context, ArrayList<String> freeContentList) {
        freeContentInflater = LayoutInflater.from(context);
        this.freeContentList = freeContentList;
        this.context = context;
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
        holder.countDocument();


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
                        holder.delete();
                        holder.download();
                        break;
                    case R.id.delete:
                        holder.delete();
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();

    }

    @Override
    public int getItemCount() {
        return freeContentList.size();
    }

    public class FreeContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView freeContentTextItemView;
        ImageView freeContentImageItemView;
        TextView freeContentTextView;
        ProgressBar progressBar;
        FreeContentAdapter freeContentAdapter;
        TextView buttonViewOption;
        TextView txtDownloadStatut;
        TextView txtDownloadCount;
        ImageView imgDownloadStatut;

        int countDownloaded = 0;

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
            freeContentImageItemView = (ImageView) itemView.findViewById(R.id.free_content_img_item);
            progressBar = itemView.findViewById(R.id.progressbar);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);

            txtDownloadStatut = (TextView) itemView.findViewById(R.id.free_content_statut_txt);
            txtDownloadCount = (TextView) itemView.findViewById(R.id.free_content_download_count);
            imgDownloadStatut = (ImageView) itemView.findViewById(R.id.free_content_statut_img);

            this.freeContentAdapter = freeContentAdapter;

            freeContentTextItemView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {
            download();
        }

        public void countDocument() {
            imgDownloadStatut.setVisibility(View.GONE);
            txtDownloadStatut.setVisibility(View.GONE);
            txtDownloadCount.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            final String topicPath = freeContentTextView.getText().toString();

            String[] topicPathBeginArray = topicPath.split("-");
            final String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");
            final File topicDirectory = new File(contentDirectory + "/" + topicPathBegin + topicPath);

            final StorageReference topicReference = rootContentTopic.child(topicPath);
            topicReference.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            int size = listResult.getItems().size();
                            int itemsNumber = 0;
                            for (StorageReference item : listResult.getItems()) {
                                File file = new File(topicDirectory + "/" + item.getName());
                                if (file.exists() && file.length() != 0L) {
                                    itemsNumber++;
                                }
                            }

                            progressBar.setVisibility(View.GONE);
                            updateCountDetails(size, itemsNumber);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Uh-oh, an error occurred!
                            txtDownloadCount.setText("N/A");
                        }
                    });
        }

        private void updateCountDetails(int totalCloud, int totalLocal) {
            //progressBar.setVisibility(View.GONE);

            txtDownloadStatut.setVisibility(View.VISIBLE);
            txtDownloadStatut.setText(totalCloud != totalLocal ? "Télécharger" : "Ouvrir");

            imgDownloadStatut.setVisibility(View.VISIBLE);
            imgDownloadStatut.setImageResource(totalCloud == totalLocal ? R.drawable.ic_baseline_check_24 : R.drawable.ic_baseline_cloud_download_24);

            txtDownloadCount.setVisibility(totalCloud != totalLocal ? View.VISIBLE : View.GONE);
            txtDownloadCount.setText(String.format(Locale.US, "%d/%d", totalLocal, totalCloud));
            Log.d("FREECONTENTADAPTER", String.format(Locale.US, "%s : %d/%d", freeContentTextView.getText().toString(), totalLocal, totalCloud));
        }

        private void delete() {
            String[] topicPathBeginArray = freeContentTextView.getText().toString().split("-");
            final String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");
            Log.d("pathTopicBegin", topicPathBegin);

            File rootPath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File contentDirectory = new File(rootPath + "/Content/.content/free content/" + topicPathBegin + freeContentTextView.getText().toString());
            if (contentDirectory.exists()) {
                for (File child : contentDirectory.listFiles()) {
                    child.delete();
                }
                Toast.makeText(context, "Document supprime", Toast.LENGTH_LONG).show();
                contentDirectory.delete();
            } else {
                Toast.makeText(context, "Ce document n'existe pas", Toast.LENGTH_LONG).show();
            }

            countDocument();
        }

        public void download() {
            progressBar.setVisibility(View.VISIBLE);

            final String topicPath = freeContentTextView.getText().toString();
            String[] topicPathBeginArray = topicPath.split("-");
            final String topicPathBegin = topicPathBeginArray[0].replaceAll(" ", "/").replaceFirst("/", " ");
            Log.d("pathTopicBegin", topicPathBegin);
            Log.d("pathTopic", topicPathBegin + topicPath);

            final StorageReference topicReference = rootContentTopic.child(topicPath);

            contentDirectory.mkdirs();
            final File topicDirectory = new File(contentDirectory + "/" + topicPathBegin + topicPath);
            topicDirectory.mkdirs();

            final Intent successIntent = new Intent(context, ContentViewer.class);
            successIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);

            topicReference.listAll()
                    .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            int size = listResult.getItems().size();
                            //Log.d("free content",topicReference.getPath());
                            Log.d("free content", topicDirectory.getPath());
                            successIntent.putExtra("path", "/free content/" + topicPathBegin + topicPath + "/index.html");

                            int localCount = 0;
                            countDownloaded = 0;

                            for (StorageReference item : listResult.getItems()) {
                                progressBar.setVisibility(View.VISIBLE);

                                File file = new File(topicDirectory + "/" + item.getName());
                                Log.d("inTopic", file.getName());
                                if (file.exists() && file.length() != 0L) {
                                    localCount += 1;
                                    countDownloaded += 1;

                                    updateCountDetails(size, countDownloaded);

                                    if (localCount == size) {
                                        Log.d("LOCAL LOAD", "Load complete");
                                        progressBar.setVisibility(View.INVISIBLE);
                                        context.startActivity(successIntent);
                                    }
                                } else {
                                    try {
                                        file.createNewFile();
                                        downloadContents(item, file, size, successIntent, progressBar);
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


        private void downloadContents(StorageReference storageReference, File file, final int resultItemsSize, final Intent intent, final ProgressBar progressBar) throws IOException {
            storageReference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(!task.isComplete())
                        return;

                    countDownloaded += 1;

                    updateCountDetails(resultItemsSize, countDownloaded);

                    Toast.makeText(itemView.getContext(), "Downloading...", Toast.LENGTH_LONG).show();
                    if (countDownloaded == resultItemsSize) {
                        Toast.makeText(itemView.getContext(), "Download complete", Toast.LENGTH_LONG).show();
                        Log.d("DOWNLOAD", "Download complete");
                        progressBar.setVisibility(View.INVISIBLE);
                        //context.startActivity(intent);
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

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
