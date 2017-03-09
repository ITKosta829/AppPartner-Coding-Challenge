package com.apppartner.androidtest.chat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.apppartner.androidtest.MainActivity;
import com.apppartner.androidtest.R;
import com.apppartner.androidtest.api.ChatLogMessageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    protected static final String chatDataJSON = "http://dev3.apppartner.com/AppPartnerDeveloperTest/scripts/chat_log.php";

    List<ChatLogMessageModel> chatLogDataArrayList;

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context) {
        Intent starter = new Intent(context, ChatActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_chat);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        chatAdapter = new ChatAdapter(this);

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        chatLogDataArrayList = new ArrayList<>();

        new getChatData().execute(chatDataJSON);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class getChatData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder sb = new StringBuilder();

            HttpURLConnection urlConnection = null;
            try {
                java.net.URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.connect();

                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    in.close();

                    return sb.toString();

                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                JSONObject json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ChatLogMessageModel chatData = new ChatLogMessageModel(jsonObject);
                    chatLogDataArrayList.add(chatData);
                }

                chatAdapter.setChatLogMessageModelList(chatLogDataArrayList);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }

}