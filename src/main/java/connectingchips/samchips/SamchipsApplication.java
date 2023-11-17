package connectingchips.samchips;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class SamchipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamchipsApplication.class, args);
		//conditionCheck
	}
}
