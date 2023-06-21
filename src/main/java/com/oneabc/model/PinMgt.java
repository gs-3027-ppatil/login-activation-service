package com.oneabc.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PINMGT")
public class PinMgt {
	@Id
	@Column(name = "pinmgt_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "current_mpin")
	private String currentMpin;

	@Column(name = "mpin_exp")
	private Date mpinExpiry;

	@Column(name = "active")
	private boolean active;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_cust_id")
	private Customer customer;
}
