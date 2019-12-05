package br.ufes.inf.lied.objects.test;

public class Car {

	private String VIN;
	private String color;
	private Integer miles;

	public Car() {

	}

	public Car( String vIN, String color, Integer miles ) {
		VIN = vIN;
		this.color = color;
		this.miles = miles;
	}

	public String getVIN() {
		return VIN;
	}

	public void setVIN( String vIN ) {
		VIN = vIN;
	}

	public String getColor() {
		return color;
	}

	public void setColor( String color ) {
		this.color = color;
	}

	public Integer getMiles() {
		return miles;
	}

	public void setMiles( Integer miles ) {
		this.miles = miles;
	}

}
