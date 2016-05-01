package com.transactions;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.mongodb.FongoDB;
import com.mongodb.Mongo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TransactionBoot.class)
@WebIntegrationTest("server.port:9000")
public class TransactionsTests{
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private FongoDB db;
	
	@Before
	public void setUp() {
	    db = (FongoDB) new Fongo("test").getDB("test");
	    db.getMongo();
	}

	@Configuration
	@EnableMongoRepositories
	@ComponentScan(basePackageClasses = { AccountsRepo.class })
	static class MongoConfiguration extends AbstractMongoConfiguration {

		@Override
		protected String getDatabaseName() {
			return "testdb";
		}

		@Override
		public Mongo mongo() {
			return new Fongo("testdb").getMongo();
		}

		@Override
		protected String getMappingBasePackage() {
			return "com.spring.data.mongo";
		}
	}
	@Test
	public void testRetrieveAllTransactionsService() throws Exception {
		
		RestTemplate rt = new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.getMessageConverters().add(new StringHttpMessageConverter());

		Transactions transactions = new Transactions();
		transactions.setAmount(1000.00);
		transactions.setFromAccountId("1234");
		transactions.setToAccount("123");
		Transactions transactionsnew = rt.postForObject("http://localhost:9000/transactions", transactions, Transactions.class);
		
		assertNotNull("Transaction Id should not be null", transactionsnew.getId());
		
	}
}