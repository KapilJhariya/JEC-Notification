package com.example.jec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class Acadimic extends AppCompatActivity {

    private MyViewModel viewModel;
    ArrayList<Item> items3 = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acadimic);

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
        String websiteUrl = "https://www.jecjabalpur.ac.in/academics/academiccalenders.aspx";

        viewModel.scrapeWebsite(websiteUrl ,items3 , "3");
    }
    ProgressBar progressBar;
    private void updateUI(String htmlContent) {

        RecyclerView recyclerView3;
        recyclerView3 = findViewById(R.id.recyclerView3);
        recyclerView3.setVisibility(View.VISIBLE);

        Collections.reverse(items3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView3.setAdapter(new MyAdaptor(getApplicationContext(),items3));

        if(recyclerView3.getAdapter().getItemCount()>0){
            //After content is loaded , progress bar will disappear
            progressBar = findViewById(R.id.progressBar3);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}