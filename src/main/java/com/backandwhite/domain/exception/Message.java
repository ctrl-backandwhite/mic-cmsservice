package com.backandwhite.domain.exception;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public enum Message {

    GIFT_CARD_EXPIRED("GC001", "Gift card has expired"),
    GIFT_CARD_INSUFFICIENT_BALANCE("GC002", "Insufficient gift card balance. Available: %s"),
    GIFT_CARD_INACTIVE("GC003", "Gift card is not active"),
    GIFT_CARD_ALREADY_ACTIVATED("GC004", "Gift card is already activated"),
    NEWSLETTER_ALREADY_SUBSCRIBED("NL001", "Email is already subscribed"),
    NEWSLETTER_NOT_FOUND("NL002", "Subscriber not found"),
    SLIDE_POSITION_CONFLICT("SL001", "Slide position %d is already taken"),
    CAMPAIGN_DATE_CONFLICT("CP001", "Campaign end date must be after start date"),
    LOYALTY_INSUFFICIENT_POINTS("LY001", "Insufficient loyalty points. Available: %d"),
    LOYALTY_TIER_OVERLAP("LY002", "Loyalty tier points range overlaps with existing tier"),
    TEMPLATE_NAME_EXISTS("ET001", "Email template with name '%s' already exists"),
    SETTING_NOT_FOUND("ST001", "Setting section '%s' not found"),
    SEO_PATH_EXISTS("SE001", "SEO page for path '%s' already exists"),
    FLOW_NOT_EMPTY("FL001", "Cannot delete flow with active references");

    private final String code;
    private final String detail;

    Message(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public String format(Object... args) {
        return String.format(this.detail, args);
    }

    public BusinessException toBusinessException(Object... args) {
        log.warn("Business rule violation: {}", format(args));
        return new BusinessException(this.code, format(args));
    }
}
