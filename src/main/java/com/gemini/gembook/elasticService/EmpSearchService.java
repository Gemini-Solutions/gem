package com.gemini.gembook.elasticService;

import com.gemini.gembook.model.EmpElasticModel;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmpSearchService {
    @Autowired
    ElasticsearchOperations elasticsearchRestTemplate;
    private final Logger logger = LoggerFactory.getLogger(EmpSearchService.class);

    public SearchHits<EmpElasticModel> multiMatchQuery(String input) {
        String query = ".*" + input + ".*";
        SearchHits<EmpElasticModel> list;
        try {
//            NativeSearchQuery search = new NativeSearchQueryBuilder()
//                    .withQuery(QueryBuilders.matchPhrasePrefixQuery("name", query))
//                    //.withQuery(QueryBuilders.matchQuery("name", query)
//                            //.field("name")
//                            //.prefixLength(3)
//                            //.lenient(true)
//                            //.minimumShouldMatch("2%"))
//                    //.fuzziness(Fuzziness.ONE)
//                    .build();

            NativeSearchQuery search1 = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.multiMatchQuery(query,"name","teamName","designation","skills","achievements","trainingsDone,hobbies,gemTalkDelivered")
                            .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX).prefixLength(3)).build();

            list=elasticsearchRestTemplate.search(search1,EmpElasticModel.class,
                    IndexCoordinates.of("gemini_employee"));
            return list;
        }catch (Exception e){
            logger.error("Exception in multiMatchQuery method: {}",e.getMessage());
        }
        return null;
    }
}