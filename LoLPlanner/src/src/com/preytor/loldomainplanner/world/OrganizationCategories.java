package com.preytor.loldomainplanner.world;

public class OrganizationCategories {
	
	private String name;
	private int[] organizations;
	
	public OrganizationCategories(){}
	
	public OrganizationCategories(final String name, final int[] organizations){
		setName(name);
		setOrganizations(organizations);
	}
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final int[] getOrganizations() {
		return organizations;
	}

	public final void setOrganizations(int[] organizations) {
		this.organizations = organizations;
	}
}
