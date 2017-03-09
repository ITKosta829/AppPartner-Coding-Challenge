package com.apppartner.androidtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.apppartner.androidtest.animation.AnimationActivity;
import com.apppartner.androidtest.chat.ChatActivity;
import com.apppartner.androidtest.login.LoginActivity;

public class MainActivity extends AppCompatActivity
{
    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.activity_main_title);
        setContentView(R.layout.activity_main);

    }

    //==============================================================================================
    // Button Click Methods
    //==============================================================================================

    public void onChatClicked(View v)
    {
        ChatActivity.start(this);
    }

    public void onLoginClicked(View v)
    {
        LoginActivity.start(this);
    }

    public void onAnimationClicked(View v)
    {
        AnimationActivity.start(this);
    }
}