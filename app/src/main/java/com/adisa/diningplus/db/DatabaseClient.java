package com.adisa.diningplus.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context ctx;
    private static DatabaseClient instance;

    private AppDatabase db;

    public DatabaseClient(Context ctx) {
        this.ctx = ctx;
        db = Room.databaseBuilder(ctx, AppDatabase.class, "dining").build();
    }

    public static synchronized DatabaseClient getInstance(Context ctx) {
        if (instance == null) {
            instance = new DatabaseClient(ctx);
        }
        return instance;
    }

    public AppDatabase getDB() {
        return db;
    }
}
