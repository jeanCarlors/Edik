package com.sinapse.eleve.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.eleve.R;

import java.util.ArrayList;
import java.util.List;

public class TeachersByClassroomAdapter extends RecyclerView.Adapter<TeachersByClassroomAdapter.TeacherListViewHolder> {
    private List<String> teacherList;
    private LayoutInflater teacherInflater;

    public TeachersByClassroomAdapter(Context context, ArrayList<String> teacherList){
        teacherInflater = LayoutInflater.from(context);
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public TeachersByClassroomAdapter.TeacherListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tItemView = teacherInflater.inflate(R.layout.teacher_item_by_classroom_layout, parent, false);
        return new TeachersByClassroomAdapter.TeacherListViewHolder(tItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersByClassroomAdapter.TeacherListViewHolder holder, int position) {
        String sCurrent = teacherList.get(position);
        holder.teacherBtn.setText(sCurrent);
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class TeacherListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final Button teacherBtn;
        final TeachersByClassroomAdapter teachersByClassroomAdapter;

        public TeacherListViewHolder(@NonNull View itemView, TeachersByClassroomAdapter teachersByClassroomAdapter) {
            super(itemView);
            teacherBtn = itemView.findViewById(R.id.teacher_item_btn);
            this.teachersByClassroomAdapter = teachersByClassroomAdapter;

            teacherBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
