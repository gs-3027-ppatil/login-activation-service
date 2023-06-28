package com.oneabc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_OccupationType")
public class OccupationType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OccupationType_seq_gen")
	@SequenceGenerator(name = "OccupationType_seq_gen", sequenceName = "OccupationType_seq")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@JsonBackReference
    private Customer customer;
	
	
	@Column(name = "OccupationType")
	private String occupationType;

	@Column(name = "State")
	private String state;

	@Column(name = "CreatedBy")
	private String createdby;

	@Column(name = "CreatedDate")
	private String createddate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModififedDate")
	private String modififedDate;

	@Column(name = "Active")
	private String active;
	
	

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getOccupationType() {
		return occupationType;
	}

	public void setOccupationType(String occupationType) {
		this.occupationType = occupationType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModififedDate() {
		return modififedDate;
	}

	public void setModififedDate(String modififedDate) {
		this.modififedDate = modififedDate;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public OccupationType() {
		super();
	}

	public OccupationType(int id, Customer customer, String occupationType, String state, String createdby,
			String createddate, String modifiedBy, String modififedDate, String active) {
		super();
		this.id = id;
		this.customer = customer;
		this.occupationType = occupationType;
		this.state = state;
		this.createdby = createdby;
		this.createddate = createddate;
		this.modifiedBy = modifiedBy;
		this.modififedDate = modififedDate;
		this.active = active;
	}
	
	
	
	
	

}
