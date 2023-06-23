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
@Table(name = "T_LoanType")
public class LoanType {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq_gen")
    @SequenceGenerator(name = "loan_seq_gen", sequenceName = "loan_seq")
	private long id;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@JsonBackReference
    private Customer customer;
	
	
	@Column(name = "LoanType")
	private String loanType;
	
	@Column(name = "Createdby")
	private String createdby;
	
	@Column(name = "Createddate")
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

	public void setId(long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
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

	public LoanType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LoanType(long id, Customer customer, String loanType, String createdby, String createddate,
			String modifiedBy, String modififedDate, String active) {
		super();
		this.id = id;
		this.customer = customer;
		this.loanType = loanType;
		this.createdby = createdby;
		this.createddate = createddate;
		this.modifiedBy = modifiedBy;
		this.modififedDate = modififedDate;
		this.active = active;
	}
	
	
	
	
	
}
