package com.example.jec;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private MyViewModel viewModel;
    private FloatingActionButton fab;
    ProgressBar progressBar;
    private Switch mySwitch;
    TextView textView;

    ArrayList<Item> items1 = new ArrayList<Item>();
    ArrayList<Item> items2 = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final String[] div = new String[1];
        if (NetworkUtils.isInternetAvailable(getApplicationContext())) {
            // Internet is available
            textView = findViewById(R.id.textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String urlToOpen = "https://www.linkedin.com/in/kapil-jhariya/";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlToOpen));

                    startActivity(intent);
                    return false;
                }
            });

            fab = findViewById(R.id.fab);
            Intent intent = new Intent(this,Acadimic.class);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   startActivity(intent);
                }
            });

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission
                Log.d("NOTIFY","NO PERMISSION");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            } else {
                // Permission is already granted, you can now use notifications
                Log.d("NOTIFY","YES PERMISSION");

            }

            //check for new notice by periodic work
            scheduleWork();

            // Get an instance of the ViewModel
            viewModel = new ViewModelProvider(this).get(MyViewModel.class);

            // Observe LiveData for HTML content changes
            viewModel.getHtmlContentLiveData().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String htmlContent) {

                    // Update your UI with the HTML content
                    updateUI(htmlContent);
                }
            });

            // web scraping URL
            String websiteUrl = "https://www.jecjabalpur.ac.in/index.aspx";

            viewModel.scrapeWebsite(websiteUrl ,items1 , "2");
            viewModel.scrapeWebsite(websiteUrl ,items2 , "1");

        } else {
            // Internet is not available, show a message to refresh

            Intent intent = new Intent(this,MainActivity2.class);
            Toast.makeText(this, "CHECK INTERNET CONNECTION", Toast.LENGTH_SHORT).show();

            startActivity(intent);

        }
    }
    private void scheduleWork() {
        // Create a periodic work request to run every 15 minutes
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                MyWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build();

        // Enqueue the work request with WorkManager
        WorkManager.getInstance(this).enqueue(workRequest);
    }
    private void updateUI(String htmlContent) {
        mySwitch = findViewById(R.id.switch1);
        RecyclerView recyclerView1,recyclerView2;
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView1 = findViewById(R.id.recyclerView);

        if(!mySwitch.isChecked()) {
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.INVISIBLE);
        } else{
            recyclerView1.setVisibility(View.INVISIBLE);
            recyclerView2.setVisibility(View.VISIBLE);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Check the position of the switch

                if (isChecked) {
                    // Switch is in the checked (on) position
                    Log.d("Switch","ON");
                    recyclerView1.setVisibility(View.INVISIBLE);
                    recyclerView2.setVisibility(View.VISIBLE);

                } else {
                    // Switch is in the unchecked (off) position
                    Log.d("Switch","OFF");
                    recyclerView2.setVisibility(View.INVISIBLE);
                    recyclerView1.setVisibility(View.VISIBLE);
                }
            }

        });


        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView1.setAdapter(new MyAdaptor(getApplicationContext(),items1));

        if(recyclerView1.getAdapter().getItemCount()>0){
            //After content is loaded , progress bar will disappear
            progressBar = findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.INVISIBLE);
        }

        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView2.setAdapter(new MyAdaptor(getApplicationContext(),items2));

        if(recyclerView2.getAdapter().getItemCount()>0){
            //After content is loaded , progress bar will disappear
            progressBar = findViewById(R.id.progressBar2);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}



