package com.whiskels.voting_system.util;

import com.whiskels.voting_system.util.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    // Usage of findById over getOne
    // https://stackoverflow.com/a/47370947
    public static <T, K extends Integer> T findById(JpaRepository<T, K> repository, K id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id " + id));
    }
}
