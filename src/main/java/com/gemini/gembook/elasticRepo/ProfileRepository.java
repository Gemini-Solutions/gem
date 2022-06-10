package com.gemini.gembook.elasticRepo;

import com.gemini.gembook.model.EmpElasticModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends ElasticsearchRepository<EmpElasticModel,Integer> {

}




