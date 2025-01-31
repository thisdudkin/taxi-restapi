package by.dudkin.driver.domain;

import by.dudkin.common.entity.BaseEntity;
import by.dudkin.common.entity.PersonalInfo;
import by.dudkin.common.enums.DriverStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
@NamedEntityGraph(name = "driver-assignments-cars", attributeNodes = {
    @NamedAttributeNode(value = "assignments", subgraph = "assignments-subgraph"),
    @NamedAttributeNode("ratings")
},
    subgraphs = @NamedSubgraph(name = "assignments-subgraph", attributeNodes = @NamedAttributeNode("car"))
)
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drivers")
@ToString(exclude = {"assignments", "ratings"})
@EntityListeners(AuditingEntityListener.class)
public class Driver implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "username", nullable = false)
    private String username;

    @Embedded
    private PersonalInfo info;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DriverStatus status = DriverStatus.READY;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Builder.Default
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Assignment> assignments = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "driver_ratings", joinColumns = @JoinColumn(name = "driver_id"))
    @Column(name = "rating")
    private List<Integer> ratings = new ArrayList<>();

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
        Driver driver = (Driver) o;
        return getId() != null && Objects.equals(getId(), driver.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public double getAverageRating() {
        return ratings.isEmpty() ? 0.0 : ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

}
