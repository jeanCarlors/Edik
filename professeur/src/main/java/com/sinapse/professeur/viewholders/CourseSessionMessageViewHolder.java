package com.sinapse.professeur.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;

public class CourseSessionMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView tvFrom;
    public TextView tvContent;
    public TextView tvDate;
    public ImageView imgType;
    public VideoView videoView;
    public ProgressBar progressBar;
    public CourseSessionMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        tvFrom = itemView.findViewById(R.id.tv_from);
        tvContent = itemView.findViewById(R.id.tv_content);
        tvDate = itemView.findViewById(R.id.tv_date);
        imgType = itemView.findViewById(R.id.img_type);
        videoView = itemView.findViewById(R.id.video_video);
        progressBar = itemView.findViewById(R.id.progressbar);
    }
}
