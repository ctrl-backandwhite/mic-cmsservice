-- =============================================
-- mic-cmsservice: All tables
-- =============================================

-- D.1 Slides
CREATE TABLE IF NOT EXISTS slides (
    id              VARCHAR(64) PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    subtitle        VARCHAR(500),
    image_url       VARCHAR(500) NOT NULL,
    link            VARCHAR(500),
    button_text     VARCHAR(100),
    position        INT          NOT NULL DEFAULT 0,
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_slides_active   ON slides(active);
CREATE INDEX IF NOT EXISTS idx_slides_position ON slides(position);

-- D.2 Newsletter
CREATE TABLE IF NOT EXISTS newsletter_subscribers (
    id              VARCHAR(64) PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    status          VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    subscribed_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    unsubscribed_at TIMESTAMP,
    source          VARCHAR(100),
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_newsletter_email  ON newsletter_subscribers(email);
CREATE INDEX IF NOT EXISTS idx_newsletter_status ON newsletter_subscribers(status);

-- D.3 Gift Cards
CREATE TABLE IF NOT EXISTS gift_card_designs (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    gradient_config JSONB,
    emoji           VARCHAR(10),
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS gift_cards (
    id              VARCHAR(64) PRIMARY KEY,
    code            VARCHAR(50)   NOT NULL UNIQUE,
    design_id       VARCHAR(64)   REFERENCES gift_card_designs(id),
    original_amount DECIMAL(12,2) NOT NULL,
    balance         DECIMAL(12,2) NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    buyer_id        VARCHAR(64),
    recipient_name  VARCHAR(200),
    recipient_email VARCHAR(255),
    message         TEXT,
    send_date       DATE,
    expiry_date     DATE          NOT NULL,
    activated_at    TIMESTAMP,
    created_at      TIMESTAMP     DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_gift_cards_code     ON gift_cards(code);
CREATE INDEX IF NOT EXISTS idx_gift_cards_buyer    ON gift_cards(buyer_id);
CREATE INDEX IF NOT EXISTS idx_gift_cards_status   ON gift_cards(status);

CREATE TABLE IF NOT EXISTS gift_card_transactions (
    id              VARCHAR(64) PRIMARY KEY,
    gift_card_id    VARCHAR(64)   NOT NULL REFERENCES gift_cards(id),
    type            VARCHAR(20)   NOT NULL,
    amount          DECIMAL(12,2) NOT NULL,
    order_id        VARCHAR(64),
    created_at      TIMESTAMP     DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_gc_transactions_card ON gift_card_transactions(gift_card_id);

-- D.4 Campaigns
CREATE TABLE IF NOT EXISTS campaigns (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    type            VARCHAR(30)  NOT NULL,
    value           DECIMAL(12,2),
    badge           VARCHAR(50),
    start_date      TIMESTAMP    NOT NULL,
    end_date        TIMESTAMP    NOT NULL,
    applies_to_categories JSONB,
    applies_to_products   JSONB,
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_campaigns_active ON campaigns(active);
CREATE INDEX IF NOT EXISTS idx_campaigns_dates  ON campaigns(start_date, end_date);

-- D.5 Loyalty
CREATE TABLE IF NOT EXISTS loyalty_tiers (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    min_points      INT          NOT NULL DEFAULT 0,
    max_points      INT,
    multiplier      DECIMAL(4,2) NOT NULL DEFAULT 1.0,
    benefits        JSONB,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS loyalty_rules (
    id              VARCHAR(64) PRIMARY KEY,
    action          VARCHAR(30)  NOT NULL,
    points_per_unit INT          NOT NULL DEFAULT 1,
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS loyalty_transactions (
    id              VARCHAR(64) PRIMARY KEY,
    user_id         VARCHAR(64)  NOT NULL,
    points          INT          NOT NULL,
    type            VARCHAR(20)  NOT NULL,
    description     TEXT,
    order_id        VARCHAR(64),
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_loyalty_tx_user ON loyalty_transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_loyalty_tx_type ON loyalty_transactions(type);

-- D.6 Email Templates
CREATE TABLE IF NOT EXISTS email_templates (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    trigger_type    VARCHAR(30)  NOT NULL,
    subject         VARCHAR(500) NOT NULL,
    body_html       TEXT         NOT NULL,
    variables       JSONB,
    category        VARCHAR(50),
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_email_templates_trigger  ON email_templates(trigger_type);
CREATE INDEX IF NOT EXISTS idx_email_templates_category ON email_templates(category);

-- D.7 Flows
CREATE TABLE IF NOT EXISTS flows (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    type            VARCHAR(30)  NOT NULL,
    active          BOOLEAN      NOT NULL DEFAULT true,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS flow_steps (
    id              VARCHAR(64) PRIMARY KEY,
    flow_id         VARCHAR(64)  NOT NULL REFERENCES flows(id) ON DELETE CASCADE,
    position        INT          NOT NULL DEFAULT 0,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    icon            VARCHAR(50),
    sla_days        INT,
    trigger_type    VARCHAR(50),
    send_email      BOOLEAN      NOT NULL DEFAULT false,
    send_sms        BOOLEAN      NOT NULL DEFAULT false,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_flow_steps_flow ON flow_steps(flow_id);

-- D.8 Settings
CREATE TABLE IF NOT EXISTS settings (
    key             VARCHAR(100) PRIMARY KEY,
    value           JSONB        NOT NULL,
    section         VARCHAR(30)  NOT NULL,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_settings_section ON settings(section);

-- D.8 SEO Pages
CREATE TABLE IF NOT EXISTS seo_pages (
    id              VARCHAR(64) PRIMARY KEY,
    path            VARCHAR(500) NOT NULL UNIQUE,
    meta_title      VARCHAR(200),
    meta_description TEXT,
    indexable        BOOLEAN      NOT NULL DEFAULT true,
    seo_score       INT,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);

CREATE INDEX IF NOT EXISTS idx_seo_pages_path ON seo_pages(path);

-- D.8 Contact Messages
CREATE TABLE IF NOT EXISTS contact_messages (
    id              VARCHAR(64) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    subject         VARCHAR(500) NOT NULL,
    message         TEXT         NOT NULL,
    read            BOOLEAN      NOT NULL DEFAULT false,
    created_at      TIMESTAMP    DEFAULT NOW(),
    updated_at      TIMESTAMP,
    created_by      VARCHAR(120),
    updated_by      VARCHAR(120)
);
