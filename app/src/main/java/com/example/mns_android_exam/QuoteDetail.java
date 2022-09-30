package com.example.mns_android_exam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuoteDetail extends AppCompatActivity {

    ImageView ivAnimePoster;
    Button btnSave;
    private FileOutputStream fileOutputStream;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_detail);

        ivAnimePoster = findViewById(R.id.ivAnimePoster);
        btnSave = findViewById(R.id.btnSave);

        String animeQuoted = getIntent().getExtras().getString("animeQuoted");
        String formattedAnimeQuoted = animeQuoted.replace(" ","%20");

        Log.d("detail", "detail reached");

        JsonObjectRequest request = new JsonObjectRequest(
                "https://kitsu.io/api/edge/anime?filter[text]="+formattedAnimeQuoted+"&coverImage=tiny",
                resultat -> {
                    try {
                        setUrl(resultat.getJSONArray("data").getJSONObject(1).getJSONObject("attributes").getJSONObject("posterImage").getString("medium"));
                        Glide.with(this).load(getUrl()).into(ivAnimePoster);

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ContextCompat.checkSelfPermission(QuoteDetail.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                    saveImg();
                                }else {
                                    askPermission();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        RequestManager.getInstance(this).addToRequestQueue(request);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(QuoteDetail.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if(requestCode == 100){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImg();
                Toast.makeText(QuoteDetail.this,"Starting download",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(QuoteDetail.this,"Please provide the required permissions",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void saveImg() {

        File dir = new File(Environment.getExternalStorageDirectory(), "MNS-ANDROID-APP-DOWNLOAD");
        if(!dir.exists()) dir.mkdir();

        BitmapDrawable drawable = (BitmapDrawable) ivAnimePoster.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, System.currentTimeMillis()+".jpg");


        try {
            fileOutputStream = new FileOutputStream(file);
            Log.d("save","file output reached");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        Toast.makeText(QuoteDetail.this,"Download from source complete", Toast.LENGTH_SHORT).show();

        try {
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent saveIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            saveIntent.setData(Uri.fromFile(file));
            sendBroadcast(saveIntent);

            Toast.makeText(QuoteDetail.this,"Image saved on device", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}