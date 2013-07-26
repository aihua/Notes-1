package com.java.examples.thread;

class AddThread extends Thread {
	T06SyncMethod mValue;

	public AddThread(T06SyncMethod value) {
		mValue = value;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			mValue.increment();
			try {
				long rd = (long) (Math.random() * 100.0);
				Thread.sleep(rd);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

class MinusThread extends Thread {
	T06SyncMethod mValue;

	public MinusThread(T06SyncMethod value) {
		mValue = value;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			mValue.decrement();
			try {
				long rd = (long) (Math.random() * 100.0);
				Thread.sleep(rd);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class T06SyncMethod {

	private int c = 0;

	public synchronized void increment() {
		c++;
	}

	public synchronized void decrement() {
		c--;
	}

	public synchronized int value() {
		return c;
	}

	// Display a message, preceded by
	// the name of the current thread
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

	public static void main(String args[]) {
		T06SyncMethod value = new T06SyncMethod();
		AddThread t1 = new AddThread(value);
		MinusThread t2 = new MinusThread(value);

		t1.start();
		t2.start();
		while (t1.isAlive() || t2.isAlive()) {
			try {
				t1.join(2000);
				t2.join(2000);
				threadMessage("waiting for children thread...");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		threadMessage("children exited , value: " + value.value());
	}
}
