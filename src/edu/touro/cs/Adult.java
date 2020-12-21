package edu.touro.cs;

import static edu.touro.cs.Main.*;

public class Adult extends Thread {
	private String name;

	public Adult(String s) {
		super(s);
		name = s;
	}

	public void run() {
		try {
			wakeup();
			Thread.sleep((int) (rand.nextDouble() * 1000));
			feedChildren();
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
			while(childCounter < 10) {}
			while(childTracker < 10) {
				System.out.println(name + " is feeding " + children[childTracker].getName() + ".");
				synchronized(children[childTracker]) {
					children[childTracker].notify();
				}
				childTracker++;
				parentLock.notify();
				parentLock.wait();
			}
			childCounter = 0;
			parentLock.notify();
		}
	}
}
