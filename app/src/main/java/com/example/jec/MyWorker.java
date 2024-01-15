package com.example.jec;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Perform background task

        Log.d("BACKTASK", "step 1 done");

        try {
            // Perform web scraping here
            String url = "https://www.jecjabalpur.ac.in/index.aspx";
            Document document = Jsoup.connect(url).get();

            // Extract information from the HTML document
            Element divElement = document.getElementById("1");

            if (divElement != null) {
                // Select the first div inside the outer div
                Element firstInnerDiv = divElement.firstElementChild();

                // Check if an inner div is found
                if (firstInnerDiv != null) {
                    // Extract the content of the first inner div
                    String content = firstInnerDiv.text();

                        String fileName = "Important_Notice.txt";
                        String data = content;

                        String existingNotice = readFromInternalStorage(getApplicationContext(),fileName);
                        Log.d("MYFILE",">"+existingNotice+"<"+" , "+">"+data+"<");
                        if (!existingNotice.equals(data)){
                            //If new notice found , send a notification
                            showNotification("Important Notice: ", data,1);
                            Log.d("BACKTASK", "step 5 done");
                            writeToInternalStorage(getApplicationContext(),fileName,data);
                        }else{
//                            showNotification("My Notification", "No New Notice",1);
                        }
                        Log.d("HEHEH","Text content of div: " + content);


                    Log.d("TESTING","Content of the first inner div: " + content);
                } else {
                    Log.d("TESTING","No inner div found inside the outer div.");
                }
            } else {
                Log.d("TESTING","No outer div found.");
            }

            Element divElement2 = document.getElementById("2");
            if (divElement != null) {
                // Select the first div inside the outer div
                Element firstInnerDiv = divElement2.firstElementChild();

                // Check if an inner div is found
                if (firstInnerDiv != null) {
                    // Extract the content of the first inner div
                    String content = firstInnerDiv.text();

                    String fileName2 = "Examination_Notice_Results_Notice.txt";
                    String data = content;

                    String existingNotice = readFromInternalStorage(getApplicationContext(),
                            fileName2);
                    Log.d("MYFILE",">"+existingNotice+"<"+" , "+">"+data+"<");
                    if (!existingNotice.equals(data)){
                        //If new notice found , send a notification
                        showNotification("Examination Notice & Results: ", data,2);
                        Log.d("BACKTASK", "step 5 done");
                        writeToInternalStorage(getApplicationContext(),fileName2,data);
                    }else{
//                        showNotification("My Notification", "No New Notice",2);
                    }
                    Log.d("HEHEH","Text content of div: " + content);


                    Log.d("TESTING","Content of the first inner div: " + content);
                } else {
                    Log.d("TESTING","No inner div found inside the outer div.");
                }
            } else {
                Log.d("TESTING","No outer div found.");
            }

            // Indicate whether the work finished successfully
            return Result.success();
        } catch (IOException e) {
            // Handle exceptions, log errors, etc.
            e.printStackTrace();
//            return Result.failure();
        }

        return Result.success();
    }

    public static void writeToInternalStorage(Context context, String fileName, String data) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(data.getBytes());
            // The file will be created or overwritten if it already exists
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromInternalStorage(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();

        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("");
            }

        } catch (IOException e) {
            // If the file doesn't exist, create an empty file
            writeToInternalStorage(context, fileName, "temp");
            Log.d("MYFILE","new file created");
        }

        return stringBuilder.toString();
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID",
                    "JEC Notices",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Shows 'Examination Notice & Results' and 'Important Notice' " +
                    "latest Updates");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String content , int id)  {

        // Create an Intent that opens your main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Create a notification
        createNotificationChannel(this.getApplicationContext());
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.getApplicationContext(), "CHANNEL_ID").setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent) // Set the PendingIntent here
                .setAutoCancel(true);;
        Log.d("BACKTASK", "step 2 done");




        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        Log.d("BACKTASK", "step 3 done");

//        check permission for notification
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getApplicationContext(), "Please Give Notification Permission ",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        notificationManager.notify(id, builder.build());
        Log.d("BACKTASK","step 4 done");

    }



}
