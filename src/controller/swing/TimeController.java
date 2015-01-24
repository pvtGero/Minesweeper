package controller.swing;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import model.IMineFacade;
import model.events.ClearEvent;
import model.events.GameOverEvent;
import model.events.GameWonEvent;
import model.events.StartGameEvent;
import view.swing.UiFacade;

public class TimeController implements Observer, Runnable {

	private IMineFacade domainHandler;
	private UiFacade uiHandler;

	private ScheduledExecutorService executor;

	/**
	 * Constructs and initializes a new controller for the timer.
	 * 
	 * @param domainHandler
	 *            The model component of the mvc architecture pattern
	 * @param uiHandler
	 *            The view component of the mvc architecture pattern
	 */
	public TimeController(IMineFacade domainHandler, UiFacade uiHandler) {
		this.domainHandler = domainHandler;
		this.uiHandler = uiHandler;
		
		init();
	}

	/**
	 * Adds this controller as an observer that will listen to notifications
	 * from the model and view components.
	 */
	public void addObservers() {
		domainHandler.addObserver(this);
	}

	@Override
	public void run() {
		int time = domainHandler.getCurrentTime();
		uiHandler.setTime(time);
	}
	
	/**
	 * Initialization.
	 */
	private void init(){
		executor = Executors.newSingleThreadScheduledExecutor();
		uiHandler.setTime(0);
	}

	@Override
	public void update(Observable arg0, Object hint) {

		if (hint instanceof StartGameEvent)
			executor.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);

		else if (hint instanceof GameOverEvent || hint instanceof GameWonEvent)
			executor.shutdown();
		
		else if (hint instanceof ClearEvent){
			executor.shutdown();
			init();
		}
	}
}
