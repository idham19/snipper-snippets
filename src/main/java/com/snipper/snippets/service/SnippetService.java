package com.snipper.snippets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snipper.snippets.encryption_util.EncryptionUtil;
import com.snipper.snippets.model.Snippet;
import com.snipper.snippets.model.User;
import com.snipper.snippets.repository.SnippetRepository;
import com.snipper.snippets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {
    @Autowired
    SnippetRepository snippetRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    UserRepository userRepository;
    private EncryptionUtil encryptionUtil;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, EncryptionUtil encryptionUtil) {
        this.snippetRepository = snippetRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public void cleanUpSnippetTable() {
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 0;");
        jdbcTemplate.execute("DELETE FROM snippets");
        jdbcTemplate.execute("ALTER TABLE snippets AUTO_INCREMENT = 1;");
        jdbcTemplate.execute("SET SQL_SAFE_UPDATES = 1;");
    }

    public void importJsonData(InputStream inputStream) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Snippet> snippets = objectMapper.readValue(inputStream, new TypeReference<List<Snippet>>() {
        });
        for (Snippet snippet : snippets) {
            if (!snippet.getCode().isEmpty()) {
                String hashCodeSnippet = encryptionUtil.encrypt(snippet.getCode());
                snippet.setCode(hashCodeSnippet);
            }
            if (!snippet.getLanguage().isEmpty()) {
                String hashLanguageSnippet = encryptionUtil.encrypt(snippet.getLanguage());
                snippet.setLanguage(hashLanguageSnippet);
            }

            // Resolve and set the user
            if (snippet.getUser() != null && snippet.getUser().getId() != null) {
                User user = userRepository.findById(snippet.getUser().getId())
                        .orElseThrow(() -> new RuntimeException("User not found for ID: " + snippet.getUser().getId()));
                snippet.setUser(user);
            } else {
                throw new RuntimeException("User information is missing for snippet.");
            }
            snippetRepository.save(snippet);
        }
    }

    public List<Snippet> findAllSnippets() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        List<Snippet> snippets = snippetRepository.findAll();
        for(Snippet snippet :snippets){
            snippet.setLanguage(EncryptionUtil.decrypt(snippet.getLanguage()));
            snippet.setCode(EncryptionUtil.decrypt(snippet.getCode()));
        }
        return snippetRepository.findAll();
    }

    public Optional<Snippet> findSnippetById(Long id) {
        Optional<Snippet> snippet = snippetRepository.findById(id);

        return snippetRepository.findById(id);
    }

    public Snippet addSnippet(Snippet snippet) {
        return snippetRepository.save(snippet);
    }

    public void deleteSnippetById(Long id) {
        snippetRepository.deleteById(id);
    }

    public List<Snippet> getSnippetsByLanguage(String lang) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        List<Snippet> snippets = snippetRepository.findAllByLanguage(lang);
        if (snippets.isEmpty()) {
            System.out.println("No snippet available");
        }
        //decrypt the data before return it
        for (Snippet snippet : snippets) {
            snippet.setLanguage(encryptionUtil.decrypt(snippet.getLanguage()));
            snippet.setCode(encryptionUtil.decrypt(snippet.getCode()));
        }
        return snippets;
    }

}
