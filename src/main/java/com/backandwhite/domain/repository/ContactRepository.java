package com.backandwhite.domain.repository;

import com.backandwhite.domain.model.ContactMessage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepository {
    ContactMessage save(ContactMessage message);

    Optional<ContactMessage> findById(String id);

    Page<ContactMessage> findAll(Pageable pageable);

    ContactMessage update(ContactMessage message);
}
