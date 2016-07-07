package ch.skema.lager.event;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;
import com.github.wolfie.blackboard.annotation.ListenerMethod;

public class KategorieEvent implements Event {

	public interface KategorieEventListener extends Listener {
		@ListenerMethod
		public void reloadEntries(KategorieEvent event);
	}
}
