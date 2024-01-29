package jp.co.maru.component.batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jp.co.maru.component.scraping.Scraping;

@Component
public class PySettings implements CommandLineRunner{
	private Scraping scraping;
	
	public PySettings(Scraping scraping) {
		this.scraping = scraping;
	}
	
	@Override
	public void run(String... args) {
		scraping.fetchData();
    	
    	System.out.println("start application");
	}
}
