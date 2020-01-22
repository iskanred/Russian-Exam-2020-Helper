package com.iskandev.examrus;

import androidx.annotation.NonNull;

public enum LogTag {
    ERROR("error_tag"),
    INFO("info_tag"),
    DEBUG("debug_tag");

    private final String tagName;

    LogTag(final String tagName) {
        this.tagName = tagName;
    }

    @Override
    @NonNull
    public String toString() {
        return tagName;
    }
}
