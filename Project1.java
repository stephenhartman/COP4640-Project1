package project1;

import java.io.*;
import java.util.*;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Project1 class has multiple Page replacement policy algorithms.  There is LRU, which is least recently used, FIFO, first in
 * first out, and a Custom policy which replaces the the the middle recently used page.  This class contains the algorithms,
 * counting mechanisms, output methods, and totaling methods to create a full report based on the number of iterations,
 * number of page frames to use, the page range, and the number of pages to randomly generate.
 * @author Group 3 - Windows 10
 * @version 9/17/17
 *
 */
public class Project1 {
	
	// Totals for the FIFO,LRU,CUSTOM failure & success rates
	private double successFIFO, failureFIFO, faultFIFO, successLRU, failureLRU, faultLRU, successCustom,
		failureCustom, faultCustom;
	private double successRate, failureRate, successRateTotal, failureRateTotal, success, pageFaultAverage, pageFaultTotal;

	int xPages = 0; 			// Number of pages in array
	int yPageRange = 0; 		// Pages 0 through Y
	int zPageFrames = 0;		// Number of page frames to use
	final int ITERATIONS = 100; // Number of run iterations

	boolean flag;				// flag = true if page exists in page frames
	int pages[];				// Array of xPages random pages
	double pageFault;			// Page fault counter
	int page;					// Page value for comparison
	int checkCounter;			// Counts the number of times pages were checked
	int frame[];				// Page frame array

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	Queue<Integer> queue = new LinkedList<Integer>();
	DecimalFormat decimal = new DecimalFormat("#.##");	// Formatter for doubles
	DecimalFormat integer = new DecimalFormat("#"); 	// Formatter for integers

	/**
	 * Takes page frame count from user input
	 *
	 * @throws IOException
	 */
	public void menu() throws IOException 
	{
		System.out.println("COP4640 - Group 3");
		System.out.println(ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME));
		System.out.println("Page Frame Simulator: FIFO, LRU, and Custom Algorithm");
		System.out.println();
		
		System.out.print("Please enter the number of pages you wish to utilize (X)      : ");
		String x = br.readLine();
		xPages = Integer.parseInt(x);
		System.out.println();
		
		System.out.print("Please enter the range of pages you wish to utilize 0 to (Y)  : ");
		String y = br.readLine();
		yPageRange = Integer.parseInt(y);
		System.out.println();
		
