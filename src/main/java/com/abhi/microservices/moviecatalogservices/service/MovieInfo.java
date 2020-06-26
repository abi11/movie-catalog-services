package com.abhi.microservices.moviecatalogservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abhi.microservices.moviecatalogservices.models.CatalogItem;
import com.abhi.microservices.moviecatalogservices.models.Movie;
import com.abhi.microservices.moviecatalogservices.models.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class MovieInfo {

	@Autowired
	RestTemplate restTemplate;
	
	public MovieInfo() {
		// TODO Auto-generated constructor stub
	}

	
	@HystrixCommand(fallbackMethod = "getFallbackCatalog",
			        threadPoolKey="movieInfoPool",
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
	public CatalogItem getCatalogItem(Rating rating) {
		Movie movie=restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(),Movie.class);
		return new CatalogItem(movie.getName(),"Desc",rating.getRating());
	}
	
	public CatalogItem getFallbackCatalog(Rating rating){
		
		return new CatalogItem("Movie Not found", " ", rating.getRating());
				
	}
}
