package com.oneabc.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "CURRENTMPIN")
	private String currentMpin;

	@Column(name = "MPIN_EXP")
	private Date mpinExpiry;

	@Column(name = "ACTIVE")
	private boolean active;

	@OneToOne(mappedBy = "mPin", cascade = CascadeType.ALL)
	private Customer customer;
}
