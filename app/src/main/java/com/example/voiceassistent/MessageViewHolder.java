package com.example.voiceassistent;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voiceassistent.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    protected TextView messageText;
    protected TextView messageDate;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.message_view);
        messageDate = itemView.findViewById(R.id.message_date_view);
        DisplayMetrics metrics = getDisplayMetrics(itemView.getContext());
        setMessageBubbleMaxWidth(metrics.widthPixels);
    }

    private DisplayMetrics getDisplayMetrics(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics;
    }

    private void setMessageBubbleMaxWidth(int displayWidthPixels) {
        messageText.setMaxWidth(2 * displayWidthPixels / 3);
    }

    public void bind(Message message) {
        messageText.setText(message.text);
        DateFormat fmt = new SimpleDateFormat("HH:mm", getCurrentLocale(this.itemView.getContext()));
        messageDate.setText(fmt.format(message.date));
    }

    Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }
}
