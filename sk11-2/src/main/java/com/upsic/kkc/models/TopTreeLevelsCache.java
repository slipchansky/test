package com.upsic.kkc.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TopTreeLevelsCache {

    /**
     * Ид не автогенерируемый так как в таблице должна быть, вообще, всего одна запись
     */
    @Column(nullable = false)
    @Id
    private Long id;

    /**
     * Флаг инициализации кеша
     */
    @Column(nullable = false)
    private Boolean initializing;

    /**
     * Флаг обновления кеша
     */
    @Column(nullable = false)
    private Boolean updating;

    /**
     * Время начала последнего обновления кеша
     */
    @Column(nullable = false)
    private Instant lastUpdateStartedTime;

    /**
     * Время последнего обновления кеша
     */
    @Column(nullable = false)
    private Instant lastUpdateTime;

}
