package com.oneabc.model;

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
@Table(name = "T_CUSTOMER")
public class Customer {
	@Id
	@Column(name = "cust_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long custId;

	@Column(name = "name")
	private String name;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_pinmgt_id")
	private PinMgt mPin;
}
