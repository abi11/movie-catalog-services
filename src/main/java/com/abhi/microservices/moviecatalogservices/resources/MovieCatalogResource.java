package com.abhi.microservices.moviecatalogservices.resources;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.abhi.microservices.moviecatalogservices.models.CatalogItem;
import com.abhi.microservices.moviecatalogservices.models.Movie;
import com.abhi.microservices.moviecatalogservices.models.Rating;
import com.abhi.microservices.moviecatalogservices.models.UserRatings;
import com.abhi.microservices.moviecatalogservices.service.MovieInfo;
import com.abhi.microservices.moviecatalogservices.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	WebClient.Builder webClientBuilder;
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	MovieInfo movieInfo;
	
	@Autowired
	UserRatingInfo userRatingInfo;
	
//	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	@RequestMapping ("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		WebClient.Builder builder=WebClient.builder();
		UserRatings ratings = userRatingInfo.getUserRating(userId);
		//UserRatings ratings=restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId,UserRatings.class);
		//List<Rating> ratings=restTemplate.getForEntity("http://localhost:8059/ratingsdata/", userId,ParameterizedType<List<Rating> user>.rating);
				//Arrays.asList(new Rating("1234",4),				new Rating("5678",3));
				
		//Movie movie=restTemplate.getForObject("http://localhpost:8069/movies/"+ ratings.g,Movie.class);
		
		return ratings.getUserRating().stream().map(rating -> {
			return movieInfo.getCatalogItem(rating);
//				Movie movie=restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(),Movie.class);
////			Movie movie=  webClientBuilder.build()
////			   .get()
////			   .uri("http://localhost:8069/movies/"+ rating.getMovieId())
////			   .retrieve()
////			   .bodyToMono(Movie.class)
////			   .block();
//			
//			return new CatalogItem(movie.getName(),"Desc",rating.getRating());
				
//				.map(rating -> {
//			Movie movie=restTemplate.getForObject("http://localhpost:8069/movies/"+ rating.getMovieId(),Movie.class);
//			  new CatalogItem(movie.getName(),"Test",rating.getRating());
		})
		.collect(Collectors.toList());		
//		return Collections.singletonList(
//				new CatalogItem("Transformers", "Test", 4));
				
	}
	
	
//public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
//		
//		return Arrays.asList(new CatalogItem("No Movie", " ", 0));
//				
//	}
	




}
