package com.oneabc.entity;

 

import java.util.Date;

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
@Table(name = "T_Kyc") 
public class Kyc {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kyc_seq_gen")
    @SequenceGenerator(name = "kyc_seq_gen", sequenceName = "kyc_seq")
	private long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	@JsonBackReference
    private Customer customer;
	
	 
	
	@Column(name = "permanentaddress")
	private String permanentAddress;
	
	@Column(name = "digilockerstatus")
	private String digilockerStatus;
	
	@Column(name = "digilockercreateddate")
	private String digilockerCreatedDate;
	
	@Column(name = "videokyc")
	private String videoKYC;
	
	@Column(name = "lastfetcheddt")
	private Date lastFetchedDT;

	
	
	
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

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getDigilockerStatus() {
		return digilockerStatus;
	}

	public void setDigilockerStatus(String digilockerStatus) {
		this.digilockerStatus = digilockerStatus;
	}

	public String getDigilockerCreatedDate() {
		return digilockerCreatedDate;
	}

	public void setDigilockerCreatedDate(String digilockerCreatedDate) {
		this.digilockerCreatedDate = digilockerCreatedDate;
	}

	public String getVideoKYC() {
		return videoKYC;
	}

	public void setVideoKYC(String videoKYC) {
		this.videoKYC = videoKYC;
	}

	 

	public Kyc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Kyc(long id, Customer customer, String permanentAddress, String digilockerStatus,
			String digilockerCreatedDate, String videoKYC, Date lastFetchedDT) {
		super();
		this.id = id;
		this.customer = customer;
		this.permanentAddress = permanentAddress;
		this.digilockerStatus = digilockerStatus;
		this.digilockerCreatedDate = digilockerCreatedDate;
		this.videoKYC = videoKYC;
		this.lastFetchedDT = lastFetchedDT;
	}

	 
	
}
	