		System.out.print("Please enter the number of page frames you wish to utilize (Z): ");
		String z = br.readLine();
		zPageFrames = Integer.parseInt(z);
	}

	/**
	 * Increments the total counters for averages
	 */
	public void incrementTotals() 
	{
		pageFaultTotal += pageFault;
		successRateTotal += successRate;
		failureRateTotal += failureRate;
	}

	/**
	 * Displays the fault average, and average success and failure rates
	 */
	public void printResults() 
	{
		System.out.println("Page fault average number: " + decimal.format(pageFaultAverage) + "/" + xPages);
		System.out.println("Average success rate:      " + decimal.format(successRateTotal) + "%");
		System.out.println("Average failure rate:      " + decimal.format(failureRateTotal) + "%");
	}

	/**
	 * Sets all totals to 0 (used after sequence execution)
	 */
	public void resetTotals() 
	{
		pageFaultTotal = 0;
		successRateTotal = 0;
		failureRateTotal = 0;
	}
	
	/**
	 * Creates a random integer with a value between min and max inclusive
	 * @param min Minimum value of the integer
	 * @param max Maximum value of the integer
	 * @return Random number
	 */
	public static int randInt(int min, int max)
	{
		Random random = new Random();
		int randomNum =  random.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	/**
	 * Creates an array of pages within specified range
	 * @param numPages (X) the number of pages to utilize
	 * @param pageRange (Y) the range for the integers in each page
	 * @return Array of pages
	 */
	public static int[] createPages(int numPages, int pageRange)
	{
		int[] pageArray = new int[numPages];
		for (int i = 0; i < pageArray.length; i++)
		{
			pageArray[i] = randInt(0, pageRange);
		}
		return pageArray;
	}
	
	/**
	 * Check if a value is within an array
	 * @param value The value to check
	 * @param frames The range of frames
	 * @param array
	 * @return
	 */
	static boolean isInFrames(int value, int frames, int[] array) 
	{
		for (int i = 0; i < frames; i++){
			if (array[i] == value){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Resets all instance variables, including frame and pages arrays
	 *
	 * @throws IOException
	 */
	public void reset() throws IOException 
	{
		frame = new int[zPageFrames];
		
		// Creates sentinel value page frames
		for (int i = 0; i < zPageFrames; i++) {
			frame[i] = -1;
		}
		
		// Fill the pages array with X random integers within 0 to Y range
		pages = createPages(xPages, yPageRange);
		
		// Reset all counting values
		pageFault = 0;
		success = 0;
		failureRate = 0;
		successRate = 0;
		checkCounter = 0;
	}

	/**
	 * Executes page request sequence using a page replacement policy
	 * calculates pageFaultAverage, calls printResults() then resetTotals()
	 *
	 * @param algorithm The algorithm value to determine which module to execute
	 * @throws IOException
	 */
	void execute(int algorithm) throws IOException 
	{
		for (int i = 0; i < ITERATIONS; i++) 
		{
			reset();
			switch (algorithm) 
			{
			case 0:
				fifo();
				break;
			case 1:
				lru();
				break;
			case 2:
				userDefined();
				break;
			}
			incrementTotals();
		}
		pageFaultAverage = pageFaultTotal / ITERATIONS;

		switch (algorithm) 
		{
		case 0:
			System.out.println("\n***FIFO Totals***");
			printResults();
			successFIFO = successRateTotal;
			failureFIFO = failureRateTotal;
			faultFIFO = pageFaultAverage;
			break;
		case 1:
			System.out.println("\n***LRU Totals***");
			printResults();
			successLRU = successRateTotal;
			failureLRU = failureRateTotal;
			faultLRU = pageFaultAverage;
			break;
		case 2:
			System.out.println("\n***Custom Totals***");
			printResults();
			successCustom = successRateTotal;
			failureCustom = failureRateTotal;
			faultCustom = pageFaultAverage;
			break;
		}
		resetTotals();
	}

	/**
	 * FIFO page replacement policy fills pages[] with random page requests
	 * ranging from 0 to yPageFrames, counts page faults, and runs success rate report
	 */
	public void fifo() 
	{
		int index = 0;
		System.out.println();
		System.out.println("FIFO Policy");

		for (int j = 0; j < xPages; j++) 
		{
			queue.add(pages[j]);
		}

		System.out.print("Random Pages: ");
		System.out.print(pages[0]);
		
		for (int i = 1; i < xPages; i++) 
		{
			System.out.print(", " + pages[i]);
		}
		System.out.println();

		do 
		{
			int pageIndex = 0;

			System.out.println("FIFO: ");
			for (pageIndex = 0; pageIndex < xPages; pageIndex++) 
			{
				page = queue.remove();
				flag = true;
				if (isInFrames(page, zPageFrames, frame))
					flag = false;
				if (flag) 
				{
					frame[index] = page;
					index++;

					if (index == zPageFrames)
						index = 0;
					
					System.out.print("Frame :");
					for (int i = 0; i < zPageFrames; i++) 
					{
						System.out.print(frame[i] + " ");
					}

					System.out.print("  Page Fault!");
					System.out.println();
					pageFault++;
				} else 
				{
					System.out.print("Frame: ");
					for (int i = 0; i < zPageFrames; i++) 
					{
						System.out.print(frame[i] + " ");
					}
					System.out.println();
				}
				checkCounter++;
			}
		} while (checkCounter < xPages);		
		successRate();
	}

	/**
	 * LRU page replacement policy, fills pages[] with random page requests
	 * ranging from 0 to yPageFrames, , counts page faults, and runs success rate report
	 */
	public void lru() 
	{
		int k = 0;
		
		System.out.println();
		System.out.println("LRU Policy");

		for (int j = 0; j < xPages; j++) 
		{
			queue.add(pages[j]);
		}

		System.out.print("Random Pages: ");
		System.out.print(pages[0]);
		
		for (int i = 1; i < xPages; i++) 
		{
			System.out.print(", " + pages[i]);
		}
		
		System.out.println();

		int[] lruFrame = new int[zPageFrames];
		int[] array1 = new int[zPageFrames];
		int[] array2 = new int[zPageFrames];

		for (int i = 0; i < zPageFrames; i++) 
		{
			lruFrame[i] = -1;
			array1[i] = -1;
			array2[i] = -1;
		}
		do 
		{
			int pageIndex = 0;
			for (pageIndex = 0; pageIndex < xPages; pageIndex++) 
			{
				page = queue.remove();
				flag = true;
				if (isInFrames(page, zPageFrames, lruFrame))
					flag = false;

				for (int i = 0; i < zPageFrames && flag; i++) 
				{
					if (lruFrame[i] == array1[zPageFrames - 1]) 
					{
						k = i;
						break;
					}
				}

				if (flag) 
				{
					lruFrame[k] = page;
					System.out.print("Frame: ");
					for (int j = 0; j < zPageFrames; j++) 
					{
						System.out.print(lruFrame[j] + " ");
					}
					pageFault++;
					System.out.print("  Page Fault!");
					System.out.println();
				} 
				else 
				{
					System.out.print("Frame: ");
					for (int i = 0; i < zPageFrames; i++) 
					{
						System.out.print(lruFrame[i] + " ");
					}
					System.out.println();
				}
				int p = 1;
				array2[0] = page;
				for (int i = 0; i < array1.length; i++) 
				{
					if (page != array1[i] && p < zPageFrames) 
					{
						array2[p] = array1[i];
						p++;
					}
				}
				for (int i = zPageFrames - 1; i >= 0; i--) 
				{
					array1[i] = array2[i];
				}
				checkCounter++;
			}
		} while (checkCounter < xPages);
		successRate();
	}
	
	
	/**
	 * Custom page replacement policy, fills pages[] with random page requests
	 * ranging from 0 to yPageFrames, counts page faults, and runs success rate report
	 */
	public void userDefined() 
	{
		int k = 0;
		
		System.out.println();
		System.out.println("Custom Policy");

		for (int j = 0; j < xPages; j++) 
		{
			queue.add(pages[j]);
		}

		System.out.print("Random Pages: ");
		System.out.print(pages[0]);
		
		for (int i = 1; i < xPages; i++) 
		{
			System.out.print(", " + pages[i]);
		}
		
		System.out.println();

		int[] lruFrame = new int[zPageFrames];
		int[] array1 = new int[zPageFrames];
		int[] array2 = new int[zPageFrames];

		for (int i = 0; i < zPageFrames; i++) 
		{
			lruFrame[i] = -1;
			array1[i] = -1;
			array2[i] = -1;
		}
		do 
		{
			int pageIndex = 0;
			for (pageIndex = 0; pageIndex < xPages; pageIndex++) 
			{
				page = queue.remove();
				flag = true;
				if (isInFrames(page, zPageFrames, lruFrame))
					flag = false;

				for (int i = 0; i < zPageFrames && flag; i++) 
				{
					if (lruFrame[i] == array1[zPageFrames - 1]) 
					{
						k = i;
						break;
					}
				}

				if (flag) 
				{
					lruFrame[k] = page;
					System.out.print("Frame: ");
					for (int j = 0; j < zPageFrames; j++) 
					{
						System.out.print(lruFrame[j] + " ");
					}
					pageFault++;
					System.out.print("  Page Fault!");
					System.out.println();
				} 
				else 
				{
					System.out.print("Frame: ");
					for (int i = 0; i < zPageFrames; i++) 
					{
						System.out.print(lruFrame[i] + " ");
					}
					System.out.println();
				}
				int p = 1;
				array2[0] = page;
				for (int i = 0; i < array1.length; i++) 
				{
					if (page != array1[i] && p < zPageFrames) 
					{
						array2[p] = array1[i];
						p++;
					}
				}
				for (int i = 0; i < zPageFrames; i++) 
				{
					array1[i] = array2[i];
				}
				checkCounter++;
			}
		} while (checkCounter < xPages);
		successRate();
	}

	/**
	 * Prints summary of all sequence executions: Page Fault Average, Average
	 * Success Rate, and Average Failure rate for FIFO, LRU, and Custom
	 */
	public void overallPerformance() 
	{
		System.out.println();

		System.out.println("***FIFO***");
		System.out.println("Page Fault Average: " + faultFIFO);
		System.out.println("Average Success Rate FIFO: " + (xPages - faultFIFO) + "/" + xPages
				+ "   Success Rate FIFO: " + decimal.format(successFIFO) + "%");
		System.out.println("Average Failure Rate FIFO: " + faultFIFO + "/" + xPages + "   Failure Rate FIFO: "
			 + decimal.format(failureFIFO) + "%");
		System.out.println();

		System.out.println("***LRU***");
		System.out.println("Page Fault Average: " + faultLRU);
		System.out.println("Average Success Rate LRU: " + (xPages - faultLRU) + "/" + xPages
				+ "   Success Rate LRU: " + decimal.format(successLRU) + "%");
		System.out.println("Average Failure Rate LRU: " + faultLRU + "/" + xPages + "   Failure Rate LRU: "
				+ decimal.format(failureLRU) + "%");
		System.out.println();

		System.out.println("***CUSTOM***");
		System.out.println("Page Fault Average: " + faultCustom);
		System.out.println("Average Success Rate Custom: " + (xPages - faultCustom) + "/" + xPages
				+ "   Success Rate Custom: " + decimal.format(successCustom) + "%");
		System.out.println("Average Failure Rate Custom: " + faultCustom + "/" + xPages
				+ "   Failure Rate Custom: " + decimal.format(failureCustom) + "%");
	}

	/**
	 * Used for printing results after each policy execution Displays page fault
	 * count, and success and failure rates
	 */
	public void successRate() 
	{
		System.out.println();
		System.out.println("Page faults: " + integer.format(pageFault));

		success = xPages - pageFault;
		successRate = success / xPages;
		failureRate = pageFault / xPages;

		System.out.println("Success Number: " + integer.format(success) + "/" + xPages + "   Success Rate: "
			 + integer.format(successRate* 100) + "%");
		System.out.println(
				"Failure Number: " + integer.format(pageFault) + "/" + xPages + "   Failure Rate: " 
						+ integer.format(failureRate * 100) + "%");
	}
}
