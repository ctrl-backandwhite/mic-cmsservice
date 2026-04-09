-- =============================================
-- Currency Rates table for multi-currency support
-- =============================================

CREATE TABLE IF NOT EXISTS currency_rates (
    id              VARCHAR(64)   PRIMARY KEY DEFAULT gen_random_uuid()::text,
    currency_code   VARCHAR(3)    NOT NULL UNIQUE,
    currency_name   VARCHAR(100)  NOT NULL,
    currency_symbol VARCHAR(10)   NOT NULL,
    country_name    VARCHAR(100)  NOT NULL,
    country_code    VARCHAR(5)    NOT NULL,
    flag_emoji      VARCHAR(10)   NOT NULL DEFAULT '🏳️',
    timezone        VARCHAR(50)   NOT NULL DEFAULT 'UTC',
    language        VARCHAR(5)    NOT NULL DEFAULT 'en',
    rate            DECIMAL(18,8) NOT NULL DEFAULT 1.00000000,
    active          BOOLEAN       NOT NULL DEFAULT false,
    last_synced_at  TIMESTAMP     NOT NULL DEFAULT NOW(),
    created_at      TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_currency_rates_active ON currency_rates(active);
CREATE INDEX IF NOT EXISTS idx_currency_rates_code   ON currency_rates(currency_code);

-- Seed USD as the base currency (always active, rate = 1.0)
INSERT INTO currency_rates (currency_code, currency_name, currency_symbol, country_name, country_code, flag_emoji, timezone, language, rate, active)
VALUES ('USD', 'United States Dollar', '$', 'United States', 'US', '🇺🇸', 'America/New_York', 'en', 1.00000000, true)
ON CONFLICT (currency_code) DO NOTHING;
