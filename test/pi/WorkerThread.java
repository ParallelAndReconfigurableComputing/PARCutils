package pi;

public class WorkerThread extends Thread {
		
	private ParIterator<String> pi = null;
	private int id = -1;

	public WorkerThread(int id, ParIterator<String> pi) {
		this.id = id;
		this.pi = pi;
	}

	public void run() {
		while (pi.hasNext()) {
			String element = pi.next();
			System.out.println("Hello from Thread "+id+", who got element: "+element);
			
			// slow down the threads (to illustrate the scheduling)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("    Thread "+id+" has finished.");
	}
}

