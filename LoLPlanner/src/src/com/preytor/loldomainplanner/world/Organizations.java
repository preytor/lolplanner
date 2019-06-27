package com.preytor.loldomainplanner.world;

public class Organizations {
	private int id;
	private String name;
	private int maxPopulation;
	private int maxStockpile;
	
	public Organizations(){}
	
	public Organizations(final int id, final String name, final int maxPopulation, final int maxStockpile){
		setId(id);
		setName(name);
		setMaxPopulation(maxPopulation);
		setMaxStockpile(maxStockpile);
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final int getMaxPopulation() {
		return maxPopulation;
	}

	public final void setMaxPopulation(int maxPopulation) {
		this.maxPopulation = maxPopulation;
	}

	public final int getMaxStockpile() {
		return maxStockpile;
	}

	public final void setMaxStockpile(int maxStockpile) {
		this.maxStockpile = maxStockpile;
	}

}
