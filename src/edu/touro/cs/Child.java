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
		}
		catch(InterruptedException e) {
		}
	}

	private void wakeup() throws InterruptedException {
		Thread.sleep((int) (rand.nextDouble() * 1000));
		System.out.println(name + " is awake.");
		synchronized(childCounter) {
			childCounter++;
			if(childCounter >= 6) {
				synchronized(parentLock) {
					parentLock.notifyAll();
				}
			}
		}
	}

	private void waitForBreakfast() throws InterruptedException {
		synchronized(this) {
			this.wait();
			System.out.println(name + " is fed and off to school!");
		}
	}
}
