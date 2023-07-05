package com.oneabc.entity;

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
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "currentmpin")
	private String currentMpin;

	@Column(name = "mpinexpiry")
	private Date mpinExpiry;

	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "createddate")
	private Date createdDate;
	
	@Column(name = "modifieddate")
	private Date modifiedDate;

	@Column(name = "modifiedby")
	private String modifiedBy;
	
	@Column(name = "active")
	private String active;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_cust_id")
	private Customer customer;
}
