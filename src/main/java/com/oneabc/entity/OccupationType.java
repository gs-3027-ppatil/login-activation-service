package com.oneabc.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_OccupationType")
public class OccupationType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OccupationType_seq_gen")
	@SequenceGenerator(name = "OccupationType_seq_gen", sequenceName = "OccupationType_seq")
	private int id;	
	
	@Column(name = "occupationtype")
	private String occupationType;

	@Column(name = "state")
	private String state;

	@Column(name = "createdby")
	private String createdBy;

	@Column(name = "createddate")
	private Date createdDate;

	@Column(name = "modifiedby")
	private String modifiedBy;

	@Column(name = "modifieddate")
	private Date modifiedDate;

	@Column(name = "active")
	private String active;
	
	

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedby(String createdBy) {
		this.createdBy = createdBy;
	}

	 

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	 

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public OccupationType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getCreateddate() {
		return createdDate;
	}

	public void setCreateddate(Date createddate) {
		this.createdDate = createddate;
	}

	public Date modifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public OccupationType(int id, String occupationType, String state, String createdBy, Date createdDate,
			String modifiedBy, Date modifiedDate, String active) {
		super();
		this.id = id;
		this.occupationType = occupationType;
		this.state = state;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.active = active;
	}

	 

	 
	 

	 
	
	
	
	
	

}
