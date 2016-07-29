package com.ractoc.rsdl.generator.definition;

public class Restriction {

	private String restrictionType;
	private String restrictionValue;

    public Restriction() {
		super();
	}

	public Restriction(String restrictionType, String restrictionValue) {
		this.restrictionType = restrictionType;
		this.restrictionValue = restrictionValue;
	}

	public String getRestrictionType() {
		return restrictionType;
	}

	public String getRestrictionValue() {
		return restrictionValue;
	}

    @Override
    public String toString() {
        return "Result{" +
                "type='" + restrictionType + '\'' +
                ", value='" + restrictionValue + '\'' +
                '}';
    }
	
}
