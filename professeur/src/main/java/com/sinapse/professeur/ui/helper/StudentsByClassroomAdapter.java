package com.sinapse.professeur.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;

import java.util.ArrayList;
import java.util.List;

public class StudentsByClassroomAdapter extends RecyclerView.Adapter<StudentsByClassroomAdapter.StudentListViewHolder> {
    private List<String> studentList;
    private LayoutInflater studentInflater;

    public StudentsByClassroomAdapter(Context context, ArrayList<String> studentList){
        studentInflater = LayoutInflater.from(context);
        this.studentList = studentList;
    }
    @NonNull
    @Override
    public StudentsByClassroomAdapter.StudentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sItemView = studentInflater.inflate(R.layout.student_item_by_classroom_layout, parent, false);
        return new StudentsByClassroomAdapter.StudentListViewHolder(sItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsByClassroomAdapter.StudentListViewHolder holder, int position) {
        String sCurrent = studentList.get(position);
        holder.studentBtn.setText((position + 1)+" - "+sCurrent);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class StudentListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final Button studentBtn;
        final StudentsByClassroomAdapter studentsByClassroomAdapter;

        public StudentListViewHolder(@NonNull View itemView, StudentsByClassroomAdapter studentsByClassroomAdapter) {
            super(itemView);
            studentBtn = itemView.findViewById(R.id.student_item_btn);
            this.studentsByClassroomAdapter = studentsByClassroomAdapter;

            studentBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
