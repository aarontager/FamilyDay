package edu.touro.cs;

import java.util.ArrayList;
import java.util.List;

import static edu.touro.cs.Main.*;

public class Adult extends Thread {
	private String name;
	private List<Child> readingList;

	public Adult(String s) {
		super(s);
		name = s;
		readingList = new ArrayList<>();
	}

	public void run() {
		try {
			wakeup();
			Thread.sleep(750);
			feedChildren();
			Thread.sleep(750);
			waitForKids();
			Thread.sleep(750);
			bathTime();
//			Thread.sleep(750);
			eatDinner();
			waitForKids();
			Thread.sleep(750);
			readBooks();
		}
		catch(InterruptedException e) {
		}
	}

	private void wakeup() throws InterruptedException {
		synchronized(parentLock) {
			parentLock.wait();
			System.out.println(name + " is awake.");
		}
	}

	private void feedChildren() throws InterruptedException {
		synchronized(parentLock) {
			while(breakfastList.size() > 0) {
				Child temp = breakfastList.get(0);
				System.out.println(name + " is feeding " + temp.getName() + ".");
				synchronized(temp) {
					temp.notify();
				}
				parentLock.notify();
				parentLock.wait();
				Thread.sleep((int) (rand.nextDouble() * 250));
			}
			parentLock.notify();
		}
	}

	private void waitForKids() throws InterruptedException {
		synchronized(parentLock) {
			parentLock.wait();
		}
	}

	private void bathTime() throws InterruptedException {
		synchronized(parentLock) {
			while(bathList.size() > 0) {
				Child temp = bathList.get(0);
				System.out.println(name + " is bathing " + temp.getName() + ".");
				synchronized(temp) {
					temp.notify();
				}
				parentLock.notify();
				parentLock.wait();
				Thread.sleep((int) (rand.nextDouble() * 250));
			}
			parentLock.notify();
			synchronized(childLock) {
				childLock.notifyAll();
			}
		}
	}

	private void eatDinner() throws InterruptedException {
		dinnerTable.acquire();
		System.out.println(name + " is eating.");
		Thread.sleep((int) (rand.nextDouble() * 1500));
		System.out.println(name + " finished dinner!");
		dinnerTable.release();
	}

	private void readBooks() throws InterruptedException {
		System.out.println("here!");
		StringBuilder names = new StringBuilder();
		synchronized(parentLock) {
			while(bookList.size() > 0) {
				Child temp = bookList.remove(0);
				readingList.add(temp);
				names.append(temp.getName() + ", ");
				parentLock.notify();
				parentLock.wait();
			}
			parentLock.notify();
		}
		names.setLength(names.length() - 2);
		System.out.println(name + " is reading to " + names.toString());
		Thread.sleep((int) (rand.nextDouble() * 1500));
		System.out.println(name + " finished reading.");
		for(Child c : readingList) {
			synchronized(c) {
				c.notify();
			}
		}
	}
}
