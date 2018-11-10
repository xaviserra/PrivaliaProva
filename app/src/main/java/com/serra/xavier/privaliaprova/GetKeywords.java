package com.serra.xavier.privaliaprova;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetKeywords extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private MainActivity activity;
    private String string;
    private ProgressDialog pDialog;

    public GetKeywords(MainActivity activity, String string) {
        this.activity = activity;
        this.string = string;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Method to charge all the keywords to the list.
     * Create the connection and prepare to parse all the data recieved.
     * All the data ordered in anArrayList.
     * @param params
     * @return
     */
    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        URL url = null;
        ArrayList<String> result = new ArrayList<>();
        try {
            String urlString = "https://api.themoviedb.org/3/search/keyword?api_key=93aea0c77bc168d8bbce3918cefefa45&query=" + this.string + "&page=1";
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            //connection.setRequestProperty("Authorization", "Bearer {access_token}");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();

            InputStream stream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String st = sb.toString();
            if (!st.equals("null")) {
                JSONObject jsonobj = new JSONObject(st);

                JSONArray results = jsonobj.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {

                    String name = "";
                    JSONObject r = results.getJSONObject(i);
                    //System.out.println("TOTAL: " + r.toString());
                    if(r.getString("name")!= null) {
                        name = r.getString("name");
                    }
                    result.add(name);
                    System.out.println(" 1. " + name + "\n");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * This Method obtain all the results from the doInBackground and call the returnKeywords method to add all the data to the Keyword List view
     */
    @Override
    protected void onPostExecute(ArrayList<String> result) {
        activity.returnKeyworrds(result);

    }
}
