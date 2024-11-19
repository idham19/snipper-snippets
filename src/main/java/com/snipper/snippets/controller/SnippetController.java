package com.snipper.snippets.controller;


import com.snipper.snippets.model.Snippet;
import com.snipper.snippets.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Snippet> getAllSnippets() {
        return snippetService.findAllSnippets();
    }

    @GetMapping("/{id}")
    public Optional<Snippet> getSnippetById(@PathVariable Long id) {
        return snippetService.findSnippetById(id);
    }

    @GetMapping("/{lang}")
    public List<Snippet> getSnippetByLanguage(String language) {
        return snippetService.getSnippetsByLanguage(language);
    }

}
