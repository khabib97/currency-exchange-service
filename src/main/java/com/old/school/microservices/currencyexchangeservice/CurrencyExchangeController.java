package com.old.school.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class CurrencyExchangeController { 
	
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	@Autowired
	private CurrecyExchangeRepository currecyExchangeRepository;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retriveExchangeValue(@PathVariable String from, @PathVariable String to) {
		//CurrencyExchange currencyExchange = new CurrencyExchange(1000L,from,to,BigDecimal.valueOf(50));
		logger.info("RetrieveExchangeValue called with {} to {}",from,to);
		CurrencyExchange currencyExchange = currecyExchangeRepository.findByFromAndTo(from, to);
		if(currencyExchange == null) {
			throw new RuntimeException("Unable to find data for "+from+ " to "+to );
		}
		
		String port = environment.getProperty("local.server.port");
		
		//change-kubernetes
		String host = environment.getProperty("HOSTNAME");
		String version = "v2";
		currencyExchange.setEnvironment(port + " " + version + " " + host);
		return currencyExchange;
	}
}
