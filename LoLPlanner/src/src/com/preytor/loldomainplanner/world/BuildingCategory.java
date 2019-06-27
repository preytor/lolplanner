package com.preytor.loldomainplanner.world;

public class BuildingCategory {
			
	private String name;
	private int[] buildingsID;

	public BuildingCategory(){}
	
	public BuildingCategory(final String name, final int[] buildingsID) {
		this.setName(name);
		this.setBuildingsID(buildingsID);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getBuildingsID() {
		return buildingsID;
	}

	public void setBuildingsID(int[] buildingsID) {
		this.buildingsID = buildingsID;
	}
	
}
