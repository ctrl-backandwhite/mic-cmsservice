package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactRepository {
    ContactMessage save(ContactMessage message);

    Optional<ContactMessage> findById(String id);

    Page<ContactMessage> findAll(Pageable pageable);

    ContactMessage update(ContactMessage message);
}
