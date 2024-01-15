package com.example.jec;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView notice , download ;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        notice = itemView.findViewById(R.id.notice);
        download = itemView.findViewById(R.id.Download);
    }

}
