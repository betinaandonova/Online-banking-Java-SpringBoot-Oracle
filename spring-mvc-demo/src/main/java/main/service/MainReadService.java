package main.service;

import java.util.List;
import java.util.Optional;

public interface MainReadService<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);
}