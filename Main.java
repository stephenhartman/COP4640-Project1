package project1;

import java.io.IOException;

/**
 * The main class creates a project1 object and initializes all the methods to complete the task.
 * @author Group 3 - Windows 10
 * @version 9/17/17
 *
 */
public class Main {

	public static void main(String args[]) throws IOException {

		final int FIFO = 0;
		final int LRU = 1;
		final int CUSTOM = 2;
		
		Project1 project = new Project1();

		project.menu();

		project.reset();

		project.execute(FIFO);

		project.execute(LRU);

		project.execute(CUSTOM);

		project.overallPerformance();
	}
}
