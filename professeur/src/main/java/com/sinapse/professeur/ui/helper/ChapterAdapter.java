package com.sinapse.professeur.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;
import com.sinapse.professeur.ui.ContentDetails;
//import com.sinapse.professeur.ui.ContentDetails;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    private List<String> chapterList = new ArrayList<>();
    private LayoutInflater chapterInflater;

    public ChapterAdapter(Context context, ArrayList<String> chapterList){
        chapterInflater = LayoutInflater.from(context);
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ChapterAdapter.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chapterItemView = chapterInflater.inflate(R.layout.chapter_item_layout, parent, false);
        return new ChapterViewHolder(chapterItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder holder, int position) {
        String current = chapterList.get(position);
        holder.chapterTextView.setText(current);
        holder.chapterTextItemView.setText("Cliquer pour avoir les contenus de "+current);
        holder.chapterImageItemView.setImageResource(R.drawable.edik_content);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView chapterTextItemView;
        public final ImageButton chapterImageItemView;
        public final TextView chapterTextView;
        final ChapterAdapter chapterAdapter;

        public ChapterViewHolder(@NonNull View itemView, ChapterAdapter chapterAdapter) {
            super(itemView);
            chapterTextItemView = (TextView) itemView.findViewById(R.id.chapter_text_item);
            chapterTextView = (TextView) itemView.findViewById(R.id.chapter_text);
            chapterImageItemView = (ImageButton) itemView.findViewById(R.id.chapter_img_item);
            this.chapterAdapter = chapterAdapter;

            chapterTextItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ContentDetails.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            Log.d("testTrois", chapterTextView.getText().toString());
            TextView text = v.getRootView().findViewById(R.id.header_chapter_text_view);
            String partOne = text.getText().toString().substring(28);
            String partTwo = chapterTextView.getText().toString();
            intent.putExtra("chapter", "/"+partOne+"/"+partTwo);
            v.getContext().startActivity(intent);
        }
    }
}
