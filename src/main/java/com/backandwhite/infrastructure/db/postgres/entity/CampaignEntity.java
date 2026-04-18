package com.backandwhite.infrastructure.db.postgres.entity;

import com.backandwhite.common.domain.valueobject.Money;
import com.backandwhite.common.domain.valueobject.MoneyConverter;
import com.backandwhite.common.infrastructure.entity.AuditableEntity;
import com.backandwhite.domain.valueobject.CampaignType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@With
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaigns")
public class CampaignEntity extends AuditableEntity {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CampaignType type;

    @Convert(converter = MoneyConverter.class)
    @Column(precision = 12, scale = 2)
    private Money value;

    @Column(length = 50)
    private String badge;

    @Column(name = "badge_color", length = 20)
    private String badgeColor;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applies_to_categories", columnDefinition = "jsonb")
    private List<String> appliesToCategories;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applies_to_products", columnDefinition = "jsonb")
    private List<String> appliesToProducts;

    @Column
    private Boolean active;

    @Column(length = 500)
    private String description;

    @Column(name = "min_order", precision = 12, scale = 2)
    private BigDecimal minOrder;

    @Column(name = "max_discount", precision = 12, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "buy_qty")
    private Integer buyQty;

    @Column(name = "get_qty")
    private Integer getQty;

    @Column(name = "is_flash")
    private Boolean isFlash;

    @Column(name = "show_on_home")
    private Boolean showOnHome;

    @Column
    private Integer priority;
}
