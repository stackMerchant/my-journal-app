package com.svats.journalApp.repository;

import com.svats.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForAnalysis() {
        Query query = new Query();

        Criteria validEmail = Criteria.where("email").exists(true).ne("");

        Criteria wantAnalysisValidType = Criteria.where("wantsAnalysis").type(JsonSchemaObject.Type.BOOLEAN);
        Criteria wantAnalysis = Criteria.where("wantsAnalysis").is(true);

        Criteria isUser = Criteria.where("roles").is("USER");
        Criteria isAdmin = Criteria.where("roles").is("ADMIN");
        Criteria validRole = new Criteria().orOperator(isUser, isAdmin);

        query.addCriteria(new Criteria().andOperator(
                validEmail,
                wantAnalysisValidType,
                wantAnalysis,
                validRole
        ));

        return mongoTemplate.find(query, User.class);
    }

}
