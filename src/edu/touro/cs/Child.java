package edu.touro.cs;

import static edu.touro.cs.Main.*;

public class Child extends Thread {
	private String name;

	public Child(String s) {
		super(s);
		name = s;
	}

	public void run() {
		try {
			wakeup();
			waitForBreakfast();
			goToSchool();
			getBath();
			waitForParents();
			Thread.sleep(750);
			eatDinner();
			waitForStory();
			System.out.println(name + " is sleeping.");
		}
		catch(InterruptedException e) {
		}
	}

	private void wakeup() throws InterruptedException {
		Thread.sleep((int) (rand.nextDouble() * 1000));
		System.out.println(name + " is awake.");
		synchronized(breakfastList) {
			breakfastList.add(this);
			if(breakfastList.size() >= 6) {
				synchronized(parentLock) {
					parentLock.notifyAll();
				}
			}
		}
	}

	private void waitForBreakfast() throws InterruptedException {
		synchronized(this) {
			this.wait();
			breakfastList.remove(this);
			System.out.println(name + " is fed and off to school.");
		}
	}

	private void goToSchool() throws InterruptedException {
		Thread.sleep((int) (rand.nextDouble() * 5000));
		System.out.println(name + " is home from school");
		synchronized(bathList) {
			bathList.add(this);
			if(bathList.size() >= 10) {
				synchronized(parentLock) {
					parentLock.notify();
				}
			}
		}
	}

	private void getBath() throws InterruptedException {
		synchronized(this) {
			this.wait();
			bathList.remove(this);
			System.out.println(name + " got their bath.");
		}
	}

	private void waitForParents() throws InterruptedException {
		synchronized(childLock) {
			childLock.wait();
		}
	}

	private void eatDinner() throws InterruptedException {
		dinnerTable.acquire();
		System.out.println(name + " is eating.");
		Thread.sleep((int) (rand.nextDouble() * 1500));
		System.out.println(name + " finished dinner!");
		bookList.add(this);
		if(bookList.size() >= 10) {
			synchronized(parentLock) {
				parentLock.notifyAll();
			}
		}
		dinnerTable.release();
	}

	private void waitForStory() throws InterruptedException {
		synchronized(this) {
			this.wait();
		}
	}
}
