package com.gemini.gembook.repository;

import com.gemini.gembook.model.ToolLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolLinksRepository extends JpaRepository<ToolLinks, String> {
}
