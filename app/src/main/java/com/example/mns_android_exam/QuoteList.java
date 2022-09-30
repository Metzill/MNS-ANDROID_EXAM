package com.example.mns_android_exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuoteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_list);

        ArrayList<Quote> quoteList = new ArrayList<>();

        RecyclerView rvQuoteList = findViewById(R.id.rvQuoteList);
        rvQuoteList.setLayoutManager(new LinearLayoutManager(this));

        JsonArrayRequest request = new JsonArrayRequest(
                "https://animechan.vercel.app/api/quotes",
                resultat -> {
                    try {
                        for(int i =0; i < resultat.length(); i++) {
                            JSONObject json = resultat.getJSONObject(i);
                            Quote quote = new Quote(json);
                            quoteList.add(quote);
                        }

                        rvQuoteList.setAdapter(
                                new AdapterQuoteList(
                                        quoteList,
                                        quote -> {
                                            Intent intent = new Intent(
                                                    this,
                                                    QuoteDetail.class);
                                            intent.putExtra("animeQuoted", quote.getAnime());
                                            startActivity(intent);
                                        }));
                        Toast.makeText(
                                this,
                                "Afffichage de " + quoteList.size() + " quote(s)",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );

        RequestManager.getInstance(this).addToRequestQueue(request);

    }
}