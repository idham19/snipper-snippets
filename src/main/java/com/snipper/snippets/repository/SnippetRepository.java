package com.snipper.snippets.repository;

import com.snipper.snippets.model.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnippetRepository extends JpaRepository<Snippet,Long> {
}
