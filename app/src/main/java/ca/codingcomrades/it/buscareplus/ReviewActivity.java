// Naman Shah , n01392496 , Section RNA
// Aryan Sood , n01393003, Section RNA
// Vishesh Bansal, n01395119, Section RNA
// Jaskirat Singh , N01403975 , Section RNB


package ca.codingcomrades.it.buscareplus;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Single Responsibility Principal
public class ReviewActivity  extends AppCompatActivity {
    FirebaseDatabase database;
    Button submit;
    RatingBar ratingBar;
    TextView model;
    FirebaseAuth fAuth;
    EditText fullName,phone,email,comment;
    Float rating;
    String name,num,emailAddress,Comment;
    Button addnotification;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bindFields();
        String str = android.os.Build.MODEL;
        model.setText(str);
        addnotification = (Button) findViewById(R.id.submitBtn);
        //If the SDK VERSION is newer than Oreo
        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Review", "Notificattion", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        addnotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sendReview();

            }
        });}
    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences prefs = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String port = prefs.getString("port","false");
        String ds = prefs.getString("ds","false");
        if(port.equalsIgnoreCase("true")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
        if(ds.equalsIgnoreCase("true")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void addNotification() {
        String id="id";
        NotificationCompat.Builder review  = new NotificationCompat.Builder(ReviewActivity.this,"Review")
                .setSmallIcon(R.drawable.star_review)
                .setContentTitle(getString(R.string.notification_heading))
                .setContentText(getString(R.string.notification_main))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(ReviewActivity.this);
        managerCompat.notify(1,review.build());
    }


    public void sendReview(){
        if(validate()) {
            database = FirebaseDatabase.getInstance();
            fAuth = FirebaseAuth.getInstance();
            String uid = fAuth.getUid();
            DatabaseReference myRef = database.getReference("FeedBack"+"/"+uid);
            DatabaseReference fullNameChild = myRef.child("Name");
            fullNameChild.setValue(name);
            DatabaseReference emailChild = myRef.child("Email");
            emailChild.setValue(emailAddress);
            DatabaseReference phoneChild = myRef.child("Phone");
            phoneChild.setValue(num);
            DatabaseReference commentChild = myRef.child("Comment");
            commentChild.setValue(Comment);
            DatabaseReference ratingChild = myRef.child("Rating");
            ratingChild.setValue(rating);
            addNotification();
            finish();
        }
    }

    public void bindFields(){
        fullName = findViewById(R.id.fullNameInput);
        phone = findViewById(R.id.phoneNumInput);
        email = findViewById(R.id.emailInput);
        ratingBar =findViewById(R.id.ratingBar);
        comment = findViewById(R.id.commentInput);
        model = findViewById(R.id.model_print);
        submit = findViewById(R.id.submitBtn);
    }
    public boolean validate(){
        getValues();
        if(name.isEmpty() || num.isEmpty() || emailAddress.isEmpty() || Comment.isEmpty() || rating==0){
            return false;
        }
        return true;
    }
    public void getValues(){
        name = fullName.getText().toString();
        num = phone.getText().toString();
        emailAddress = email.getText().toString();
        Comment = comment.getText().toString();
        rating = ratingBar.getRating();
        Log.d("TAG", "getValues: " +rating);

    }
    }


