package application;

import java.util.concurrent.ExecutorService;

import javafx.concurrent.Task;

public class ExecutorController {
	
	private ExecutorService executor;
	private QuizController controllerQuiz;
	
	/**
	 * Constructor which takes in an ExecutorService for it to manage.
	 * 
	 * @param executor - ExecutorService instance to keep open and manage.
	 */
	public ExecutorController(ExecutorService executor) {
		this.executor = executor;
		
	}
	
	
	/**
	 * Setter method which sets the QuizController instance of the application to interact with.
	 * 
	 * @param quiz - the unique QuizController instance.
	 */
	public void setQuiz(QuizController quiz) {
		this.controllerQuiz = quiz;
	}
	
	
	/**
	 * This method runs a bash command in a separate thread. It disables the GUI from processing any interactions
	 * in the back-end whilst the thread is still busy.
	 * 
	 * @param command - String value of input passed into bash terminal.
	 */
	public void runTask(String command) {
		
		// Disable user input on the quiz
		controllerQuiz.setBusy(true);
		
		// Lambda function to create task that runs generic bash command
		Task<Void> runnableTask = new Task<Void>() {
			@Override
			public Void call(){
				try {
					ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
					Process process = pb.start();

					process.waitFor();

					process.destroy();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		
		// Set condition to re-enable user input when thread has successfuly completed
		runnableTask.setOnSucceeded( event -> {
			controllerQuiz.setBusy(false);
		});
		
		// Run the scheduled task
		executor.execute(runnableTask);
		
	}
	
	
	/**
	 * This method shuts down the executor service running the spare thread/s.
	 * 
	 */
	public void endExecutor() {
		executor.shutdown();
	}
	
	
}