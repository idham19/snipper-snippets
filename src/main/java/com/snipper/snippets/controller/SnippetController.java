package com.snipper.snippets.controller;


import com.snipper.snippets.model.Snippet;
import com.snipper.snippets.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/snippets")
public class SnippetController {

    @Autowired
    SnippetService snippetService;

    @PostMapping
    public Snippet addSnippet(@RequestBody Snippet snippet) {
        return snippetService.addSnippet(snippet);
    }

    @GetMapping
    public List<Snippet> getAllSnippets() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return snippetService.findAllSnippets();
    }

    @GetMapping("/{id}")
    public Optional<Snippet> getSnippetById(@PathVariable Long id) {
        return snippetService.findSnippetById(id);
    }

    @GetMapping("/languages/{lang}")
    public List<Snippet> getSnippetByLanguage(@PathVariable("lang") String language) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        System.out.println("Language requested: " + language);
        List<Snippet> snippets=snippetService.getSnippetsByLanguage(language);
        System.out.println("Number of snippets found: " + snippets.size());
        return snippets;
    }

}
