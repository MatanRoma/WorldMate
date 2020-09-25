package com.example.androidsecondproject.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.androidsecondproject.model.Message;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.repository.Repository;
import com.google.firebase.database.Query;

public class ChatViewModel extends AndroidViewModel {
    private Repository mRepository;
    private String chatId;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mRepository =Repository.getInstance(application.getApplicationContext());
    }

    public Query readAllMessages() {
        return mRepository.readAllMessages(chatId);
    }

    public void setChatId(String chatId) {
        this.chatId=chatId;
    }

    public void writeMessage(String text) {
        mRepository.writeMessage(chatId,new Message(mRepository.getCurrentUserId(),text));
    }

    public String getMyUid() {
        return mRepository.getCurrentUserId();
    }
}
