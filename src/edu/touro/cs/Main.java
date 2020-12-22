package edu.touro.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
	public static Child[] children = new Child[10];
	public static Adult[] adults = new Adult[2];
	public static Random rand = new Random();
	public static Semaphore dinnerTable = new Semaphore(4);

	public static List<Child> breakfastList = new ArrayList<>(),
			bathList = new ArrayList<>(),
			bookList = new ArrayList<>();

	public static Object parentLock = new Object(),
			childLock = new Object();

	public static void main(String[] args) {
		for(int i = 0; i < children.length; i++) {
			children[i] = new Child("Child " + (i + 1));
			children[i].start();
		}
		for(int i = 0; i < adults.length; i++) {
			adults[i] = new Adult("Adult " + (i + 1));
			adults[i].start();
		}
	}
}
