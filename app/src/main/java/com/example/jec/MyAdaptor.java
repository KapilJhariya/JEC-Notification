package com.example.jec;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    List<Item> items;
    public MyAdaptor(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent,
                false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.notice.setText(items.get(position).getName());
        holder.download.setText(items.get(position).getDownload());


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handle the URL click event here
                String url = "https://www.jecjabalpur.ac" +
                        ".in/"
                        + holder.download.getText();
                Log.d("LINK",url);
                Log.d("LINK", String.valueOf(context));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType( Uri.parse(url), "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                v.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}
