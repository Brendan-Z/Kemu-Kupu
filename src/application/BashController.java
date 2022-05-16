package application;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class is the bread and butter of the bash commands and hosts all
 * the methods that are required to run the bash commands through Java.
 * 
 * @author Group 32
 *
 */

public class BashController {

	private ExecutorController controllerExec;
	private TimerController controllerTimer;

	/**
	 * This method sets instances of each of the executor and timer controllers as private fields to be
	 * stored as link to these instances.
	 * 
	 * @param executor - ExecutorController instance
	 * @param timer - TimerController instance
	 */
	public BashController(ExecutorController executor, TimerController timer) {
		this.controllerExec = executor;
		this.controllerTimer = timer;
	}


	/**
	 * A constructor which sets only the executor instance and leaves the TimerController instance empty. 
	 * 
	 * @param executor - ExecutorController instance
	 */
	public BashController(ExecutorController executor) {
		this.controllerExec = executor;
	}


	/**
	 * Setter method for the executor instance attached to this controller instance.
	 * 
	 * @param executor - ExecutorController instance
	 */
	public void setExec(ExecutorController executor) {
		this.controllerExec = executor;
	}


	/**
	 * This is a general method that runs a bash command with no output (hence silent) Intended for
	 * file manipulation, festival, and data movement. No data is transferred back to java from the
	 * bash process.
	 * 
	 * @param command - String value passed into bash terminal
	 */
	public void runSilent(String command) {

		controllerExec.runTask(command);

	}


	/**
	 * This method starts the timer 
	 * 
	 */
	public void runTimer() {
		controllerTimer.timerStart();

	}

	/**
	 * This method uses bash but returns an arraylist of strings from the stdin of this java program. 
	 * It is intended to obtain the lines in a file, specifically the words we want to quiz on from the 
	 * selection file.
	 * 
	 * @param command - String input to be passed into bash terminal
	 * @return ArrayList<String> which should represent each line of the output of the bash command
	 */
	public ArrayList<String> getLines(String command) {
		ArrayList<String> outputArray = new ArrayList<String>();
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", command);
			Process process = pb.start();
			InputStream stdout = process.getInputStream();
			BufferedReader bufferedStdOut = new BufferedReader(new InputStreamReader(stdout));
			String line = null;

			process.waitFor();

			while ((line = bufferedStdOut.readLine()) != null) {
				// account for empty newline from empty file by ignoring 0 length lines
				if (line.length() > 0) {
					outputArray.add(line);
				}
			}

			process.destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputArray;
	}

}

