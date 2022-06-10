package com.gemini.gembook.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gemini.gembook.model.GemFiles;
import com.gemini.gembook.repository.PhotoRepository;

/**
 * Service class to serve request for file uploaded on the server to a post.
 */
@Service
public class PhotoService {
	

	private PhotoRepository photoRepository;
	private final Logger logger = LoggerFactory.getLogger(PhotoService.class);
	
	
	@Autowired
	public PhotoService(PhotoRepository photoRepository){
		this.photoRepository = photoRepository;
	}
	
	/**
	 * Adds the photo uploaded in a post to database.
	 * @param gemFiles
	 * @return Photo object
	 */
	public GemFiles addFile(GemFiles gemFiles) {
		GemFiles uploadGemFiles = null;
		try {
			uploadGemFiles = photoRepository.save(gemFiles);
		}
        catch (Exception e){
            logger.error("Exception in addFile() : {}",e.getMessage());
        }
		return uploadGemFiles;
	}
	
	public List<GemFiles> getFileByPostId(int PostId) {
		List<GemFiles> gemFiles = null;
		try {
			gemFiles = photoRepository.getFileByPostId(PostId);
		}
        catch (Exception e){
            logger.error("Exception in getFileByPostId() : {}",e.getMessage());
        }
		return gemFiles;
	}

}
