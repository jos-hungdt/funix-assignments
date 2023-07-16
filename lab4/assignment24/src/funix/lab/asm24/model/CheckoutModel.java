package funix.lab.asm24.model;

public class CheckoutModel {
	private String name;
	private String company;
	private String addressLine1;
	private String addressLine2;
	private String zip;
	private String city; 
	private String state; 
	private String country; 
	private String phone;
	private String email;
	private String comment;
	

	
	public CheckoutModel() {
		name = "";
		company = "";
		addressLine1 = "";
		addressLine2 = "";
		zip = "";
		city = "";
		state = "";
		country = "";
		phone = "";
		email ="";
		comment = "";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return this.name + this.company + this.addressLine1 + this.addressLine2 + this.zip + this.city + this.state + this.country + this.phone + this.email + this.comment;
		
	}
}
