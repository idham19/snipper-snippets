package com.snipper.snippets.model;

import jakarta.persistence.*;

import java.math.BigInteger;

@Entity
@Table(name = "snippets")
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String language;
    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Foreign key in the Snippet table
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}