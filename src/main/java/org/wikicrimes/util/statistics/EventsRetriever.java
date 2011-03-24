package org.wikicrimes.util.statistics;

import java.awt.Point;
import java.security.InvalidParameterException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.wikicrimes.util.statistics.Param.Application;

public abstract class EventsRetriever {

	public abstract List<Point> getPoints();
	
	public static EventsRetriever getEventsRetriever(HttpServletRequest request, ServletContext context) {
		Application app = Param.getApplication(request);
		switch(app) {
		case WIKICRIMES:
			return new CrimeEventsRetriever(request, context);
		case WIKIMAPPS:
			return new WikimappsEventsRetriever(request);
		default:
			throw new InvalidParameterException();
		}
		
	}
	
}
