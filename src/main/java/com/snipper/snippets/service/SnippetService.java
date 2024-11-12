package com.snipper.snippets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipper.snippets.model.Snippet;
import com.snipper.snippets.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SnippetService {

    @Autowired
    SnippetRepository snippetRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void cleanUpSnippetTable() {
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 0;");
        jdbcTemplate.execute("DELETE FROM users");
        jdbcTemplate.execute("ALTER TABLE users AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 1;");
    }

    public void importJsonData(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Snippet> snippets = objectMapper.readValue(inputStream, new TypeReference<List<Snippet>>() {
        });
        for (Snippet snippet : snippets
        ) {
            snippetRepository.save(snippet);
        }
    }
}
