package service.core;

/**
 * Class to store the quotations returned by the quotation services
 * 
 * @author Rem
 *
 */
public class Quotation {

	private String company;
	private String reference;
	private double price;

	public Quotation(String company, String reference, double price) {
		this.company = company;
		this.reference = reference;
		this.price = price;

	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String toString() {
		String newLine = "----------------------------------------------------";
		return String.format("%sCompany: %s\nReference: %s\nPrice: %.2lf\n%s",
				newLine, company, reference, price, newLine);
	}

}
