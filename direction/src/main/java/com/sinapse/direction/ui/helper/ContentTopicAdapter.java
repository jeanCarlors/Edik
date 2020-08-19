package com.sinapse.direction.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.sinapse.direction.R;
import com.sinapse.direction.ui.Models.Topic;

import java.util.ArrayList;
import java.util.List;

public class ContentTopicAdapter extends RecyclerView.Adapter<ContentTopicViewHolder> {

    private List<StorageReference> topicList = new ArrayList<>();
    private LayoutInflater topicInflater;
    private Context context;

    public ContentTopicAdapter(Context context, List<StorageReference> topicList){
        topicInflater = LayoutInflater.from(context);
        this.topicList = topicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContentTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View freeContentItemView = topicInflater.inflate(R.layout.free_content_item_layout, parent, false);
        return new ContentTopicViewHolder(freeContentItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContentTopicViewHolder holder, int position) {
        StorageReference current = topicList.get(position);

        holder.bind(current);
    }



    @Override
    public int getItemCount() {
        return topicList.size();
    }
}
