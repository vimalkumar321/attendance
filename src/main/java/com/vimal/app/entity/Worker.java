package com.vimal.app.entity;

import java.math.BigDecimal;

import com.vimal.app.enums.Designation;
import com.vimal.app.enums.converter.DesignationConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "workers",
        indexes = {
                @Index(name = "idx_worker_phone", columnList = "phone", unique = true),
                @Index(name = "idx_worker_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Worker extends BaseEntity {

	private static final long serialVersionUID = -5106031177842978394L;
	@Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;
    
    @Column(nullable = false)
    @Convert(converter = DesignationConverter.class)
    private Designation designation;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMax("0.0")
    private BigDecimal dailyWageRate;
    
    @Column(nullable = false)
    private boolean active = true;
}
