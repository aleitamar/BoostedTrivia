package com.adaptionsoft.games.uglytrivia;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ibenlays on 4/11/2016.
 */
public class GameTest extends Game {
  private static final GameStateDAO gameState = new GameStateDAO(){
    @Override
    public void save(List players, List places, int[] purses, boolean[] inPenaltyBox) {

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
  private String actualMessage;
  private GameTest game;
  private WinnersList winnersList = new WinnersList() {
    @Override
    public void add(String player) {
    }

    @Override
    public int getWinnings(String player) {
      return 4;
    }
  };

  public GameTest() {
    super(authenticator, settings, gameState);
  }

  @Override
  void print(String message) {
    this.actualMessage += message + "\n";
  }

  @Before
  public void setUp() throws Exception {
    game = new GameTest();
    game.actualMessage = "";
    game.addPlayer("Alaa");
  }

  @Test
  public void rollZero() throws Exception {
    game.roll(0);
    assertOutput(line("Alaa was added") +
        line("They are player number 1") +
        line("Alaa is the current player") +
        line("They have rolled a 0") +
        line("Alaa's new location is 0") +
        line("The category is Pop"));
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
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 1\n" +
        "The category is Science\n");
  }

  @Test
  public void wrongAnswer_playerEntersPenalty() throws Exception {
    getIntoPenalty();
    assertOutput("Alaa was added\n" +
            "They are player number 1\n" +
            "Alaa is the current player\n" +
            "They have rolled a 2\n" +
            "Alaa's new location is 2\n" +
            "The category is Sports\n" +
            "Question was incorrectly answered\n" +
            "Alaa was sent to the penalty box\n");
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
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Answer was correct!!!!\n" +
        "Alaa now has 1 Gold Coins.\n" +
        "Alaa is a champ!!!!\n");
  }

  @Test
  public void evenRollInPenalty_stayInPenaltyAndNoCoinsAdded() throws Exception {
    GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(2);
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Question was incorrectly answered\n" +
        "Alaa was sent to the penalty box\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa is not getting out of the penalty box\n");
  }

  @Test
  public void oddRollInPenalty_GetOutOfPenalty() throws Exception {
    GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(3);
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Question was incorrectly answered\n" +
        "Alaa was sent to the penalty box\n" +
        "Alaa is the current player\n" +
        "They have rolled a 3\n" +
        "Alaa is getting out of the penalty box\n" +
        "Alaa's new location is 5\n" +
        "The category is Science\n");
  }

  @Test
  public void moveThroughTheBoardUntillReachingStartingPosition() throws Exception {
    rollMany(1, game.NUMBER_OF_BOXES);
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 1\n" +
        "The category is Science\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 3\n" +
        "The category is Rock\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 4\n" +
        "The category is Pop\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 5\n" +
        "The category is Science\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 6\n" +
        "The category is Sports\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 7\n" +
        "The category is Rock\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 8\n" +
        "The category is Pop\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 9\n" +
        "The category is Science\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 10\n" +
        "The category is Sports\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 11\n" +
        "The category is Rock\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 0\n" +
        "The category is Pop\n" +
        "Alaa is the current player\n" +
        "They have rolled a 1\n" +
        "Alaa's new location is 1\n" +
        "The category is Science\n");
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
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Question was incorrectly answered\n" +
        "Alaa was sent to the penalty box\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa is not getting out of the penalty box\n");
  }

  @Test
  public void winGame() throws Exception {
//    winnersList.setWinnings(4);
    GameWinnersList.setTestingInstance(winnersList);
    getIntoPenalty();
    game.roll(2);
    game.wasCorrectlyAnswered();
    assertOutput("Alaa was added\n" +
        "They are player number 1\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa's new location is 2\n" +
        "The category is Sports\n" +
        "Question was incorrectly answered\n" +
        "Alaa was sent to the penalty box\n" +
        "Alaa is the current player\n" +
        "They have rolled a 2\n" +
        "Alaa is not getting out of the penalty box\n");
  }
}