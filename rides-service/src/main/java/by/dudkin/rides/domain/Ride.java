package by.dudkin.rides.domain;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.enums.RideStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * @author Alexander Dudkin
 */
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rides")
@EntityListeners(AuditingEntityListener.class)
public class Ride implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "passenger", nullable = false, updatable = false)
    private Long passengerId;

    @NotNull
    @Column(name = "driver", nullable = false, updatable = false)
    private Long driverId;

    @NotNull
    @Column(name = "car", nullable = false, updatable = false)
    private Long carId;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status = RideStatus.ACTIVE;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "county", column = @Column(name = "from_county")),
            @AttributeOverride(name = "city", column = @Column(name = "from_city")),
            @AttributeOverride(name = "street", column = @Column(name = "from_street"))
    })
    private Location from;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "county", column = @Column(name = "to_county")),
            @AttributeOverride(name = "city", column = @Column(name = "to_city")),
            @AttributeOverride(name = "street", column = @Column(name = "to_street"))
    })
    private Location to;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @CreatedDate
    @Column(name = "created_utc", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_utc")
    private Instant updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Ride ride = (Ride) o;
        return getId() != null && Objects.equals(getId(), ride.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
