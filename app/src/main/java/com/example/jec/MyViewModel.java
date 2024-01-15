package com.example.jec;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> htmlContentLiveData = new MutableLiveData<>();

    public LiveData<String> getHtmlContentLiveData() {
        return htmlContentLiveData;
    }


    public void scrapeWebsite(final String url , ArrayList<Item> items , String divv) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Document document = null;
                try {
                    document = Jsoup.connect(url).timeout(100000).get();
                    // Process the document
                } catch (SocketTimeoutException e) {
                    // Handle timeout exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Handle other IO exceptions
                    e.printStackTrace();
                }

//                final String htmlContent = document.html();
                String tableHtml = new String();

                if(divv.equals("1") || divv.equals("2")) {
                    // Select the specific div by its ID
                    Element parentDiv = document.getElementById(divv);


                    if (parentDiv != null) {
                        // Select all child div elements inside the parent div
                        Elements childDivs = parentDiv.select("> div");

                        // Iterate over each child div
                        for (Element childDiv : childDivs) {
                            // Get the text content of the child div
                            String divText = childDiv.text();
                            // Check if the string ends with "Click here to view"
                            if (divText.endsWith("Click Here to View")) {
                                // Remove the substring from the end
                                divText = divText.substring(0, divText.length() - ("Click Here to View").length());
                            }

                            // Get the href attribute if the div contains an <a> tag
                            String href = childDiv.select("a").attr("href");

                            // extracted information
                            Log.d("JECC", "Text: " + divText);
                            Log.d("JECC", "Href: " + href);
                            items.add(new Item(new StringBuilder(divText), href));
                            Log.d("JECC", "------");
                        }
                    } else {
                        Log.d("JECC", "Parent div with ID 'yourDivId' not found");
                    }
                }
                else if (divv.equals("3")) {
                    Log.d("Acadimic","Step 1");
                    Elements rows = document.select("table.imagetable tbody tr");
//                    Log.d("Acadimic",rows.html());
                    for (Element row : rows) { // Start from 1 to skip header row
                        // Extract text from the second column
                        Elements secondColumnElements = row.select("td:nth-child(2) a");
                        if (!secondColumnElements.isEmpty()) {
                            String text = secondColumnElements.text();
                            String href = secondColumnElements.attr("href");

//                            rows.next();
                            Log.d("Acadimic", "1: " + text + " 2: " + href);
                            items.add(new Item(new StringBuilder(text),
                                     href.substring(3)));

//                        tableRows.add(row);
                        }
                    }
                }else{

                    }

                // Update LiveData with the HTML content on the main thread
                String finalTableHtml = tableHtml;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        htmlContentLiveData.setValue(finalTableHtml);
                    }
                });
            }
        }).start();
    }
}
