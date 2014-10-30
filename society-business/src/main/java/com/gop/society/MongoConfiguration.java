package com.gop.society;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * @author GhostOfPQ
 */
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration{
    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost:27017");
    }

    @Override
    protected String getDatabaseName() {
        return "society";
    }
}
