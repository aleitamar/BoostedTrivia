package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
  public static final int MAX_PLAYERS = 6;
  public static final int NUMBER_OF_BOXES = 12;
  private static final int LAST_BOX = NUMBER_OF_BOXES - 1;
  private SystemSettingsDAO settings;
  private final GameStateDAO game;
  private List names = new ArrayList();
  private List<Player> players = new ArrayList<>();
  private int[] coins = new int[MAX_PLAYERS];
  private boolean[] inPenalty = new boolean[MAX_PLAYERS];
  private int currentPlayer = 0;
  private boolean shouldGetOutOfPenalty;
  private Authenticator authenticator;
  private LinkedList pop = new LinkedList();
  private LinkedList science = new LinkedList();
  private LinkedList sports = new LinkedList();
  private LinkedList rock = new LinkedList();

  public Game(Authenticator authenticator, SystemSettingsDAO settings, GameStateDAO gameState) {
    this.authenticator = authenticator;
    this.game = gameState;
    this.settings = settings;
    createQuestions();
  }

  private void createQuestions() {
    int totalQuestions = this.settings.getNumberOfQuestions();
    for (int i = 0; i < totalQuestions; i++) {
      pop.addLast("Pop Question " + i);
      science.addLast("Science Question " + i);
      sports.addLast("Sports Question " + i);
      rock.addLast("Rock Question " + i);
    }
  }

  public void addPlayer(String name) {
    if (!authenticator.authenticate(name)) {
      throw new MyAuthenticationException();
    }
    initPlayer(name);
  }

  private void initPlayer(String name) {
    names.add(name);
    players.add(new Player(0));
    int playerIndex = names.size();
    coins[playerIndex] = 0;
    inPenalty[playerIndex] = false;
    print(name + " was added");
    print("They are player number " + playerIndex);
  }

  void print(String message) {
    System.out.println(message);
  }

  public void roll(int roll) {
    print(names.get(currentPlayer) + " is the current player");
    print("They have rolled a " + roll);
    if (inPenalty[currentPlayer]) {
      tryToGetOutOfPenalty(roll);
    } else {
      movePlayer(roll);
    }
    game.save(names, players, coins, inPenalty);
  }

  private void tryToGetOutOfPenalty(int roll) {
    if (isEven(roll)) {
      getOutOfPenalty(roll);
    } else {
      stayInPenalty();
    }
  }

  private void stayInPenalty() {
    print(names.get(currentPlayer) + " is not getting out of the penalty box");
    shouldGetOutOfPenalty = false;
  }

  private void movePlayer(int amount) {
    setPlace(getPlace() + amount);
    if (getPlace() > LAST_BOX) setPlace(getPlace() - NUMBER_OF_BOXES);
    print(names.get(currentPlayer)+ "'s new location is " + getPlace());
    print("The category is " + currentCategory());
    askQuestion();
  }

  private int getPlace() {
    return currentPlayer().getPlace();
  }

  private void setPlace(int newPlace) {
    currentPlayer().setPlace(newPlace);
  }

  private Player currentPlayer() {
    return players.get(currentPlayer);
  }

  private void getOutOfPenalty(int roll) {
    shouldGetOutOfPenalty = true;
    print(names.get(currentPlayer) + " is getting out of the penalty box");
    movePlayer(roll);
  }

  private boolean isEven(int roll) {
    return roll % 2 != 0;
  }

  private void askQuestion() {
    if (currentCategory() == "Pop")
      System.out.println(pop.removeFirst());
    if (currentCategory() == "Science")
      System.out.println(science.removeFirst());
    if (currentCategory() == "Sports")
      System.out.println(sports.removeFirst());
    if (currentCategory() == "Rock")
      System.out.println(rock.removeFirst());
  }

  private String currentCategory() {
    if (getPlace() == 0) return "Pop";
    if (getPlace() == 4) return "Pop";
    if (getPlace() == 8) return "Pop";
    if (getPlace() == 1) return "Science";
    if (getPlace() == 5) return "Science";
    if (getPlace() == 9) return "Science";
    if (getPlace() == 2) return "Sports";
    if (getPlace() == 6) return "Sports";
    if (getPlace() == 10) return "Sports";
    return "Rock";
  }

  public boolean wasCorrectlyAnswered() {
    if (inPenalty[currentPlayer] && !shouldGetOutOfPenalty) {
        nextPlayer();
        return true;
      } else {
        return processCorrectAnswer();
      }
    }

  private boolean processCorrectAnswer() {
    print("Answer was correct!!!!");
    addCoin();
    return checkForWin();
  }

  private void addCoin() {
    coins[currentPlayer]++;
    print(names.get(currentPlayer)
        + " now has "
        + coins[currentPlayer]
        + " Gold Coins.");
  }

  private boolean checkForWin() {
    boolean winner = didPlayerWin();
    nextPlayer(); //TODO: move to end of method? bug?
    if (winner) {
      checkIfChampion();
    }
    return winner;
  }

  private void checkIfChampion() {
    GameWinnersList.getInstance().add((String) names.get(currentPlayer));
    if (GameWinnersList.getInstance().getWinnings((String) names.get(currentPlayer)) > 3) {
      print(names.get(currentPlayer) + " is a champ!!!!");
    }
  }

  public boolean wrongAnswer() {
    print("Question was incorrectly answered");
    sendToPenalty();
    nextPlayer();
    return true;
  }

  private void sendToPenalty() {
    print(names.get(currentPlayer) + " was sent to the penalty box");
    inPenalty[currentPlayer] = true;
  }

  private void nextPlayer() {
    currentPlayer++;
    if (currentPlayer == names.size()) currentPlayer = 0;
  }

  private boolean didPlayerWin() {
    return !(coins[currentPlayer] == MAX_PLAYERS);
  }
}
