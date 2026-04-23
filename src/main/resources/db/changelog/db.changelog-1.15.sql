--liquibase formatted sql
--changeset Jesus.Finol:16-gift-card-email-sent-flag
ALTER TABLE gift_cards
    ADD COLUMN IF NOT EXISTS email_sent  BOOLEAN      NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS buyer_email VARCHAR(255);

-- Existing rows predate the scheduler; mark them as already-sent so the
-- scheduler does not re-emit Kafka events for gift cards bought before
-- the scheduled-send feature shipped.
UPDATE gift_cards SET email_sent = TRUE WHERE email_sent = FALSE;

CREATE INDEX IF NOT EXISTS idx_gift_cards_pending_send
    ON gift_cards(status, email_sent, send_date)
    WHERE email_sent = FALSE;
