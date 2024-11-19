package com.snipper.snippets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipper.snippets.model.Snippet;
import com.snipper.snippets.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {
    @Autowired
    SnippetRepository snippetRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, PasswordEncoder passwordEncoder) {
        this.snippetRepository = snippetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cleanUpSnippetTable() {
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 0;");
        jdbcTemplate.execute("DELETE FROM snippets");
        jdbcTemplate.execute("ALTER TABLE snippets AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 1;");
    }

    public void importJsonData(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Snippet> snippets = objectMapper.readValue(inputStream, new TypeReference<List<Snippet>>() {
        });
        for (Snippet snippet : snippets) {
            if (!snippet.getCode().isEmpty()) {
                String hashCodeSnippet = passwordEncoder.encode(snippet.getCode());
                snippet.setCode(hashCodeSnippet);
            }
//            if (!snippet.getLanguage().isEmpty()) {
//                String hashLanguageSnippet = passwordEncoder.encode(snippet.getLanguage());
//                snippet.setLanguage(hashLanguageSnippet);
//            }
            snippetRepository.save(snippet);
        }
    }

    public List<Snippet> findAllSnippets() {
        return snippetRepository.findAll();
    }

    public Optional<Snippet> findSnippetById(Long id) {
        return snippetRepository.findById(id);
    }

    public Snippet addSnippet(Snippet snippet) {
        return snippetRepository.save(snippet);
    }

    public void deleteSnippetById(Long id) {
        snippetRepository.deleteById(id);
    }

    public List<Snippet> getSnippetsByLanguage(String lang) {
        List<Snippet> snippets = snippetRepository.findAllByLanguage(lang);
        if (snippets.isEmpty()) {
           System.out.println("No snippet available");
        }
        return snippets;
    }

}
