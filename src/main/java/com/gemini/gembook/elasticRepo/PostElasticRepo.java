package com.gemini.gembook.elasticRepo;

import com.gemini.gembook.model.PostElasticModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostElasticRepo extends ElasticsearchRepository<PostElasticModel,Integer> {
}
