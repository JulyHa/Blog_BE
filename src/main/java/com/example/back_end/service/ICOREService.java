package com.example.back_end.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICOREService <E,K>{
    Page<E> findAll(Pageable pageable);
    Optional<E> findById(K k);
    E save(E e);
    void deleteById(K k);
}
