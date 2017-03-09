package com.apppartner.androidtest.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apppartner.androidtest.R;
import com.apppartner.androidtest.api.ChatLogMessageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.apppartner.androidtest.chat.ChatAdapter.ChatViewHolder.avatarImageView;


/**
 * A recycler view adapter used to display chat log messages in {@link ChatActivity}.
 * <p/>
 * Created on 8/27/16.
 *
 * @author Thomas Colligan
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private List<ChatLogMessageModel> chatLogMessageModelList;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public ChatAdapter(Context context)
    {
        chatLogMessageModelList = new ArrayList<>();

    }

    //==============================================================================================
    // Class Instance Methods
    //==============================================================================================

    public void setChatLogMessageModelList(List<ChatLogMessageModel> chatLogMessageModelList)
    {
        this.chatLogMessageModelList = chatLogMessageModelList;
        notifyDataSetChanged();
    }

    //==============================================================================================
    // RecyclerView.Adapter Methods
    //==============================================================================================

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_chat, parent, false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position)
    {
        ChatLogMessageModel chatLogMessageModel = chatLogMessageModelList.get(position);

        Picasso.with(avatarImageView.getContext()).load(chatLogMessageModel.avatarUrl).into(avatarImageView);
        viewHolder.messageTextView.setText(chatLogMessageModel.message);
    }

    @Override
    public int getItemCount()
    {
        return chatLogMessageModelList.size();
    }

    //==============================================================================================
    // ChatViewHolder Class
    //==============================================================================================

    public static class ChatViewHolder extends RecyclerView.ViewHolder
    {
        public static ImageView avatarImageView;
        TextView messageTextView;

        public ChatViewHolder(View view)
        {
            super(view);
            avatarImageView = (ImageView)view.findViewById(R.id.avatarImageView);
            messageTextView = (TextView)view.findViewById(R.id.messageTextView);
        }
    }

}
