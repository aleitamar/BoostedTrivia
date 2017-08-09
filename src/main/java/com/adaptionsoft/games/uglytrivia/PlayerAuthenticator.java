package com.adaptionsoft.games.uglytrivia;

/**
 * Created by itzik on 1/7/2016.
 */
public final class PlayerAuthenticator implements Authenticator {
    public PlayerAuthenticator() {
        throw new RuntimeException("I am trying to connect a remote server - you have to bypass me...");
    }
    @Override
    public boolean authenticate(String playerName) {
        return false;
    }
}
