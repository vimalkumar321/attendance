package com.vimal.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sites", indexes = {
        @Index(name = "idx_site_active", columnList = "active")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site extends BaseEntity {
	
	private static final long serialVersionUID = 8693032111207374686L;

	@Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String location;
    
    @Column(nullable = false)
    private boolean active = true;
}
