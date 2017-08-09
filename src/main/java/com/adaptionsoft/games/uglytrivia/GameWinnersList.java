package com.adaptionsoft.games.uglytrivia;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;

/**
 * Created by itzik on 1/7/2016.
 */
public class GameWinnersList implements WinnersList {
    private final MemcachedClient memcachedClient;

    private GameWinnersList() {
        try {
            memcachedClient = new MemcachedClient();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void setTestingInstance(WinnersList instance) {
        GameWinnersList.instance = instance;
    }

    private static WinnersList instance;

    public static WinnersList getInstance() {
        if (instance == null) {
            instance = new GameWinnersList();
        }
        return instance;
    }

    @Override
    public void add(String player) {
        if (memcachedClient.get(player) == null) {
            memcachedClient.set(player, 0, 0);
        }
        memcachedClient.set(player, 0, ((Integer) memcachedClient.get(player)) + 1);
    }

    @Override
    public int getWinnings(String player) {
        return (Integer)memcachedClient.get(player);
    }
}
