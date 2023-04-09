package com.loginsocial.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    public User() {
    }

    public User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.emailVerified = builder.emailVerified;
        this.imageUrl = builder.imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static class Builder {
        //mandatory
        private Long id;
        private final String name;
        private final String email;

        //optional
        private String password;
        private String imageUrl;
        private Boolean emailVerified;
        private AuthProvider provider;

        public Builder(final Long id, final String name, final String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder emailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder provider(AuthProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
