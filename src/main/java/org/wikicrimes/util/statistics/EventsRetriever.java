package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.util.List;

public abstract class EventsRetriever<T> {

	protected List<Point> points;
	protected List<T> events;
	public abstract List<Point> getPoints();
	public abstract List<T> getEvents();
	
}
