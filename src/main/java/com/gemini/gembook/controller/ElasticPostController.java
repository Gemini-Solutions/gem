package com.gemini.gembook.controller;

import com.gemini.gembook.dto.ApiResponse;
import com.gemini.gembook.elasticRepo.PostElasticRepo;
import com.gemini.gembook.elasticService.PostSearchService;
import com.gemini.gembook.model.Post;
import com.gemini.gembook.model.PostElasticModel;
import com.gemini.gembook.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class ElasticPostController {
    @Autowired
    PostSearchService postSearchService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostElasticRepo postElasticRepo;
    private final Logger logger = LoggerFactory.getLogger(ElasticPostController.class);
    @GetMapping("/post/{input}")
    public ApiResponse searchPost(@PathVariable String input){
        List<Post> postList=new ArrayList<>();
        SearchHits<PostElasticModel> postElasticModel=postSearchService.searchQuery(input);
        if(postElasticModel.isEmpty()){
            return new ApiResponse(HttpStatus.OK,"No matching results found");
        }
        long length=postElasticModel.getTotalHits();
        for(int i=0;i<length;i++){
            PostElasticModel elasticModel=postElasticModel.getSearchHit(i).getContent();
            Post post=postRepository.findById(elasticModel.getId()).orElse(null);
            if(post!=null)
            postList.add(post);
        }

        if(postList.isEmpty()){
            return new ApiResponse(HttpStatus.OK,"No matching results found");
        }
        return new ApiResponse(HttpStatus.OK,postList);
    }
    @GetMapping
    public Iterable<PostElasticModel> getAllIndexedData(){
        return postElasticRepo.findAll();
    }
}
