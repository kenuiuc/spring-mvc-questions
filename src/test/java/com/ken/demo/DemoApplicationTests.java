package com.ken.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@Slf4j
@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void catchAgain() {
		try {
			throw new SQLException();
		} catch (SQLException sqlEx) {
			log.error("Caught SQLException", sqlEx);
		} catch (Throwable th) {
			log.error("Caught throwable", th);
		}
	}

	@Test
	void testReThrow() throws SQLException {
		try {
			throw new SQLException("conflict");
		} catch (SQLException ex) {
			if (ex.getMessage().equals("conflict")) {
				log.error("Caught conflict SQLException", ex);
			} else {
				throw ex;
			}
		} catch (Throwable th) {
			// this wont catch the re-throw-ed exception from the same level catch block !!!
			log.error("Caught Throwable", th);
		}
	}

}
