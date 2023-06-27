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
	
	 
	
	@Column(name = "PermanentAddress")
	private String permanentAddress;
	
	@Column(name = "DigilockerStatus")
	private String digilockerStatus;
	
	@Column(name = "DigilockerCreatedDate")
	private String digilockerCreatedDate;
	
	@Column(name = "VideoKYC")
	private String videoKYC;
	
	@Column(name = "LastFetchedDT")
	private String lastFetchedDT;

	
	
	
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

	public String getLastFetchedDT() {
		return lastFetchedDT;
	}

	public void setLastFetchedDT(String lastFetchedDT) {
		this.lastFetchedDT = lastFetchedDT;
	}

	public Kyc() {
		super();
	}

	public Kyc(long id, Customer customer, String permanentAddress, String digilockerStatus,
			String digilockerCreatedDate, String videoKYC, String lastFetchedDT) {
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
	
