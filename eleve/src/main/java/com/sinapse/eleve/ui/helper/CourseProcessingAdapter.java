package com.sinapse.eleve.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.eleve.R;

import java.util.List;

public class CourseProcessingAdapter extends RecyclerView.Adapter<CourseProcessingAdapter.CourseProcessingViewHolder> {
    private List<String> courseContentList;
    private LayoutInflater courseContentInflater;

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
        String sCurrent = courseContentList.get(position);
        String test = sCurrent.split(">")[0];
        String owner = test.split("%")[1];
        holder.owner.setText(owner);
        holder.content.setText(sCurrent.split(">")[1]);
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
}
