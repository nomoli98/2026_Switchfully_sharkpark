package com.application.sharkpark.enums.member;

import com.application.sharkpark.enums.Feature;

import java.util.List;

public enum Role {
    ADMIN(List.of(Feature.CREATE_MEMBER, Feature.CREATE_DIVISION, Feature.CREATE_PARKING_LOT,
                  Feature.VIEW_ALL_MEMBERS)),
    MANAGER(List.of(Feature.CREATE_DIVISION, Feature.CREATE_PARKING_LOT, Feature.VIEW_ALL_MEMBERS, Feature.GET_DIVISION)),
    MEMBER(List.of(Feature.CREATE_MEMBER));

    private final List<Feature> features;

    Role(List<Feature> features) {
        this.features = features;
    }

    public boolean containsFeature(Feature feature) {
        return features.contains(feature);
    }
}
