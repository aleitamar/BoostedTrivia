package com.adaptionsoft.games.uglytrivia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by itzik on 1/7/2016.
 */
public class GameState implements GameStateDAO {
    public GameState() {
        try {
            Connection conn = null;
            Properties connectionProps = new Properties();
            connectionProps.put("user", "myuser");
            connectionProps.put("password", "mypassword");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    connectionProps);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(List players, List places, int[] purses, boolean[] inPenaltyBox) {
    }
}
