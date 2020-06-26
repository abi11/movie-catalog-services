package com.abhi.microservices.moviecatalogservices.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.abhi.microservices.moviecatalogservices.models.Rating;
import com.abhi.microservices.moviecatalogservices.models.UserRatings;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class UserRatingInfo {
	
	@Autowired
	RestTemplate restTemplate;

	public UserRatingInfo() {
		// TODO Auto-generated constructor stub
	}

	@HystrixCommand(fallbackMethod = "getFallbackUserRating",
			  	threadPoolKey="userRatingInfoPool",
			  	threadPoolProperties= {
			  			@HystrixProperty(name="coreSize", value="20"),
						@HystrixProperty(name="maxQueueSize",value="10")
			  	},
			commandProperties = {
					@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"),
					@HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="5"),
					@HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value ="50"),
					@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="5000")
			})
	public UserRatings getUserRating(@PathVariable("userId") String userId) {
		return restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId,UserRatings.class);
		
		
	}
	
	public UserRatings getFallbackUserRating(@PathVariable("userId") String userId) {
		
		UserRatings userRating= new UserRatings();
		userRating.setUserId(userId);
		userRating.setUserRating(Arrays.asList(new Rating()));
		return userRating;
	}
}
