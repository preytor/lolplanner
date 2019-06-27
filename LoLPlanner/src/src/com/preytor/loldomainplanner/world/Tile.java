package com.preytor.loldomainplanner.world;

import java.util.Map.Entry;

import com.preytor.loldomainplanner.Assets;

public class Tile {

	private int id;
	private String name;
	private String textureName;
	private int distanceBetweenSame;
	private int[] needsAdjacent;
	private int[] canBeBuiltIn;
	private int addsPopulation;
	private int removesPopulation;
	private int addsStorage;
	private int removesStorage;
	
	public Tile(){}
	
	public Tile(final int id, final String name, final String textureName, final int distanceBetweenSame, final int[] needsAdjacent, final int[] canBeBuiltIn, final int addsPopulation, final int removesPopulation, final int addsStorage, final int removesStorage){
		this.setId(id);
		this.setName(name);
		this.setTextureName(textureName);
		this.setDistanceBetweenSame(distanceBetweenSame);
		this.setNeedsAdjacent(needsAdjacent);
		this.setCanBeBuiltIn(canBeBuiltIn);
		this.setAddsPopulation(addsPopulation);
		this.setRemovesPopulation(removesPopulation);
		this.setAddsStorage(addsStorage);
		this.setRemovesStorage(removesStorage);
	}
	
	public Tile(final int id){
		this.setId(id);
		
		for(Entry<Integer, Tile> tile : Assets.buildings_map.entrySet()){
			if(tile.getKey()==id){
				this.setName(tile.getValue().getName());
				this.setTextureName(tile.getValue().getTextureName());
				this.setDistanceBetweenSame(tile.getValue().getDistanceBetweenSame());
				this.setNeedsAdjacent(tile.getValue().getNeedsAdjacent());
				this.setCanBeBuiltIn(tile.getValue().getCanBeBuiltIn());
				this.setAddsPopulation(tile.getValue().getAddsPopulation());
				this.setRemovesPopulation(tile.getValue().getRemovesPopulation());
				this.setAddsStorage(tile.getValue().getAddsStorage());
				this.setRemovesStorage(tile.getValue().getRemovesStorage());
				break;
			}
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTextureName() {
		return textureName;
	}

	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	
	public int getDistanceBetweenSame() {
		return distanceBetweenSame;
	}

	public void setDistanceBetweenSame(int distanceBetweenSame) {
		this.distanceBetweenSame = distanceBetweenSame;
	}

	public int[] getNeedsAdjacent() {
		return needsAdjacent;
	}

	public void setNeedsAdjacent(int[] needsAdjacent) {
		this.needsAdjacent = needsAdjacent;
	}

	public final int[] getCanBeBuiltIn() {
		return canBeBuiltIn;
	}

	public final void setCanBeBuiltIn(int[] canBeBuiltIn) {
		this.canBeBuiltIn = canBeBuiltIn;
	}

	public final int getAddsPopulation() {
		return addsPopulation;
	}

	public final void setAddsPopulation(int addsPopulation) {
		this.addsPopulation = addsPopulation;
	}

	public final int getRemovesPopulation() {
		return removesPopulation;
	}

	public final void setRemovesPopulation(int removesPopulation) {
		this.removesPopulation = removesPopulation;
	}

	public int getAddsStorage() {
		return addsStorage;
	}

	public void setAddsStorage(int addsStorage) {
		this.addsStorage = addsStorage;
	}

	public int getRemovesStorage() {
		return removesStorage;
	}

	public void setRemovesStorage(int removesStorage) {
		this.removesStorage = removesStorage;
	}
	
	
}
