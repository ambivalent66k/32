package org.example.storagesystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.storagesystem.enums.Status;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storage_objects")
@EntityListeners(AuditingEntityListener.class)
public class StorageObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_location")
    private String purchaseLocation;

    @Column(columnDefinition = "integer default 1")
    private int quantity;

    @Column(name = "volume_per_unit", columnDefinition = "integer default 1")
    private int volumePerUnit;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private String notes;

    @JoinColumn(name = "storage_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Storage storage;

    @JoinColumn(name = "cell_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cell cell;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "custom_attributes", columnDefinition = "jsonb")
    private Map<String, Object> customAttributes;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdAt;

    @LastModifiedDate()
    @Column(name = "updated_at", insertable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime updatedAt;
}
