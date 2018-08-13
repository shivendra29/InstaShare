package com.parse.instashare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

       final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);



        Intent intent = getIntent();
        String active_user = intent.getStringExtra("username").substring(0,1).toUpperCase()+intent.getStringExtra("username").substring(1);

        setTitle(active_user + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

        query.whereEqualTo("username",active_user);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){

                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                                        imageView.setPadding(0,20,0,20);

                                        imageView.setImageBitmap(bitmap);
                                        linearLayout.addView(imageView);
                                        Log.d("Info","Success");
                                    }
                                }
                            });
                        }
                    }
                }else{
                    Log.d("Info",e.getMessage());
                }
            }
        });




    }
}
