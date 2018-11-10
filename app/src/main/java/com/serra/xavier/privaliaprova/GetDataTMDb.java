package com.serra.xavier.privaliaprova;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


public class GetDataTMDb extends AsyncTask<ArrayList<MovieItem>, Void, ArrayList<MovieItem>> {

    private MainActivity activity;
    private String url;
    private ProgressDialog loadingD;

    public GetDataTMDb(MainActivity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    /**
     * Method to initialize the progressDialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingD = new ProgressDialog(activity);
        loadingD.setTitle("The Movie DB");
        loadingD.setMessage("Loading...");
        loadingD.show();
    }

    /**
     * Method to charge all the movies to the list.
     * Create the connection and prepare to parse all the data recieved.
     * All the data ordered in anArrayList using the new class MovieItem.
     * @param params
     * @return
     */
    @Override
    protected ArrayList<MovieItem> doInBackground(ArrayList<MovieItem>... params) {
        URL url = null;
        ArrayList<MovieItem> result = new ArrayList<>();
        try {
            //CONNECTION
            url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            connection.connect();

            //READING DATA RECIEVED
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

            //ADD DATA TO JSONOBJECT AND PARSE
            if (!sb.toString().equals("null")) {
                JSONObject jsonobj = new JSONObject(sb.toString());
                JSONArray results = jsonobj.getJSONArray("results");
                MainActivity.MAXpage = jsonobj.getInt("total_pages");

                for (int i = 0; i < results.length(); i++) {
                    String title= "";
                    String year = "";
                    String overview = "";

                    JSONObject r = results.getJSONObject(i);
                    if(!r.getString("title").equals(null)) {
                        title = r.getString("title");
                    }
                    if((!r.getString("release_date").equals(null))&&(r.getString("release_date").length() > 3)) {
                        year = r.getString("release_date");
                        year = year.substring(0,4);
                    }else{
                        year = "Not available";
                    }
                    if(!r.getString("overview").equals("null")) {
                        overview = r.getString("overview");
                    }
                    Bitmap bitmap = null;
                    if(!r.getString("poster_path").equals("null")) {
                        String ImageUrl = "https://image.tmdb.org/t/p/w500/" + r.getString("poster_path");
                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(ImageUrl).getContent());
                    }
                    result.add(new MovieItem(title + " (" + year + ")",overview,bitmap));
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
     * This Method obtain all the results from the doInBackgraound and call the returnList method to add all the data to the ListView.
     * @param result
     */
        @Override
    protected void onPostExecute(ArrayList<MovieItem> result) {
        loadingD.dismiss();
        activity.returnList(result);
    }
}
