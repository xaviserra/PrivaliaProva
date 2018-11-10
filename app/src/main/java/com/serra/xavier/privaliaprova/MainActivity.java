package com.serra.xavier.privaliaprova;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static String url = "https://api.themoviedb.org/3/movie/popular?api_key=93aea0c77bc168d8bbce3918cefefa45&page=";
    private static int page = 1;
    public static int MAXpage = 1;

    ListView listKey;
    ListView listV;
    Button searchButton;
    EditText searchText;
    ListAdapter adapter;


    Boolean controlKeyList = true;
    int ptotalItemCount = 0;
    Boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        page = 1;
        searchButton = findViewById(R.id.searchButton);
        searchButton.setText("Search");
        searchText = findViewById(R.id.searchText);
        listKey = findViewById(R.id.keywordsList);
        listKey.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                controlKeyList = false;
                searchText.setText(adapterView.getItemAtPosition(i).toString());
                startToSearch(view);
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(controlKeyList) {
                    searchKeywords(s.toString());
                }
                controlKeyList = true;
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        initiateList();

    }

    /**
     * This method initialize the list with the movies.
     * Using the url.
     * This list create a listener to show all the movies scrolling by pagination
     */
    public void initiateList(){
        listV=findViewById(R.id.list);
        adapter=new ListAdapter(this, new ArrayList<MovieItem>());
        listV.setAdapter(adapter);
        controlKeyList = true;
        ptotalItemCount = 0;
        loading = true;

        new GetDataTMDb(this, url + Integer.toString(page)).execute();

        listV.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) { }
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (loading) {
                    if (totalItemCount >= ptotalItemCount) {
                        loading = false;
                        ptotalItemCount = totalItemCount;
                        page++;
                    }
                }
                if ((!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem))&&(MAXpage > page )) {
                    new GetDataTMDb(MainActivity.this, url + Integer.toString(page)).execute();
                    loading = true;
                }
            }
        });
    }

    /**
     * Method to add the Movies list to show
     * Obtain the movie list from GetDataTRMDb class
     * @param listMovie
     */
    public void returnList(final ArrayList<MovieItem> listMovie){
        adapter.addAll(listMovie);
    }

    /**
     * Method to obtain the results of GetKeyWords
     * Add the keywords to the list and show it.
     * @param listKeywords
     */
    public void returnKeyworrds(final ArrayList<String> listKeywords){
        listKey.setVisibility(View.VISIBLE);
        listKey.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listKeywords));

    }

    /**
     * Method to search keywords
     * Call the class GetKeywords to generate the keywords.
     * @param st
     */
    public void searchKeywords(String st){
        if((st.length() > 0 )&&(!st.equals(""))) {
            new GetKeywords(this, st).execute();
        }else{
            listKey.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method to start the search.
     * Prepare the URL to search, and call the method to create the list
     * @param view
     */
    public void startToSearch (View view){
        listKey.setVisibility(View.INVISIBLE);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(!searchText.getText().toString().equals("")){
            url = "https://api.themoviedb.org/3/search/movie?api_key=93aea0c77bc168d8bbce3918cefefa45&query="+ searchText.getText().toString() + "&page=";
            page = 1;
            initiateList();
        }
    }
}
