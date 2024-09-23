package com.ex.sothat.service;

import com.ex.sothat.domain.dao.repository.OauthRepository;

import java.util.Map;

public class GoogleService implements OauthRepository {

    private Map<String, Object> attributes;

    public GoogleService(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}