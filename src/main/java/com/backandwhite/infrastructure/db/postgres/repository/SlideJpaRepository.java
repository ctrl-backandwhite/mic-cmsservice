package com.backandwhite.infrastructure.db.postgres.repository;

import com.backandwhite.infrastructure.db.postgres.entity.SlideEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SlideJpaRepository extends JpaRepository<SlideEntity, String> {

    List<SlideEntity> findByActiveTrueOrderByPositionAsc();

    List<SlideEntity> findAllByOrderByPositionAsc();

    /**
     * Returns active slides with translated title / subtitle / button_text for the
     * given locale. If a translation row does not exist the raw columns on `slides`
     * are used as fallback, so the shape stays the same regardless of locale
     * support.
     */
    @Query(value = """
            SELECT
                s.id                                         AS id,
                COALESCE(t.title,       s.title)             AS title,
                COALESCE(t.subtitle,    s.subtitle)          AS subtitle,
                s.image_url                                  AS image_url,
                s.link                                       AS link,
                COALESCE(t.button_text, s.button_text)       AS button_text,
                s.position                                   AS position,
                s.active                                     AS active,
                s.created_at                                 AS created_at,
                s.updated_at                                 AS updated_at
            FROM slides s
            LEFT JOIN slide_translations t
                   ON t.slide_id = s.id
                  AND t.locale   = :locale
            WHERE s.active = TRUE
            ORDER BY s.position ASC
            """, nativeQuery = true)
    List<Object[]> findActiveTranslated(@Param("locale") String locale);
}
