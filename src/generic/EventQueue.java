package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	int val;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
		val=19;
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
		System.out.println("HELLO" + queue);
	}

	public void processEvents()
	{
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}

	public void printEvents()
	{
		// while(queue.isEmpty() == false)
		// {
		// 	Event event = queue.poll();
		// 	System.out.println('1');
		// }
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}