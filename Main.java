package project1;

import java.io.IOException;

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
