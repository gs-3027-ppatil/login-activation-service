package com.oneabc.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_customer") 
public class Customer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_seq")
	private long id; 
 
	
	@Column(name = "mobilenumber", unique = true)
	private String mobileNumber;
	
	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;
	
	@Column(name = "dob")
	private Date dateOfBirth;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Kyc> kycCoustomer;
	
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
      private List<LoanType> loanType;
	
	@Column(name = "pannumber")
	private String panNumber;
	
	@Column(name = "aadhaarnumber")
	private String aadhaarNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "gender")
	private String gender;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference 
	private List<Address> address;
	
	@Column(name = "landmark")
	private String landmark;
	
	@Column(name = "pincode")
	private String pincode;
	 
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="occupationId_id")
    private  OccupationType occupationType;
	
	@Column(name = "companyname")
	private String companyName;
	
	@Column(name = "nricustomer")
	private boolean nriCustomer;
	
	@Column(name = "monthlyincome")
	private String monthlyIncome;
	
	@Column(name = "loanamount")
	private String loanAmount;
	
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
	
	@Column(name = "enabledha")
	private String enableDHA;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private PinMgt mpin;
	
}
	
	