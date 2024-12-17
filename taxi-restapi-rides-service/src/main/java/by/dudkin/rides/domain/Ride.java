package by.dudkin.rides.domain;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.common.enums.PaymentMethod;
import by.dudkin.common.enums.RideStatus;
import by.dudkin.common.util.Location;
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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
public class Ride implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "passenger", nullable = false, updatable = false)
    private UUID passengerId;

    @Column(name = "driver")
    private UUID driverId;

    @Column(name = "car")
    private UUID carId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status = RideStatus.PENDING;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "country", column = @Column(name = "from_country")),
        @AttributeOverride(name = "city", column = @Column(name = "from_city")),
        @AttributeOverride(name = "street", column = @Column(name = "from_street")),
        @AttributeOverride(name = "lat", column = @Column(name = "from_lat")),
        @AttributeOverride(name = "lng", column = @Column(name = "from_lng"))
    })
    private Location from;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "country", column = @Column(name = "to_country")),
        @AttributeOverride(name = "city", column = @Column(name = "to_city")),
        @AttributeOverride(name = "street", column = @Column(name = "to_street")),
        @AttributeOverride(name = "lat", column = @Column(name = "to_lat")),
        @AttributeOverride(name = "lng", column = @Column(name = "to_lng"))
    })
    private Location to;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "rating")
    private Integer rating;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
