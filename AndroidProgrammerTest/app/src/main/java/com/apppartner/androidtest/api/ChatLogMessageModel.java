package com.apppartner.androidtest.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A data model that represents a chat log message fetched from the AppPartner Web Server.
 * <p/>
 * Created on 8/27/16.
 *
 * @author Thomas Colligan
 */
public class ChatLogMessageModel {

    private static final String LOG_TAG = "ChatLogMessageModel";

    public int userId;
    public String avatarUrl;
    public String username;
    public String message;

    public ChatLogMessageModel(JSONObject jsonObject) {
        if (jsonObject != null) {
            try {
                userId = jsonObject.getInt("user_id");
                username = jsonObject.getString("username");
                avatarUrl = jsonObject.getString("avatar_url");
                message = jsonObject.getString("message");
            } catch (JSONException e) {
                Log.w(LOG_TAG, e);
            }
        }
    }
}
