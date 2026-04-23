--liquibase formatted sql
--changeset Jesus.Finol:17-gift-card-send-at-instant
-- send_date stores only a day; scheduling needs hour + minute precision so a
-- buyer can pick "today at 11:43" without the card firing immediately. Keep
-- send_date for legacy display but drive the scheduler off send_at. Backfill
-- existing rows (midnight UTC) so the partial index stays useful.
ALTER TABLE gift_cards
    ADD COLUMN IF NOT EXISTS send_at TIMESTAMPTZ;

UPDATE gift_cards
   SET send_at = (send_date::timestamp AT TIME ZONE 'UTC')
 WHERE send_at IS NULL AND send_date IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_gift_cards_pending_send_at
    ON gift_cards(status, email_sent, send_at)
    WHERE email_sent = FALSE;
