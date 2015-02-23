package com.gop.society.services;

import com.google.common.base.Strings;
import com.gop.society.exceptions.CustomNotFoundException;
import com.gop.society.models.Organization;
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
@Component("queryOrganizationService")
public class QueryOrganizationService {
    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public Pageable<Organization> getByParameters(final String name,
                                                  final String user,
                                                  final int page,
                                                  final int size) throws CustomNotFoundException {
        final Query query = new Query();

        if (!Strings.isNullOrEmpty(name)) {
            Criteria criteria = Criteria.where("name").is(name);
            query.addCriteria(criteria);
        }

        if (!Strings.isNullOrEmpty(user)) {
            Criteria criteria = Criteria.where("members").in(user);
            query.addCriteria(criteria);
        }

        if (size > 0) {
            query.skip(page * size).limit(size).with(new Sort(Sort.Direction.ASC, "login"));
        } else {
            query.skip(page * size).with(new Sort(Sort.Direction.ASC, "login"));
        }

        return new Pageable<Organization>(page, size,
                mongoTemplate.count(query, Organization.class), mongoTemplate.find(query, Organization.class));
    }
}
