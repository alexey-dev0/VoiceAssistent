package com.example.voiceassistent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceassistent.model.Message;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    protected Button sendButton;
    protected EditText questionText;
    protected RecyclerView chatMessageList;
    protected MessageListAdapter messageListAdapter;
    protected TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> onSend());

        questionText = findViewById(R.id.questionField);
        questionText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendButton.performClick();
                return true;
            }
            return false;
        });

        chatMessageList = findViewById(R.id.chatMessageList);
        if (savedInstanceState != null) {
            ArrayList<Message> messageList = savedInstanceState.getParcelableArrayList("messageList");
            messageListAdapter = new MessageListAdapter(messageList);

        } else {
            messageListAdapter = new MessageListAdapter();
        }
        chatMessageList.setLayoutManager(new LinearLayoutManager(this));
        chatMessageList.setAdapter(messageListAdapter);
        scrollDown();

        AI.initialize(this);

        textToSpeech = new TextToSpeech(getApplicationContext(),
                status -> {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(new Locale("ru"));
                    }
                });
    }

    protected void startTimer() {
        String messageThinking = getResources().getString(R.string.message_think);
        messageListAdapter.messageList.add(new Message(messageThinking, false));
        scrollDown();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.obtainMessage().sendToTarget();
            }
        }, 1000, 1000);
        sendButton.setEnabled(false);
        sendButton.setTextColor(getResources().getColor(R.color.text_color_shadow));
    }

    protected  void stopTimer() {
        timer.cancel();
        timer = new Timer();
        messageListAdapter.messageList.remove(messageListAdapter.getItemCount() - 1);
        sendButton.setEnabled(true);
        sendButton.setTextColor(getResources().getColor(R.color.text_color));
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (messageListAdapter.getItemCount() == 0) return;
            Log.w("TIMER_TICK", "TICK");
            messageListAdapter.messageList.get(messageListAdapter.getItemCount() - 1).text += "\uD83E\uDD14";
            scrollDown();
        }
    };

    protected static Timer timer = new Timer();

    protected void scrollDown()
    {
        if (messageListAdapter.getItemCount() == 0) return;
        messageListAdapter.notifyDataSetChanged();
        chatMessageList.smoothScrollToPosition(messageListAdapter.getItemCount() - 1);
    }

    protected void onSend() {
        String question = questionText.getText().toString();
        if (question.isEmpty()) return;
        questionText.getText().clear();
        messageListAdapter.messageList.add(new Message(question, true));

        startTimer();

        AI.getAnswer(question, s -> {
            stopTimer();
            messageListAdapter.messageList.add(new Message(s, false));
            scrollDown();

            textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("messageList", messageListAdapter.messageList);
    }
}