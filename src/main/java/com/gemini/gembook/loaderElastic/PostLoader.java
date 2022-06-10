package com.gemini.gembook.loaderElastic;

import com.gemini.gembook.elasticRepo.PostElasticRepo;
import com.gemini.gembook.model.Post;
import com.gemini.gembook.model.PostElasticModel;
import com.gemini.gembook.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PostLoader {

    private final Logger logger = LoggerFactory.getLogger(PostLoader.class);

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostElasticRepo postElasticRepo;

    //@PostConstruct
    @Scheduled(cron = "0 0 10 * * MON-FRI")     //second, minute, hour, day, month, weekday
    public void indexPostData() {
        try {
            System.out.println("Loading Post Data..........");
            List<Post> postList = postRepository.findAll();
            //postElasticRepo.deleteAll();
            for (Post post : postList) {
                PostElasticModel model = new PostElasticModel();
                model.setId(post.getPostId());
                model.setPostContent(post.getPostContent());
                postElasticRepo.save(model);
            }
            System.out.println("Post Data indexed successfully");

        } catch (Exception e) {
            logger.error("Exception in indexing post data to elastic");
        }
    }
}