package com.whiskels.voting_system.util;

import org.springframework.data.jpa.repository.JpaRepository;

import static com.whiskels.voting_system.util.ValidationUtil.checkNotFoundWithId;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    // Usage of findById over getOne
    // https://stackoverflow.com/a/47370947
    public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }
}
