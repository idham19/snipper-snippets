package com.snipper.snippets.repository;


import com.snipper.snippets.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    List<Snippet> findAllByLanguage(String language);


}
