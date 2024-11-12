package com.snipper.snippets.repository;


import com.snipper.snippets.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Optional<Snippet> findByLanguage(String language);


}
