package com.vimal.app.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vimal.app.enums.SettlementStatus;
import com.vimal.app.enums.converter.SettlementStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "overtime_entries",
        indexes = {
                @Index(name = "idx_worker_overtime_date", columnList = "worker_id, date"),
                @Index(name = "idx_ot_status", columnList = "settlement_status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OvertimeEntry extends BaseEntity {

	private static final long serialVersionUID = -2168655914157876627L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "worker_id", referencedColumnName = "id", nullable = false)
    private Worker worker;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attendance_id", referencedColumnName = "id", nullable = false, unique = true)
    private AttendanceLog attendance;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal overtimeHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal overtimeRateApplied;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Convert(converter = SettlementStatusConverter.class)
    private SettlementStatus settlementStatus;
}
