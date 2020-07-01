package com.example.voiceassistent;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Parcelable {
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            String text = source.readString();
            try {
                Date date = new SimpleDateFormat("HH:mm").parse(source.readString());
                Boolean isSend = source.readString().equals("true");
                Message msg = new Message(text, isSend);
                msg.date = date;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Boolean isSend) {
        this.text = text;
        this.isSend = isSend;
        this.date = new Date();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(new SimpleDateFormat("HH:mm").format(date));
        dest.writeString((isSend ? "true" : "false"));
    }
}
