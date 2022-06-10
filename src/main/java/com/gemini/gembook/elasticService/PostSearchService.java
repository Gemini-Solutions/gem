package com.gemini.gembook.elasticService;

import com.gemini.gembook.model.PostElasticModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PostSearchService {
    private final Logger logger = LoggerFactory.getLogger(PostSearchService.class);
    @Autowired
    ElasticsearchOperations elasticsearchOperations;
    public SearchHits<PostElasticModel> searchQuery(String input) {
        SearchHits<PostElasticModel> postList;
        /*String search = ".*" + input + ".*";
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("postContent","input"))
        List<PostElasticModel> postList=elasticsearchOperations.queryForList
                (searchQuery,PostElasticModel.class);*/
        try {
            BoolQueryBuilder query = QueryBuilders.boolQuery()
                    .should(QueryBuilders.queryStringQuery(input)
                            .lenient(true)
                            .field("postContent")
                            .minimumShouldMatch("30%")
                    );
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(query)
                    .build();
            postList = elasticsearchOperations.search(searchQuery,PostElasticModel.class,
                    IndexCoordinates.of("postgemini"));
            return postList;
        } catch (Exception e) {
            logger.error("Exception in searchQuery() method: {}",e.getMessage());
        }
        return null;
    }
}