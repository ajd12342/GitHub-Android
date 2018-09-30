package com.example.pseudorandomcoders.outlab9;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        try {
            String u_name = getIntent().getStringExtra("username");
            String[] url = {"https://api.github.com/users/" + URLEncoder.encode(u_name, "UTF-8"), "https://api.github.com/users/" + URLEncoder.encode(u_name, "UTF-8") + "/repos"};
            new GetUserInfo().execute(url);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    private class GetUserInfo extends AsyncTask<String, Void, Void> {
        private ArrayList<String> userData=new ArrayList<>();
        private ArrayList<RepoData> arr=new ArrayList<>();
        @Override
        protected Void doInBackground(String... temp) {
            //ArrayList<RepoData> arr = new ArrayList<>();
            //---------------------GET USER INFO---------------------
            try {
                URL url = new URL(temp[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                userData.add(topLevel.getString("name"));
                userData.add(topLevel.getString("location"));
                userData.add(topLevel.getString("company"));

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            //-------------------------GET REPO INFO--------------------------------
            try {
                URL url = new URL(temp[1]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONArray topLevel = new JSONArray(builder.toString());
                //arr.add(new RepoData(Integer.toString(topLevel.length()),"a","a"));
                for (int k=0; k<topLevel.length(); k++)
                {
                    JSONObject repoinfo=topLevel.getJSONObject(k);
                    // the remaing contain reponname, created_at, description
                    // date conversio - DateTime dt=new DateTime("2010-01-01t12:00:10:00);
                    // how to get current date= LocalDateTime.now()-dt;
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        String string = repoinfo.getString("created_at");
                        Date result = df.parse(string);
                       Date now= Calendar.getInstance().getTime();
                        long duration=now.getTime()-result.getTime();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date(duration));
                        int yy=cal.get(Calendar.YEAR)-1970;
                        int mm=cal.get(Calendar.MONTH);
                        int dd=cal.get(Calendar.DAY_OF_MONTH);
                        String res=Integer.toString(yy)+" years, "+Integer.toString(mm)+" months, "+Integer.toString(dd)+" days";
                        arr.add(new RepoData(repoinfo.getString("name"),res,repoinfo.getString("description")));
                        //                        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
//                        long yy=diffInDays/(365);
//                        long mm=yy
                    }catch (ParseException e){
                        arr.add(new RepoData("d","d","d"));
                        e.printStackTrace();
                    }
                }


                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void temp){
           // Toast.makeText(getApplicationContext(), "Completed Second", Toast.LENGTH_SHORT).show();
            final TextView nm = findViewById(R.id.name_id);
            final TextView lc = findViewById(R.id.location_id);
            final TextView cm = findViewById(R.id.company_id);
            nm.setText(userData.get(0));
            lc.setText(userData.get(1));
            cm.setText(userData.get(2));
            ListView listView=findViewById(R.id.list2);
            RepInfoAdapter adapter=new RepInfoAdapter(getApplicationContext(),arr);
            listView.setAdapter(adapter);
        }
    }
    private class RepInfoAdapter extends ArrayAdapter<RepoData>{
        public RepInfoAdapter(Context context, ArrayList<RepoData> repoDataArrayList){
            super(context,0,repoDataArrayList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RepoData repoData = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_repoinfo, parent, false);
            }
            TextView repName= (TextView) convertView.findViewById(R.id.repName);
            TextView repAge = (TextView) convertView.findViewById(R.id.repAge);
            TextView repDescription=(TextView) convertView.findViewById(R.id.repDescription);
            // Populate the data into the template view using the data object
            repName.setText(repoData.name);
            repAge.setText(repoData.created);
            repDescription.setText(repoData.description);
            repName.setTextColor(Color.BLACK);
            repAge.setTextColor(Color.BLACK);
            repDescription.setTextColor(Color.BLACK);
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
class RepoData{
    public String name;
    public String created;
    public String description;
    public RepoData(String name,String created,String description){
        this.name=name;
        this.created=created;
        this.description=description;
    }
}