package com.example.harsha.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by harsha on 28/01/17.
 */
public class HomeActivity extends AppCompatActivity{

    TextView nameToDisplayTV, soucres_tv, category_tv, sortBy_tv, errorMsg_tv;
    Spinner sourcesDropDown, categoryDropDown, sortByDropDown;
    String nameToDisplay = "", sourceName = "", errorMsg = "", emailLoggedIN, categoryName = "", sortByName = "";
    Button submitBtn;
    ImageButton refreshBtn;
    ProgressBar progressBar;
    JSONArray sourcesArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nameToDisplayTV = (TextView) findViewById(R.id.welcome_text);
        soucres_tv = (TextView) findViewById(R.id.soucres_tv);
        category_tv = (TextView) findViewById(R.id.cateogry_tv);
        sortBy_tv = (TextView) findViewById(R.id.sortBy_tv);
        errorMsg_tv = (TextView) findViewById(R.id.errorMsg);

        sourcesDropDown = (Spinner) findViewById(R.id.sources_drop_down);
        categoryDropDown = (Spinner) findViewById(R.id.category_drop_down);
        sortByDropDown = (Spinner) findViewById(R.id.sort_by_drop_down);

        submitBtn = (Button) findViewById(R.id.submit_btn);
        refreshBtn = (ImageButton) findViewById(R.id.refreshButton);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        refreshBtn.setVisibility(View.GONE);

        nameToDisplay = getIntent().getStringExtra("username");
        nameToDisplayTV.setText("Hey "+ nameToDisplay);

        errorMsg = getIntent().getStringExtra("errorMsg");

        if(errorMsg!=null){
            Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
        } else{
            errorMsg_tv.setText("");
        }

        //Building Categories
        List<String> categoryList = new ArrayList<String>();
        categoryList.add("Business");
        categoryList.add("Entertainment");
        categoryList.add("Gaming");
        categoryList.add("General");
        categoryList.add("Music ");
        categoryList.add("Science and Nature");
        categoryList.add("Sports");
        categoryList.add("Technology");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,categoryList);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoryDropDown.setAdapter(categoryAdapter);

        //Building SortByList
        List<String> sortByList = new ArrayList<String>();
        sortByList.add("Top");
        sortByList.add("Popular");
        sortByList.add("Latest");

        ArrayAdapter<String> sortByAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,sortByList);
        sortByAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sortByDropDown.setAdapter(sortByAdapter);

        //Checking Internet connection
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            isConnected = networkInfo.isConnectedOrConnecting();
        }

        //Getting Sources List
        GetSources getSources = new GetSources();
        if(isConnected){
            getSources.execute();
        } else{
            soucres_tv.setVisibility(View.GONE);
            sourcesDropDown.setVisibility(View.GONE);
            category_tv.setVisibility(View.GONE);
            categoryDropDown.setVisibility(View.GONE);
            sortBy_tv.setVisibility(View.GONE);
            sortByDropDown.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            errorMsg_tv.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            refreshBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                errorMsg_tv.setText("");
                boolean isConnected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null){
                    isConnected = networkInfo.isConnectedOrConnecting();
                }
                if(isConnected){
                    Intent newsFeedActivity = new Intent(getApplicationContext(),NewsFeedActivity.class);
                    String source = sourcesDropDown.getSelectedItem().toString();
                    if(source.contains("(") || source.contains(")")) {
                        int indexOfOpenParenthesis = source.indexOf('(');
                        int indexOfClosedParenthesis = source.indexOf(')');
                        String source_country = source.substring(indexOfOpenParenthesis+1,12);
                        source = source.substring(0,indexOfOpenParenthesis);
                        source = source.concat(source_country);
                        newsFeedActivity.putExtra("source",source.toLowerCase().replace(' ','-'));
                    } else{
                        newsFeedActivity.putExtra("source",source.toLowerCase().replace(' ','-'));
                    }
                    newsFeedActivity.putExtra("category",categoryDropDown.getSelectedItem().toString().toLowerCase().replace(' ','-'));
                    newsFeedActivity.putExtra("sortBy",sortByDropDown.getSelectedItem().toString().toLowerCase());
                    startActivity(newsFeedActivity);
                } else{
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeActivityIntent = new Intent(getApplicationContext(),HomeActivity.class);
                homeActivityIntent.putExtra("username",nameToDisplay);
                startActivity(homeActivityIntent);
                finish();
            }
        });
    }

    private void buildSources(List<String> sourcesList) {
        ArrayAdapter<String> sourcesAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sourcesList);
        sourcesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sourcesDropDown.setAdapter(sourcesAdapter);
    }

    private class GetSources extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            soucres_tv.setVisibility(View.GONE);
            sourcesDropDown.setVisibility(View.GONE);
            category_tv.setVisibility(View.GONE);
            categoryDropDown.setVisibility(View.GONE);
            sortBy_tv.setVisibility(View.GONE);
            sortByDropDown.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            errorMsg_tv.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://newsapi.org/v1/sources?language=en");
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
            progressBar.setVisibility(View.GONE);
            soucres_tv.setVisibility(View.VISIBLE);
            sourcesDropDown.setVisibility(View.VISIBLE);
            category_tv.setVisibility(View.VISIBLE);
            categoryDropDown.setVisibility(View.VISIBLE);
            sortBy_tv.setVisibility(View.VISIBLE);
            sortByDropDown.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
            errorMsg_tv.setVisibility(View.VISIBLE);
            List<String> sourcesList = new ArrayList<String>();
            try {
                JSONObject sourcesJsonObj = new JSONObject(response);
                JSONArray sourcesArray = sourcesJsonObj.optJSONArray("sources");

                System.out.println("Source Array :: " + sourcesArray);
                for (int i = 0; i < sourcesArray.length(); i++) {
                    JSONObject jSourceObject = sourcesArray.getJSONObject(i);
                    sourceName = jSourceObject.optString("name").toString();
                    sourcesList.add(sourceName);
                }
                buildSources(sourcesList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
