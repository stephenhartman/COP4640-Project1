package project1;

import java.util.Random;
import java.util.Scanner;

public class Project1 {

	protected static int numPages = 0;
	protected static int pageRange = 0;
	protected static int pageFrames = 2;
	private static Scanner sc ;
	
	
	public static void main(String[] args) 
	{
		execute();
	}
	
	public static void execute() 
	{
		sc = new Scanner(System.in);
		System.out.print("Please enter the number of pages you wish to utilize: ");
		numPages = sc.nextInt();
		System.out.println();
		System.out.print("Please enter the range of pages you wish to utilize 0 to : ");
		pageRange = sc.nextInt();
		System.out.println();

		System.out.print("Please enter the number of page frames you wish to utilize: ");
		pageFrames = sc.nextInt();
		
		int[] pageArray = createPages(numPages, pageRange);
		
		
		for (int i = 0; i < pageArray.length; i++)
		{
			System.out.println(pageArray[i]);		
		}
		
	}
	
	public static int[] createPages(int numPages, int pageRange)
	{
		
		int[] pageArray = new int[numPages];
		for (int i = 0; i < pageArray.length; i++)
		{
			pageArray[i] = randInt(0, pageRange);
		}
		return pageArray;
	}
	
	public static int randInt(int min, int max)
	{
		Random random = new Random();
		int randomNum =  random.nextInt((max - min) + 1) + min;
		
		return randomNum;
	}

}
