package com.backandwhite.infrastructure.db.postgres.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.domain.model.Campaign;
import com.backandwhite.domain.model.ContactMessage;
import com.backandwhite.domain.model.CurrencyRate;
import com.backandwhite.domain.model.EmailTemplate;
import com.backandwhite.domain.model.Flow;
import com.backandwhite.domain.model.FlowStep;
import com.backandwhite.domain.model.GiftCard;
import com.backandwhite.domain.model.GiftCardDesign;
import com.backandwhite.domain.model.GiftCardTransaction;
import com.backandwhite.domain.model.LoyaltyRule;
import com.backandwhite.domain.model.LoyaltyTier;
import com.backandwhite.domain.model.LoyaltyTransaction;
import com.backandwhite.domain.model.NewsletterSubscriber;
import com.backandwhite.domain.model.SeoPage;
import com.backandwhite.domain.model.Setting;
import com.backandwhite.domain.model.Slide;
import com.backandwhite.domain.valueobject.CampaignType;
import com.backandwhite.domain.valueobject.EmailTrigger;
import com.backandwhite.domain.valueobject.FlowType;
import com.backandwhite.domain.valueobject.GiftCardStatus;
import com.backandwhite.domain.valueobject.GiftCardTransactionType;
import com.backandwhite.domain.valueobject.LoyaltyAction;
import com.backandwhite.domain.valueobject.LoyaltyTransactionType;
import com.backandwhite.domain.valueobject.NewsletterStatus;
import com.backandwhite.domain.valueobject.SettingSection;
import com.backandwhite.infrastructure.db.postgres.entity.CampaignEntity;
import com.backandwhite.infrastructure.db.postgres.entity.ContactMessageEntity;
import com.backandwhite.infrastructure.db.postgres.entity.CurrencyRateEntity;
import com.backandwhite.infrastructure.db.postgres.entity.EmailTemplateEntity;
import com.backandwhite.infrastructure.db.postgres.entity.FlowEntity;
import com.backandwhite.infrastructure.db.postgres.entity.FlowStepEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardDesignEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardEntity;
import com.backandwhite.infrastructure.db.postgres.entity.GiftCardTransactionEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyRuleEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTierEntity;
import com.backandwhite.infrastructure.db.postgres.entity.LoyaltyTransactionEntity;
import com.backandwhite.infrastructure.db.postgres.entity.NewsletterSubscriberEntity;
import com.backandwhite.infrastructure.db.postgres.entity.SeoPageEntity;
import com.backandwhite.infrastructure.db.postgres.entity.SettingEntity;
import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InfraMappersTest {

    @Test
    void slide() {
        SlideInfraMapper m = new SlideInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        Slide s = Slide.builder().id("1").title("t").imageUrl("u").position(1).active(true).build();
        SlideEntity e = m.toEntity(s);
        Slide back = m.toDomain(e);
        assertThat(back.getTitle()).isEqualTo("t");
    }

    @Test
    void contact() {
        ContactInfraMapper m = new ContactInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        ContactMessage c = ContactMessage.builder().id("1").email("e@e.com").build();
        ContactMessageEntity e = m.toEntity(c);
        assertThat(m.toDomain(e).getEmail()).isEqualTo("e@e.com");
    }

    @Test
    void currencyRate() {
        CurrencyRateInfraMapper m = new CurrencyRateInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        CurrencyRate r = CurrencyRate.builder().id("1").currencyCode("USD").rate(BigDecimal.ONE).build();
        CurrencyRateEntity e = m.toEntity(r);
        assertThat(m.toDomain(e).getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void emailTemplate() {
        EmailTemplateInfraMapper m = new EmailTemplateInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        EmailTemplate t = EmailTemplate.builder().id("1").name("n").triggerType(EmailTrigger.WELCOME).build();
        EmailTemplateEntity e = m.toEntity(t);
        assertThat(m.toDomain(e).getName()).isEqualTo("n");
    }

    @Test
    void flow() {
        FlowInfraMapper m = new FlowInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        assertThat(m.toStepDomain(null)).isNull();
        assertThat(m.toStepEntity(null)).isNull();
        Flow f = Flow.builder().id("1").name("n").type(FlowType.DELIVERY).build();
        FlowEntity ent = m.toEntity(f);
        assertThat(m.toDomain(ent).getName()).isEqualTo("n");
        FlowStep step = FlowStep.builder().id("s").title("t").build();
        FlowStepEntity se = m.toStepEntity(step);
        assertThat(m.toStepDomain(se).getTitle()).isEqualTo("t");
    }

    @Test
    void giftCard() {
        GiftCardInfraMapper m = new GiftCardInfraMapperImpl();
        assertThat(m.toDesignDomain(null)).isNull();
        assertThat(m.toDesignEntity(null)).isNull();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        assertThat(m.toTransactionDomain(null)).isNull();
        assertThat(m.toTransactionEntity(null)).isNull();

        GiftCardDesign d = GiftCardDesign.builder().id("d").name("n").build();
        GiftCardDesignEntity de = m.toDesignEntity(d);
        assertThat(m.toDesignDomain(de).getName()).isEqualTo("n");

        GiftCard c = GiftCard.builder().id("g").code("C").status(GiftCardStatus.ACTIVE)
                .originalAmount(Money.of(BigDecimal.TEN)).balance(Money.of(BigDecimal.ONE)).build();
        GiftCardEntity e = m.toEntity(c);
        assertThat(m.toDomain(e).getCode()).isEqualTo("C");

        GiftCardTransaction tx = GiftCardTransaction.builder().id("tx").giftCardId("g")
                .type(GiftCardTransactionType.PURCHASE).amount(Money.of(BigDecimal.TEN)).build();
        GiftCardTransactionEntity te = m.toTransactionEntity(tx);
        assertThat(m.toTransactionDomain(te).getType()).isEqualTo(GiftCardTransactionType.PURCHASE);
    }

    @Test
    void loyalty() {
        LoyaltyInfraMapper m = new LoyaltyInfraMapperImpl();
        assertThat(m.toTierDomain(null)).isNull();
        assertThat(m.toTierEntity(null)).isNull();
        assertThat(m.toRuleDomain(null)).isNull();
        assertThat(m.toRuleEntity(null)).isNull();
        assertThat(m.toTransactionDomain(null)).isNull();
        assertThat(m.toTransactionEntity(null)).isNull();

        LoyaltyTier t = LoyaltyTier.builder().id("t").name("Bronze").multiplier(BigDecimal.ONE).build();
        LoyaltyTierEntity te = m.toTierEntity(t);
        assertThat(m.toTierDomain(te).getName()).isEqualTo("Bronze");

        LoyaltyRule r = LoyaltyRule.builder().id("r").action(LoyaltyAction.PURCHASE).pointsPerUnit(1).build();
        LoyaltyRuleEntity re = m.toRuleEntity(r);
        assertThat(m.toRuleDomain(re).getAction()).isEqualTo(LoyaltyAction.PURCHASE);

        LoyaltyTransaction tx = LoyaltyTransaction.builder().id("tx").userId("u").points(1)
                .type(LoyaltyTransactionType.EARN).build();
        LoyaltyTransactionEntity xe = m.toTransactionEntity(tx);
        assertThat(m.toTransactionDomain(xe).getPoints()).isEqualTo(1);
    }

    @Test
    void newsletter() {
        NewsletterInfraMapper m = new NewsletterInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        NewsletterSubscriber s = NewsletterSubscriber.builder().id("1").email("a@a.com").status(NewsletterStatus.ACTIVE)
                .build();
        NewsletterSubscriberEntity e = m.toEntity(s);
        assertThat(m.toDomain(e).getEmail()).isEqualTo("a@a.com");
    }

    @Test
    void seoPage() {
        SeoPageInfraMapper m = new SeoPageInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        SeoPage p = SeoPage.builder().id("1").path("/x").metaTitle("T").build();
        SeoPageEntity e = m.toEntity(p);
        assertThat(m.toDomain(e).getPath()).isEqualTo("/x");
    }

    @Test
    void setting() {
        SettingInfraMapper m = new SettingInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        Setting s = Setting.builder().key("k").section(SettingSection.GENERAL).build();
        SettingEntity e = m.toEntity(s);
        assertThat(m.toDomain(e).getKey()).isEqualTo("k");
    }

    @Test
    void campaign() {
        CampaignInfraMapper m = new CampaignInfraMapperImpl();
        assertThat(m.toDomain(null)).isNull();
        assertThat(m.toEntity(null)).isNull();
        Campaign c = Campaign.builder().id("c").name("n").type(CampaignType.PERCENTAGE)
                .value(Money.of(new BigDecimal("10"))).build();
        CampaignEntity e = m.toEntity(c);
        assertThat(m.toDomain(e).getName()).isEqualTo("n");
    }
}
