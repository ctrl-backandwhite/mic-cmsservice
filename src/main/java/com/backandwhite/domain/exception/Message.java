package com.backandwhite.domain.exception;

import com.backandwhite.common.exception.BusinessException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public enum Message {

    GIFT_CARD_EXPIRED("CM001", "Gift card has expired"), GIFT_CARD_INSUFFICIENT_BALANCE("CM002",
            "Insufficient gift card balance. Available: %s"), GIFT_CARD_INACTIVE("CM003",
                    "Gift card is not active"), GIFT_CARD_ALREADY_ACTIVATED("CM004",
                            "Gift card is already activated"), NEWSLETTER_ALREADY_SUBSCRIBED("CM005",
                                    "Email is already subscribed"), NEWSLETTER_NOT_FOUND("CM006",
                                            "Subscriber not found"), SLIDE_POSITION_CONFLICT("CM007",
                                                    "Slide position %d is already taken"), CAMPAIGN_DATE_CONFLICT(
                                                            "CM008",
                                                            "Campaign end date must be after start date"), LOYALTY_INSUFFICIENT_POINTS(
                                                                    "CM009",
                                                                    "Insufficient loyalty points. Available: %d"), LOYALTY_TIER_OVERLAP(
                                                                            "CM010",
                                                                            "Loyalty tier points range overlaps with existing tier"), TEMPLATE_NAME_EXISTS(
                                                                                    "CM011",
                                                                                    "Email template with name '%s' already exists"), SETTING_NOT_FOUND(
                                                                                            "CM012",
                                                                                            "Setting section '%s' not found"), SEO_PATH_EXISTS(
                                                                                                    "CM013",
                                                                                                    "SEO page for path '%s' already exists"), FLOW_NOT_EMPTY(
                                                                                                            "CM014",
                                                                                                            "Cannot delete flow with active references"), CURRENCY_RATE_NOT_FOUND(
                                                                                                                    "CM015",
                                                                                                                    "Currency rate for code '%s' not found"), CURRENCY_SYNC_FAILED(
                                                                                                                            "CM016",
                                                                                                                            "Currency rate sync failed: %s");

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
