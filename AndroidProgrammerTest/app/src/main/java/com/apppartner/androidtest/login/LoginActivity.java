package com.apppartner.androidtest.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.apppartner.androidtest.MainActivity;
import com.apppartner.androidtest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity{

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private EditText etUserName, etPassword;
    String UserNamePassword, code;

    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_login);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        etUserName = (EditText) findViewById(R.id.loginUserName);
        etPassword = (EditText) findViewById(R.id.loginPassword);
    }

    @Override
    public void onBackPressed()
    {
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

    public void onLogInClicked(View v) throws JSONException {

        if((etUserName.getText() != null) && (etPassword.getText() != null)){
            String userName = etUserName.getText().toString();
            String password = etPassword.getText().toString();

            UserNamePassword = "username=" + userName + "&password=" + password;

            new SendDataToServer().execute(UserNamePassword);

        } else {
            showAlertDialog(getString(R.string.alert_code), getString(R.string.alert_message));
        }

    }

    private void showAlertDialog(String result, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(result);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (code.equals("Invalid Entry")){

                }else {
                    onBackPressed();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class SendDataToServer extends AsyncTask<String, String, String> {

        String message;
        long startTime, stopTime, totalTime;

        @Override
        protected String doInBackground(String... params) {

            StringBuilder sb = new StringBuilder();

            startTime = System.currentTimeMillis();

            final String http = "http://dev3.apppartner.com/AppPartnerDeveloperTest/scripts/login.php";

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(http);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);

                urlConnection.connect();

                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(UserNamePassword);
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    try {
                        String json = sb.toString();
                        JSONObject jsonObject = new JSONObject(json);
                        code = jsonObject.getString("code");
                        message = jsonObject.getString("message");

                    } catch (JSONException e) {
                        Log.e("JSON ", "JSONException" + e);
                    }

                    stopTime = System.currentTimeMillis();
                    totalTime = stopTime - startTime;

                    message += "\n" + "API call time = " + totalTime + " milliseconds";

                } else {

                    code = "Invalid Entry";
                    message = urlConnection.getResponseMessage();

                    stopTime = System.currentTimeMillis();
                    totalTime = stopTime - startTime;

                    message += "\n" + "API call time = " + totalTime + " milliseconds";
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
        protected void onPostExecute(String s) {
            showAlertDialog(code, message);
        }

    }


}