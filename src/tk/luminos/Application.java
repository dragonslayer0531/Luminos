package tk.luminos;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.graphics.display.Window;

public class Application extends Thread {
	
	private List<Action> actions = new ArrayList<Action>();
	private List<Thread> threads = new ArrayList<Thread>();
	
	public boolean application_Close = false;
	
	public Scene scene;
	
	@Override
	public void run() {
		super.run();
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	public void render(Engine engine, Window window) throws Exception {
		while (!window.shouldClose() && !application_Close) {
			engine.render(scene, window);
			for (Action action : actions) {
				if (action.actionPerformed())
					action.act();
			}
		}
	}
	
	public void addAction(Action action) {
		this.actions.add(action);
	}
	
	public Scene swapScene(Scene scene) {
		Scene old = this.scene;
		this.scene = scene;
		return old;
	}
	
	public void attachThread(Thread thread) {
		threads.add(thread);
		String os_name = System.getProperty("os.name");
		if(os_name.contains("mac")) {
			thread.run();
		}
		else {
			thread.start();
		}
	}
	
	public void close() throws Exception {
		for (Thread thread : threads) {
			thread.join();
		}
		this.join();
	}
	
	public List<Thread> getThreads() {
		return threads;
	}
	
	public Thread getThreadByName(String name) {
		for (Thread thread : threads) 
			if (thread.getName().equals(name))
				return thread;
		return null;
	}

}
