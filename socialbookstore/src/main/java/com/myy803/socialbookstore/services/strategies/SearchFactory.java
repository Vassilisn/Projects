package com.myy803.socialbookstore.services.strategies;

import org.springframework.stereotype.Component;

@Component
public class SearchFactory {
	
	public SearchStrategy getStrategy(String type) {
		
		if (type.equals("Exact")) {
			return new ExactSearchStrategy();
		}
		else if (type.equals("Approx")) {
			return new ApproximateSearchStrategy();
		}
		
		return null;
	}
}
