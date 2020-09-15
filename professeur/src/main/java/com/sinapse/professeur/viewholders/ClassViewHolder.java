package com.sinapse.professeur.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sinapse.professeur.R;

import org.w3c.dom.Text;

public class ClassViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView subTitle;

    public ClassViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subtitle);
    }
}
