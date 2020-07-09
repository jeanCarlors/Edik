package com.sinapse.direction.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.helper.PdfViewer;
import com.sinapse.direction.ui.helper.TopicAdapter;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentDetails extends AppCompatActivity {
    private RecyclerView topicRecyclerView;
    private TopicAdapter topicAdapter;
    private List<String> topicList = new ArrayList<>();

    private Intent intent;
    private Bundle bundle;

    private static final int PICK_PDF_FILE = 2;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference rootContentTopic = storage.getReference().child("/Edik Content");
    private String rootUrl = "gs://edik-6adf5.appspot.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);
        intent = getIntent();
        bundle = intent.getExtras();
        TextView topicTextView = findViewById(R.id.header_topic_text_view);
        String root = "/";
        String pathTopic = "/" + (bundle.getString("chapter").replace("-", "/"));
        Log.d("test4",pathTopic);
        topicTextView.setText("LES CONTENUS DE SINAPSE DU " + bundle.getString("chapter"));
        openContentTopic(pathTopic);

    }

    public void openContentTopic(String topic){
        rootContentTopic.child(topic).listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        Log.d("test", String.valueOf(listResult.getPrefixes().size()));
                        for (StorageReference item : listResult.getPrefixes()) {
                            topicList.add(item.getName());
                        }
                        topicRecyclerView = findViewById(R.id.topic_recycler_view);
                        topicAdapter = new TopicAdapter(getApplicationContext(), (ArrayList<String>) topicList);
                        topicRecyclerView.setAdapter(topicAdapter);
                        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Root content subject", "Connexion failed"); // Uh-oh, an error occurred!
                    }
                });
    }

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openContent(View view) throws IOException {


        StorageReference gsReference = storage.getReferenceFromUrl("gs://edik-6adf5.appspot.com/index.html");

        /*final Intent successIntent = new Intent(this, PdfViewer.class);
        final String[] contentIndexPath;
        final Intent failureIntent = new Intent(this, Home.class);
        final File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        final File contentDirectory = new File(path+"/Content/.content");
        contentDirectory.mkdirs();
        File filetest = new File(contentDirectory + "/Edik Content/NS I/index.html");
        if(filetest.exists()){
            successIntent.putExtra("path", "/Edik Content/NS I/index.html");
            startActivity(successIntent);
        }*/

        /////////////////////////////////////////////
        rootContentTopic.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        int size = listResult.getItems().size();
                        int itemsNumber = 0;
                        for (StorageReference item : listResult.getPrefixes()) {
                            Log.d("Folder",  item.getName());
                            /*itemsNumber++;
                            Log.d("test", rootUrl + item.getPath());
                            Log.d("test", rootUrl + item.getPath().substring(0, item.getPath().lastIndexOf("/")));

                                File subDirectory = new File(contentDirectory + item.getPath().substring(0, item.getPath().lastIndexOf("/")));
                                 subDirectory.mkdirs();
                                final File file = new File(contentDirectory + item.getPath());
                                if(item.getName().contains(".html")){
                                    successIntent.putExtra("path", item.getPath());
                                }

                                if(file.exists()){
                                    if(itemsNumber == size){
                                        startActivity(successIntent);
                                    }else{
                                            continue;
                                    }
                                }else{
                                    try {
                                        file.createNewFile();
                                        //file.createNewFile();
                                        //Log.d("directory", contentDirectory + item.getPath().substring(0, item.getPath().lastIndexOf("/")));
                                        downloadContents(item, file, size, itemsNumber, successIntent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }*/
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        ////////////////////////////////////////////
        //startActivity(successIntent);
        /*gsReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                try {
                    downloadVideo1();
                } catch (IOException e) {
                  e.printStackTrace();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //exception.printStackTrace();
                startActivity(failureIntent);
            }
        });*/
    }

    private void downloadContents(StorageReference storageReference, File file, final int resultItemsSize, final int itemsNumber, final Intent intent) throws IOException {
        StorageReference gsReference = storage.getReferenceFromUrl(rootUrl + storageReference.getPath());
        final Intent successIntent = new Intent(this, PdfViewer.class);
        final Intent failureIntent = new Intent(this, Home.class);

        Toast.makeText(getApplicationContext(), "Downloading ...", Toast.LENGTH_LONG).show();
        /*File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File content = new File(path+"/Content");
        final File test = new File(content + "/.content");
        final File file = new File(test + storageReference.getPath());
        file.createNewFile();*/

        gsReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("breakPoint", "Download finished");
                if( itemsNumber == resultItemsSize)
                    Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_LONG).show();
                    startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                startActivity(failureIntent);
            }
        });

    }

    private void downloadVideo2() throws IOException {
        StorageReference gsReference = storage.getReferenceFromUrl("gs://edik-6adf5.appspot.com/test2.mp4");
        final Intent successIntent = new Intent(this, PdfViewer.class);
        final Intent failureIntent = new Intent(this, Home.class);

        Toast.makeText(getApplicationContext(), "Downloading video2...", Toast.LENGTH_LONG).show();
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        File content = new File(path+"/Content");
        final File test = new File(content + "/.contentTest");
        final File file = new File(test + "/video2.mp4");
        file.createNewFile();

        gsReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                startActivity(successIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                startActivity(failureIntent);
            }
        });

    }


}
