package com.example.androidsecondproject.model;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Match implements Serializable {
    private String otherUid;
    private String id;

    public Match() {
    }

    public Match(String otherUid, String id) {
        this.otherUid = otherUid;
        this.id = id;
    }

    public String getOtherUid() {
        return otherUid;
    }

    public void setOtherUid(String otherUid) {
        this.otherUid = otherUid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Log.d("string",(String)obj+" "+ this.otherUid);
        return this.otherUid.equals((String)obj);
    }
}
