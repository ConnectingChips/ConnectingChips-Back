package connectingchips.samchips;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
class SamchipsApplicationTests {

	@Test
	@Profile("prod")
	void contextLoads() {
	}

}
