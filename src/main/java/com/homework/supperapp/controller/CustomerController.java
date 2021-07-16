package com.homework.supperapp.controller;

import com.homework.supperapp.model.Article;
import com.homework.supperapp.model.Customer;
import com.homework.supperapp.model.DatabaseSeq;
import com.homework.supperapp.repository.ArticleRepository;
import com.homework.supperapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final MongoOperations mongoOperations;
    private final ArticleRepository articleRepository;

    public long generateSequence(String seqName) {
        DatabaseSeq counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                                                            new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                                                            DatabaseSeq.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    @GetMapping
    public String doSomething() {
        // mongo
        long id = generateSequence(Customer.SEQUENCE_NAME);
        customerRepository.insert(Customer.builder()
                                          .id(id)
                                          .lastName("Name" + id)
                                          .firstName("Surname" + id)
                                          .build());
        customerRepository.findAll();

        // elastic
        Article article = new Article();
        article.setTitle("Article" + id);
        article.setAuthor("John Smith" + id);
        articleRepository.save(article);
        articleRepository.findAll();
        return "Added new customer and article";
    }

}
