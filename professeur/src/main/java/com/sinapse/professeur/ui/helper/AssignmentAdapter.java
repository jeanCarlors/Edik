package com.sinapse.professeur.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.AssignmentDetails;
import com.sinapse.professeur.ui.CourseProcessing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentListViewHolder> {
    private List<String> assignmentList;
    private LayoutInflater assignmentInflater;

    public AssignmentAdapter(Context context, ArrayList<String> assignmentList){
        assignmentInflater = LayoutInflater.from(context);
        this.assignmentList = assignmentList;
    }
    @NonNull
    @Override
    public AssignmentAdapter.AssignmentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aItemView = assignmentInflater.inflate(R.layout.assignments_item_layout, parent, false);
        return new AssignmentAdapter.AssignmentListViewHolder(aItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentAdapter.AssignmentListViewHolder holder, int position) {
        final String sCurrent = assignmentList.get(position);
        String localDateTimeString = sCurrent.split("%")[0];
        Date date = new Date();
        Log.d("datedate",sCurrent);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        //LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, formatter);
        String subject = sCurrent.split("%")[1];
        Log.d("datedate",date.toString());
        String stringProcessing = subject + " - " + date.toString();
        holder.assigmentBtn.setText(stringProcessing);
        holder.assigmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AssignmentDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("assignmentHeader", sCurrent);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class AssignmentListViewHolder extends RecyclerView.ViewHolder{
        public final Button assigmentBtn;
        final AssignmentAdapter assignmentAdapter;

        public AssignmentListViewHolder(@NonNull View itemView, AssignmentAdapter assignmentAdapter) {
            super(itemView);
            assigmentBtn = itemView.findViewById(R.id.assignment_item_btn);
            this.assignmentAdapter = assignmentAdapter;
            // itemView.setOnClickListener(this);
        }
    }
}
