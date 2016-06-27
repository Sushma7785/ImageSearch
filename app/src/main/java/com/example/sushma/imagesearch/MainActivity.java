package com.example.sushma.imagesearch;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sushma.imagesearch.SpellChecker.Trie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonSubmit;
    private EditText imageText;
    private final String api_key = "ushvt5jtxf3tg97e46vy2bxh";
    private ArrayList<String> urlStrings = new ArrayList<String>();
    private imageFragment imageFragment = null;
    private static Trie dict = null;
    static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        imageText = (EditText) findViewById(R.id.imageText);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               new spellChecker(imageText.getText().toString()).execute();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class spellChecker extends AsyncTask<Void, Void, String> {
        String getImageText;

        public spellChecker(String getImageText) {
            this.getImageText = getImageText;
        }
        @Override
        protected String doInBackground(Void... params) {
            String finalString = null;
            if(dict == null) {
                dict = new Trie();
                try {
                    constructTrie();
                    Log.i(TAG, "Trie constructed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(dict.search(getImageText.toLowerCase())) {
                finalString = getImageText;
                Log.i(TAG, "String found");
            }
            else {
                finalString = autoCorrect(getImageText.toLowerCase());
            }
            return finalString;
        }


        @Override
        protected void onPostExecute(String finalString) {
            //super.onPostExecute(finalString);
            Log.i(TAG, "String inside post " + finalString);
            imageText.setText(finalString);
            new getImageTask().execute();
        }
    }

    private String autoCorrect(String initStr) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<initStr.length(); i++) {
            if(Character.isLetter(initStr.charAt(i))) {
                sb.append(initStr.charAt(i));
            }
        }
        Log.i(TAG, "final String " + sb.toString());

        if(dict.search(sb.toString())) {
            return sb.toString();
        }

        String finalString = dict.spellCheck(sb.toString());


        return finalString;

    }

    public void constructTrie() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("dictionary.txt")));
        String line;
        while ((line = br.readLine()) != null) {
            dict.insertString(line.toLowerCase());
        }
    }


    public class getImageTask extends AsyncTask<Void, Void, Void> {

        String getImageText;
        @Override
        protected void onPreExecute() {
            getImageText = imageText.getText().toString();
            Log.i(TAG," String inside getImageTask " + getImageText);
        }


        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection;
            URL url = null;

            final String baseUrl = "https://api.gettyimages.com/v3/search/images?";
            final String phrase_param = "phrase";
            final String api_key_param = "Api-Key";

            Uri finalUri = Uri.parse(baseUrl).buildUpon().
                    appendQueryParameter(phrase_param, getImageText)
                    .build();
            urlStrings.clear();
            try {
                url = new URL(finalUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty(api_key_param, api_key);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                JSONObject jObj = getJsonfromResult(inputStream);
                JSONArray jArr = jObj.getJSONArray("images");
                for(int i=0 ; i < jArr.length(); i++) {
                    JSONObject objInside = jArr.getJSONObject(i);
                    JSONArray jUrlArr = objInside.getJSONArray("display_sizes");
                    for(int j=0 ; j < jUrlArr.length(); j++) {
                        JSONObject objInside2 = jUrlArr.getJSONObject(j);
                        String uriImage = objInside2.getString("uri");
                        urlStrings.add(uriImage);
                    }

                }
                System.out.println(urlConnection.getResponseMessage() + " " + urlConnection.getResponseCode() + " " + urlStrings.size() );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Bundle data = new Bundle();
            data.putStringArrayList("urlList", urlStrings );
            System.out.println(urlStrings.size());
            imageFragment = new imageFragment();
            imageFragment.setArguments(data);
            getSupportFragmentManager().beginTransaction().replace(R.id.grid_layout,imageFragment).
                    addToBackStack("grid_view_frag").
                    commit();
        }

    }

    private JSONObject getJsonfromResult(InputStream inputStream) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while((line = br.readLine()) != null) {
                sb.append(line)
                        .append("\n");
            }

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            throw e;
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
