package com.sinapse.eleve.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.sinapse.eleve.R;
import com.sinapse.eleve.ui.CourseProcessing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CourseLauncherAdapter extends RecyclerView.Adapter<CourseLauncherAdapter.CourseListViewHolder>{
    private List<String> courseList;
    private LayoutInflater courseInflater;

    public CourseLauncherAdapter(Context context, ArrayList<String> courseList){
        courseInflater = LayoutInflater.from(context);
        this.courseList = courseList;
    }


    @NonNull
    @Override
    public CourseLauncherAdapter.CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cItemView = courseInflater.inflate(R.layout.course_list_item_layout, parent, false);
        return new CourseLauncherAdapter.CourseListViewHolder(cItemView, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CourseLauncherAdapter.CourseListViewHolder holder, int position) {
        final String sCurrent = courseList.get(position);
        String localDateTimeString = sCurrent.split("%")[0];
        Date date = new Date();
        Log.d("datedate",date.toString());
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        //LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, formatter);
        String stringProcessing = sCurrent.split("%")[1] + " - " + date.toString();
        holder.courseBtn.setText(stringProcessing);
        holder.courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CourseProcessing.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("courseHeader", sCurrent);
                    v.getContext().startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class CourseListViewHolder extends RecyclerView.ViewHolder {
        public final Button courseBtn;
        final CourseLauncherAdapter courseLauncherAdapter;

        public CourseListViewHolder(@NonNull View itemView, CourseLauncherAdapter courseLauncherAdapter) {
            super(itemView);
            courseBtn = itemView.findViewById(R.id.course_item_btn);
            this.courseLauncherAdapter = courseLauncherAdapter;
           // itemView.setOnClickListener(this);
        }

    }
}
