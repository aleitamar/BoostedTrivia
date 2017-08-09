
package com.adaptionsoft.games.trivia.runner;
import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.GameSettings;
import com.adaptionsoft.games.uglytrivia.GameState;

import java.util.Random;


public class GameRunner {

	private static boolean notAWinner;

	public static void main(String[] args) {
		Game aGame = new Game(null, new GameSettings(), new GameState());
		
		aGame.addPlayer("Chet");
		aGame.addPlayer("Pat");
		aGame.addPlayer("Sue");
		
		Random rand = new Random();
	
		do {
			
			aGame.roll(rand.nextInt(5) + 1);
			
			if (rand.nextInt(9) == 7) {
				notAWinner = aGame.wrongAnswer();
			} else {
				notAWinner = aGame.wasCorrectlyAnswered();
			}
			
			
			
		} while (notAWinner);
		
	}
}
