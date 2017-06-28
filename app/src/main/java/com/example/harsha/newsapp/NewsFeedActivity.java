package com.example.harsha.newsapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by harsha on 02/02/17.
 */
public class NewsFeedActivity extends AppCompatActivity {

    String source="", category="", sortBy="", title ="", description = "", imageURI = "", url = "";
    ArrayList<ArticlesData> articlesData;
    CustomAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);

        listView = (ListView) findViewById(R.id.list_view_articles);

        source = getIntent().getStringExtra("source");
        category = getIntent().getStringExtra("category");
        sortBy = getIntent().getStringExtra("sortBy");

        GetArticles getArticles = new GetArticles();
        getArticles.execute();
    }

    private void buildArticles(ArrayList<ArticlesData> articlesData) {
        adapter = new CustomAdapter(articlesData, getApplicationContext());
        listView.setAdapter((ListAdapter) adapter);
    }

    private class GetArticles extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source="+source+"&sortBy="+sortBy+"&category="+category+"&apiKey=108740dbd21e433b842857ca7cfa3aa4");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                articlesData = new ArrayList<>();
                if (CommonUtils.isNull(response)) {
                    Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                    homeIntent.putExtra("errorMsg","The news source you've selected isn't available with sort type you have selected.");
                    homeIntent.putExtra("username",getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("userName",""));
                    startActivity(homeIntent);
                } else{
                    JSONObject sourcesJsonObj = new JSONObject(response);
                    if (!CommonUtils.isNull(sourcesJsonObj)) {
                        JSONArray jsonArray = sourcesJsonObj.optJSONArray("articles");
                        if (!CommonUtils.isNull(jsonArray)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                title = jsonObject.optString("title").toString();
                                description = jsonObject.optString("description").toString();
                                imageURI = jsonObject.optString("urlToImage").toString();
                                url = jsonObject.optString("url").toString();

                                articlesData.add(new ArticlesData(title, description, imageURI, url));
                            }
                            buildArticles(articlesData);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
