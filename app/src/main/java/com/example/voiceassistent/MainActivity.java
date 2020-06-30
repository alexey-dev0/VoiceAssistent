package com.example.voiceassistent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    protected Button sendButton;
    protected EditText questionText;
    protected TextView chatWindow;

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
    }
}