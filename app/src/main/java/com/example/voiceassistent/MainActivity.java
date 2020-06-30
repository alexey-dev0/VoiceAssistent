package com.example.voiceassistent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    protected Button sendButton;
    protected EditText questionText;
    protected TextView chatWindow;
    protected TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatWindow = findViewById(R.id.chatWindow);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });

        AI.initialize(this);

        textToSpeech = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                textToSpeech.setLanguage(new Locale("ru"));
                            }
                        }
        });
    }

    protected void onSend()
    {
        String question = questionText.getText().toString();
        questionText.getText().clear();
        chatWindow.append(question.trim());
        chatWindow.append("\n");

        String answer = AI.getAnswer(question);
        chatWindow.append(answer);
        chatWindow.append("\n");

        textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putCharSequence("messages", chatWindow.getText());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        chatWindow.setText(savedInstanceState.getCharSequence("messages"));
        super.onRestoreInstanceState(savedInstanceState);
    }
}