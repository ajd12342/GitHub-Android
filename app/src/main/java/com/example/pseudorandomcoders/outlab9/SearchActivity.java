package com.example.pseudorandomcoders.outlab9;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button button =findViewById(R.id.searchButton);
        final TextView search_text=findViewById(R.id.searchField);
        ListView listView=(ListView) findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String query = search_text.getText().toString();
                    String url = "https://api.github.com/search/users?q=" + URLEncoder.encode(query, "UTF-8") + "&sort=repositories";
                    new GetUserInfo().execute(url);
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username=(String)parent.getItemAtPosition(position);
                Intent intent=new Intent(getApplicationContext(),UserActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }
    private class GetUserInfo extends AsyncTask<String,Void,Void>{
        ArrayList<String> outp=new ArrayList<String>();
        @Override
        protected Void doInBackground(String... temp) {
            String s=temp[0];
           // int noOfPages=1;
           //while(true) {
                try {
                    URL url = new URL(s);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    JSONObject topLevel = new JSONObject(builder.toString());
                    JSONArray items = topLevel.getJSONArray("items");
                    for (int key = 0; key < items.length(); key++) {
                        JSONObject useritem = items.getJSONObject(key);
                        outp.add(useritem.getString("login"));
                    }
                    /*
                    if(urlConnection.getHeaderFields().containsKey("Link")) {
                        String s2 = urlConnection.getHeaderFields().get("Link").get(0);
                        String count = s2.split(",")[1].split(";")[0].split("&")[2].split("=")[1];
                       noOfPages=Integer.parseInt(count.substring(0,count.length()-1));
                    }
                    outp.add(Integer.toString(noOfPages));
                    */
                    urlConnection.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                /*
            for(int i=2;i<=noOfPages;i++){
                try {
                    URL url = new URL(s+"&page="+Integer.toString(i));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    JSONObject topLevel = new JSONObject(builder.toString());
                    JSONArray items = topLevel.getJSONArray("items");
                    for (int key = 0; key < items.length(); key++) {
                        JSONObject useritem = items.getJSONObject(key);
                        outp.add(useritem.getString("login"));
                    }
                    urlConnection.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void temp){
            //Add outputs here
            Toast.makeText(getApplicationContext(), "Completed Search", Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,outp);
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);
        }
    }
}
