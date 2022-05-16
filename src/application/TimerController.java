package application;

import java.util.concurrent.ExecutorService;

import javafx.concurrent.Task;

/**
 * This TimerController class is responsible for the timer that
 * displayed on the quiz mode screen when playing the game. 
 * 
 * @author Group 32
 * 
 */

public class TimerController {

	private ExecutorService executor;
	private QuizController controllerQuiz;
	private QuizGame quizGame;
	private long timeTaken;

	public TimerController(ExecutorService exec) {
		this.executor = exec;

	}

	public void setQuiz(QuizController quiz) {
		this.controllerQuiz = quiz;
	}

	public void setQuizLogic(QuizGame inputQuiz) {
		this.quizGame = inputQuiz;
	}


	/**
	 * This method is called from the quizController class when the game starts in order for the time to
	 *  get tracked. It makes use of a new thread separate from the GUI and the game logic thread, relying
	 *  on its own unique instance of an executor service.
	 *  
	 */
	public void timerStart() {

		int currentRound = quizGame.getRound();
		long timeLimit = 60;
		timeTaken = -1;

		Task<Void> runnableTask = new Task<Void>() {
			@Override
			public Void call(){
				try {
					// Don't start timing to allow the user to hear the festival voice, dependent on word length
					controllerQuiz.updateTimer(Long.toString(timeLimit));
					int wordLength = quizGame.getWordController().getWord(currentRound).length();
					Thread.sleep(wordLength*350);

					long startTime = System.currentTimeMillis();
					long elapsedSeconds = 0;

					while ((timeLimit - elapsedSeconds) > 0) {
						// If we have moved onto next round (correct, incorrect, don't know, end early)
						if (currentRound != quizGame.getRound()) {
							timeTaken = elapsedSeconds;
							return null;
						}
						long elapsedTime = System.currentTimeMillis() - startTime;
						elapsedSeconds = elapsedTime / 1000;
						controllerQuiz.updateTimer(Integer.toString( (int) (timeLimit - elapsedSeconds)));
					}

				} catch (Exception e) {
					e.printStackTrace();
				} 

				return null;
			}

		};

		runnableTask.setOnSucceeded( event -> {
			// Update time taken for that word round
			quizGame.setRoundTime((int) timeTaken, (int) currentRound);

			// Update GUI
			controllerQuiz.setScoreText((int) currentRound);

			// If timeTaken == -1, it means the user didn't progress to next round by themselves in time.
			if (timeTaken == -1) {
				controllerQuiz.dontKnowClicked(null);
			}

		});

		// Run the scheduled task
		executor.execute(runnableTask);
	}


	/**
	 * This method closes the executor service it manages for when the application is shut down.
	 * 
	 */
	public void endExecutor() {
		executor.shutdown();
	}


}