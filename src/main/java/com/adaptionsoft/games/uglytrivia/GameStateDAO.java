package com.adaptionsoft.games.uglytrivia;

import java.util.List;

/**
 * Created by ibenlays on 4/11/2016.
 */
public interface GameStateDAO {
  void save(List players, int[] places, int[] purses, boolean[] inPenaltyBox);
}
