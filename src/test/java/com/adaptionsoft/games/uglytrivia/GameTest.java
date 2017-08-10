package com.adaptionsoft.games.uglytrivia;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ibenlays on 4/11/2016.
 */
public class GameTest {
  private Game game;
  
  private static final GameStateDAO gameState = new GameStateDAO(){
	    @Override
	    public void save(List players, int[] places, int[] purses, boolean[] inPenaltyBox) {

	    }
	  };
	  private static final Authenticator authenticator = new Authenticator() {
	    @Override
	    public boolean authenticate(String playerName) {
	      return true;
	    }
	  };
	  private static final SystemSettingsDAO settings = new SystemSettingsDAO() {
	    @Override
	    public int getNumberOfQuestions() {
	      return 100;
	    }
	  };
	  
  private WinnersList winnersList = new WinnersList() {
	    @Override
	    public void add(String player) {
	    }

	    @Override
	    public int getWinnings(String player) {
	      return 4;
	    }
	  };
	  
  @Before
  public void setUp() throws Exception {
    game = new Game(authenticator, gameState, settings);
    game.actualMessage = "";
    game.add("Alaa");
  }

  @Test
  public void rollZero() throws Exception {
    game.roll(0);
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 0\nAlaa's new location is 0\nThe category is Pop\nPop Question 0\n");
  }

  private void assertOutput(String expectedOutput) {
    assertThat(game.actualMessage,
        is(expectedOutput));
  }

  private String line(String text) {
    return text + "\n";
  }

  @Test
  public void oddRoll() throws Exception {
    game.roll(1);
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 1\nThe category is Science\nScience Question 0\n");
  }

  @Test
  public void wrongAnswer_playerEntersPenalty() throws Exception {
    getIntoPenalty();
    assertOutput("Alaa was added\n"
    		+ "They are player number 1\n"
    		+ "Alaa is the current player\n"
    		+ "They have rolled a 2\n"
    		+ "Alaa's new location is 2\n"
    		+ "The category is Sports\n"
    		+ "Sports Question 0\n"
    		+ "Question was incorrectly answered\n"
    		+ "Alaa was sent to the penalty box\n");
  }

  private void getIntoPenalty() {
    game.roll(2);
    game.wrongAnswer();
  }

  @Test
  public void correctAnswer_coinIsAdded() throws Exception {
	GameWinnersList.setTestingInstance(winnersList);
    game.roll(2);
    game.wasCorrectlyAnswered();
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 2\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nAnswer was correct!!!!\nAlaa now has 1 Gold Coins.\nAlaa is a champ!!!!\n");
  }

  @Test
  public void evenRollInPenalty_stayInPenaltyAndNoCoinsAdded() throws Exception {
	GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(2);
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 2\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nQuestion was incorrectly answered\nAlaa was sent to the penalty box\nAlaa is the current player\nThey have rolled a 2\nAlaa is not getting out of the penalty box\n");
  }

  @Test
  public void oddRollInPenalty_GetOutOfPenalty() throws Exception {
	GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(3);
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 2\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nQuestion was incorrectly answered\nAlaa was sent to the penalty box\nAlaa is the current player\nThey have rolled a 3\nAlaa is getting out of the penalty box\nAlaa's new location is 5\nThe category is Science\nScience Question 0\n");
  }

  @Test
  public void moveThroughTheBoardUntillReachingStartingPosition() throws Exception {
    rollMany(1, game.NUMBER_OF_BOXES);
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 1\nThe category is Science\nScience Question 0\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 3\nThe category is Rock\nRock Question 0\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 4\nThe category is Pop\nPop Question 0\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 5\nThe category is Science\nScience Question 1\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 6\nThe category is Sports\nSports Question 1\nAlaa is the current player\nThey have rolled a 1\nAlaa's new location is 7\nThe category is Rock\nRock Question 1\n");
  }

  private void rollMany(int roll, int numberOfRolls) {
    for (int i = 0; i <= numberOfRolls; i++) {
      game.roll(roll);
    }
  }

  @Test
  public void answerCorrectlyInPenalty() throws Exception {
	GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(2);
    game.wasCorrectlyAnswered();
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 2\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nQuestion was incorrectly answered\nAlaa was sent to the penalty box\nAlaa is the current player\nThey have rolled a 2\nAlaa is not getting out of the penalty box\n");
  }

  @Test
  public void winGame() throws Exception {
//	GameWinnersList.setWinnings(4);
	GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(2);
    game.wasCorrectlyAnswered();
    assertOutput("Alaa was added\nThey are player number 1\nAlaa is the current player\nThey have rolled a 2\nAlaa's new location is 2\nThe category is Sports\nSports Question 0\nQuestion was incorrectly answered\nAlaa was sent to the penalty box\nAlaa is the current player\nThey have rolled a 2\nAlaa is not getting out of the penalty box\n");
  }
}