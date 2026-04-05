package com.backandwhite.application.usecase;

import com.backandwhite.api.dto.PaginationDtoOut;
import com.backandwhite.domain.model.ContactMessage;

public interface ContactUseCase {
    ContactMessage submit(ContactMessage message);

    ContactMessage findById(String id);

    PaginationDtoOut<ContactMessage> findAll(int page, int size, String sortBy, boolean ascending);

    void markAsRead(String id);
}
