package com.backandwhite.application.usecase.impl;

import com.backandwhite.common.domain.model.PageResult;
import com.backandwhite.application.usecase.ContactUseCase;
import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.domain.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backandwhite.common.exception.Message.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContactUseCaseImpl implements ContactUseCase {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public ContactMessage submit(ContactMessage message) {
        message.setRead(false);
        return contactRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessage findById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("ContactMessage", id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<ContactMessage> findAll(int page, int size, String sortBy, boolean ascending) {
        Pageable pageable = PageRequest.of(page, size,
                ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        return PageResult.from(contactRepository.findAll(pageable));
    }

    @Override
    @Transactional
    public void markAsRead(String id) {
        ContactMessage message = contactRepository.findById(id)
                .orElseThrow(() -> ENTITY_NOT_FOUND.toEntityNotFound("ContactMessage", id));
        message.setRead(true);
        contactRepository.update(message);
    }
}
