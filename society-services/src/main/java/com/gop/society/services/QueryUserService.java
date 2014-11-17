package com.gop.society.services;

import com.google.common.base.Strings;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.User;
import com.gop.society.utils.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * @author GhostOfPQ
 */
@Slf4j
@Component("queryUserService")
public class QueryUserService {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public Pageable<User> getByParameters(final String login,
                                          final int page,
                                          final int size) throws CustomNotFoundException {
        final Query query = new Query();

        if (!Strings.isNullOrEmpty(login)) {
            Criteria criteria = Criteria.where("login").is(login);
            query.addCriteria(criteria);
        }

        if (size > 0) {
            query.skip(page * size).limit(size).with(new Sort(Sort.Direction.ASC, "login"));
        } else {
            query.skip(page * size).with(new Sort(Sort.Direction.ASC, "login"));
        }

        return new Pageable<User>(page, size,
                mongoTemplate.count(query, User.class), mongoTemplate.find(query, User.class));
    }
}
