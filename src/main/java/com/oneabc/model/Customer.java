package com.oneabc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oneabc.model.Kyc;
import com.oneabc.model.LoanType;
import com.oneabc.model.OccupationType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
	@SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_seq")
	private long id;

	@Column(name = "MobileNumber")
	private String mobileNumber;

	@Column(name = "FirstName")
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "DOB")
	private String dateOfBirth;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Kyc> kycCoustomer;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<LoanType> loanType;

	@Column(name = "PanNumber")
	private String panNumber;

	@Column(name = "AdharNumber")
	private String adharNumber;

	@Column(name = "Email")
	private String email;

	@Column(name = "Gender")
	private String gender;

	@Column(name = "Landmark")
	private String landmark;

	@Column(name = "Pincode")
	private String pincode;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<OccupationType> occupationType;

	@Column(name = "CompanyName")
	private String companyName;

	@Column(name = "NriCustomer")
	private boolean nriCustomer;

	@Column(name = "MonthlyIncome")
	private String monthlyIncome;

	@Column(name = "LoanAmount")
	private String loanAmount;

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

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private PinMgt mpin;
}
