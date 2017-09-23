package com.example.nkorolkov.andmanyaiswelldone;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends ActivityWithMenu {
    private DatabaseReference mSimpleFirechatDatabaseReference;
    private FirebaseRecyclerAdapter<ChatMessage, FirechatMsgViewHolder>
            mFirebaseAdapter;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private MainActivity that;
    private Button mSendButton, mScrollButton;
    private EditText mMsgEditText, mMsgDescriptionText;
    private String mUsername, mAvatar;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        that = this;
        receiveMessages();
        sendMessage();
    }

    public void receiveMessages(){
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(false);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mSimpleFirechatDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAdapter = new FirebaseRecyclerAdapter
                <ChatMessage, FirechatMsgViewHolder>

                (ChatMessage.class,  R.layout.chat_message, FirechatMsgViewHolder.class,
                        mSimpleFirechatDatabaseReference.child("messages")) {
            @Override
            protected void populateViewHolder(FirechatMsgViewHolder viewHolder,
                                              ChatMessage friendlyMessage, int position) {

                if (friendlyMessage.getDescription().equals(""))
                    viewHolder.msgDescriptionView.setVisibility(View.GONE);
                if (friendlyMessage.getText().equals(""))
                    viewHolder.msgTextView.setVisibility(View.GONE);

                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.userTextView.setText(friendlyMessage.getName());
                viewHolder.msgTextView.setText(friendlyMessage.getText());
                viewHolder.msgDescriptionView.setText(friendlyMessage.getDescription());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.userImageView
                            .setImageDrawable(ContextCompat
                                    .getDrawable(MainActivity.this,
                                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(MainActivity.this)
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.userImageView);
                }
            }
        };

        mScrollButton = (Button) findViewById(R.id.scrollBtn);
        mFirebaseAdapter.registerAdapterDataObserver(
            new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    mScrollButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                            mMessageRecyclerView.scrollToPosition(mFirebaseAdapter.getItemCount()-1);
                        }
                    });
                }
            });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    public static class FirechatMsgViewHolder extends RecyclerView.ViewHolder {
        public TextView msgTextView;
        public TextView msgDescriptionView;
        public TextView userTextView;
        public CircleImageView userImageView;

        public FirechatMsgViewHolder(View v) {
            super(v);
            msgTextView = (TextView) itemView.findViewById(R.id.msgTextView);
            msgDescriptionView = (TextView) itemView.findViewById(R.id.msgDescriptionView);
            userTextView = (TextView) itemView.findViewById(R.id.userTextView);
            userImageView = (CircleImageView) itemView.findViewById(R.id.userImageView);
        }
    }

    public void sendMessage(){
        // SEND MESSAGES
        mSendButton = (Button) findViewById(R.id.sendButton);
        mMsgEditText = (EditText) findViewById(R.id.msgEditText);
        mMsgDescriptionText = (EditText) findViewById(R.id.msgDescriptionText);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsername = (mFirebaseUser == null) ? null : mFirebaseUser.getDisplayName();

        if (mUsername == null){
            Toast.makeText(this, "Вы не авторизованы! Советую войти в систему =)",
                    Toast.LENGTH_LONG).show();
            mSendButton.setEnabled(false);
            return;
        }

        mSendButton.setEnabled(true);
        mAvatar = null;
        if (mFirebaseUser.getPhotoUrl() != null)
            mAvatar = mFirebaseUser.getPhotoUrl().toString();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgEditText.getText().toString().equals("") &&
                        mMsgDescriptionText.getText().toString().equals("")) {
                    Toast.makeText(
                            that,
                            "Странно... зачем отправлять пустое сообщение?",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                ChatMessage friendlyMessage = new ChatMessage(
                        mMsgEditText.getText().toString(),
                        mMsgDescriptionText.getText().toString(),
                        mUsername,
                        mAvatar
                );
                mSimpleFirechatDatabaseReference.child("messages").push().setValue(friendlyMessage);
                mMsgEditText.setText("");
                mMsgDescriptionText.setText("");
            }
        });
    }
}
