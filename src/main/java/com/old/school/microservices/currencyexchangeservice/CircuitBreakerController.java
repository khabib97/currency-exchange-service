package com.old.school.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	@Retry(name="sample-api", fallbackMethod = "hardCodedResponse")
	public String sampleApi() {
		logger.info("Sample api called received");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api-circuit-breaker")
	@CircuitBreaker(name="default ", fallbackMethod = "hardCodedResponse")
	@RateLimiter(name="default") // during 10s allow 10000 calls to sample api
	@Bulkhead(name="default")	//concurrent call allow
	public String sampleApiCircuiteBreaker() {
		logger.info("Sample api called received");
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy", String.class);
		return forEntity.getBody();
	}
	
	public String hardCodedResponse(Exception ex) {
		return "fallback-response";
	}

}
