package com.sinapse.professeur.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.ContentByChapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>  {
    private List<String> subjectList = new ArrayList<>();
    private LayoutInflater subjectInflater;

    public SubjectAdapter(Context context, ArrayList<String> subjectList){
        subjectInflater = LayoutInflater.from(context);
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View sItemView = subjectInflater.inflate(R.layout.subject_item_layout, parent, false);
        return new SubjectViewHolder(sItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        String sCurrent = subjectList.get(position);
        holder.subjectTextView.setText(sCurrent);
        holder.subjectTextItemView.setText("Cliquer pour avoir les contenus de "+sCurrent);
        holder.subjectImageItemView.setImageResource(R.drawable.edik_content);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }


    public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView subjectTextItemView;
        public final ImageButton subjectImageItemView;
        public final TextView subjectTextView;
        final SubjectAdapter subjectAdapter;

        public SubjectViewHolder(@NonNull View itemView, SubjectAdapter subjectAdapter) {
            super(itemView);
            subjectTextItemView = (TextView) itemView.findViewById(R.id.subject_text_item);
            subjectTextView = (TextView) itemView.findViewById(R.id.subject_text);
            subjectImageItemView = (ImageButton) itemView.findViewById(R.id.subject_img_item);
            this.subjectAdapter = subjectAdapter;

            subjectTextItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ContentByChapter.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            TextView text = v.getRootView().findViewById(R.id.header_subject_text_view);
            String partOne = text.getText().toString().substring(27);
            String partTwo = subjectTextView.getText().toString();
            intent.putExtra("subject", "/"+partOne+"/"+partTwo);
            v.getContext().startActivity(intent);
        }
    }
}
