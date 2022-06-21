package com.upsic.kkc.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity<T extends Serializable>{
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        @SuppressWarnings("unchecked") BaseEntity<T> that = (BaseEntity<T>) o;
        return id != null && id.equals(that.id);
    }

    public int hashCode() {
        int hashCode = 17;
        hashCode += null == getId() ? super.hashCode() : getId().hashCode() * 31;
        return hashCode;
    }
}
