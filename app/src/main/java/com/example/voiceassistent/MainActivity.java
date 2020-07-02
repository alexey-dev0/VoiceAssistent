package com.example.voiceassistent;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceassistent.model.Message;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "AI_settings";
    protected static Timer timer = new Timer();
    protected Button sendButton;
    protected EditText questionText;
    protected RecyclerView chatMessageList;
    protected MessageListAdapter messageListAdapter;
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (messageListAdapter.getItemCount() == 0) return;
            Log.w("TIMER_TICK", "TICK");
            messageListAdapter.messageList.get(messageListAdapter.getItemCount() - 1).text += "\uD83E\uDD14";
            scrollDown();
        }
    };
    protected TextToSpeech textToSpeech;
    SharedPreferences sPref;
    private boolean isLight = true;
    private String THEME = "THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        if (sPref.contains(THEME)) {
            isLight = sPref.getBoolean(THEME, true);
            if (!isLight) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);

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
        ((LinearLayoutManager) chatMessageList.getLayoutManager()).setStackFromEnd(true);
        chatMessageList.setAdapter(messageListAdapter);

        AI.initialize(this);
        textToSpeech = new TextToSpeech(getApplicationContext(),
                status -> {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(new Locale("ru"));
                    }
                });

        scrollDown();
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
        sendButton.setTextColor(getResources().getColor(R.color.light_text_color));
    }

    protected void stopTimer() {
        timer.cancel();
        timer = new Timer();
        messageListAdapter.messageList.remove(messageListAdapter.getItemCount() - 1);
        sendButton.setEnabled(true);
        sendButton.setTextColor(getResources().getColor(R.color.light_text_color_shadow));
    }

    protected void scrollDown() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_setting:
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isLight = true;
                break;
            case R.id.night_setting:
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isLight = false;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(THEME, isLight);
        editor.apply();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scrollDown();
    }
}