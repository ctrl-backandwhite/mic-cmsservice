package com.backandwhite.infrastructure.db.postgres.repository.impl;

import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.domain.repository.ContactRepository;
import com.backandwhite.infrastructure.db.postgres.mapper.ContactInfraMapper;
import com.backandwhite.infrastructure.db.postgres.repository.ContactMessageJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactRepositoryImpl implements ContactRepository {

    private final ContactMessageJpaRepository jpa;
    private final ContactInfraMapper mapper;

    @Override
    public ContactMessage save(ContactMessage message) {
        message.setId(UUID.randomUUID().toString());
        return mapper.toDomain(jpa.save(mapper.toEntity(message)));
    }

    @Override
    public Optional<ContactMessage> findById(String id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<ContactMessage> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public ContactMessage update(ContactMessage message) {
        return mapper.toDomain(jpa.save(mapper.toEntity(message)));
    }
}
