package com.abhi.microservices.moviecatalogservices.models;

import java.util.List;

public class UserRatings {

	private List<Rating> userRating;
	private String userId;
	
	public List<Rating> getUserRating() {
		return userRating;
	}

	public void setUserRating(List<Rating> userRating) {
		this.userRating = userRating;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserRatings() {
		// TODO Auto-generated constructor stub
	}

}
