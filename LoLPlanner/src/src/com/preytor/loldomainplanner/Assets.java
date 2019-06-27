package com.preytor.loldomainplanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.preytor.loldomainplanner.world.BuildingCategory;
import com.preytor.loldomainplanner.world.OrganizationCategories;
import com.preytor.loldomainplanner.world.Organizations;
import com.preytor.loldomainplanner.world.Tile;

public class Assets {

	public static HashMap<Integer, Tile> buildings_map = new HashMap<Integer, Tile>();
//	public static HashMap<Integer, Sprite> sprites_map = new HashMap<Integer, Sprite>();
	public static List<BuildingCategory> categories_map = new ArrayList<BuildingCategory>();
	public static List<Organizations> organizations_map = new ArrayList<Organizations>();
	public static List<OrganizationCategories> organizationCategories_map = new ArrayList<OrganizationCategories>();
	
	public static void loadBuildings() {
		Json json = new Json();
		Tile buildingLoader;
		
		if(Main.Debug){
			System.out.println("Loading buildings");
		}
		FileHandle file = Gdx.files.internal(Defines.DEFINES_PATH+Defines.BUILDINGS_FILE);
		if(Main.Debug){
			System.out.println("buildings exists: "+Gdx.files.internal(Defines.DEFINES_PATH+Defines.BUILDINGS_FILE).exists());
		}
		
		for(String tile : file.readString().split(";")){
			buildingLoader = json.fromJson(Tile.class, tile);
			
			buildings_map.put(buildingLoader.getId(), new Tile(buildingLoader.getId(), buildingLoader.getName(), buildingLoader.getTextureName(), buildingLoader.getDistanceBetweenSame(), buildingLoader.getNeedsAdjacent(), buildingLoader.getCanBeBuiltIn(), buildingLoader.getAddsPopulation(), buildingLoader.getRemovesPopulation(), buildingLoader.getAddsStorage(), buildingLoader.getRemovesStorage()));
			if(Main.Debug){
				System.out.println("building "+buildingLoader.getName()+" (ID: "+buildingLoader.getId()+") loaded");
			}
		}
	}	
	
//	public static void loadSprites() {
//		Json json = new Json();
//		Tile buildingLoader;
//		
//		if(Main.Debug){
//			System.out.println("Loading buildings sprites");
//		}
//		FileHandle file = Gdx.files.internal(Defines.DEFINES_PATH+Defines.BUILDINGS_FILE);
//		if(Main.Debug){
//			System.out.println("buildings exists: "+Gdx.files.internal(Defines.DEFINES_PATH+Defines.BUILDINGS_FILE).exists());
//		}	
//
//		TextureAtlas atlas = new TextureAtlas(Defines.TEXTURES_PATH+Defines.BUILDINGS_TEXTURES_PATH+Defines.BUILDINGS_TEXTURES_FILE);
//		for(String tile : file.readString().split(";")){
//			buildingLoader = json.fromJson(Tile.class, tile);
//			
//			sprites_map.put(buildingLoader.getId(), new Sprite(atlas.findRegion(buildingLoader.getTextureName())));
//			if(Main.Debug){
//				System.out.println("building sprite "+buildingLoader.getName()+" (ID: "+buildingLoader.getId()+") loaded");
//			}
//		}
//	}
	
	public static void loadCategories() {
		Json json = new Json();
		BuildingCategory categoryLoader;
		
		if(Main.Debug){
			System.out.println("Loading categories");
		}
		
		FileHandle file = Gdx.files.internal(Defines.DEFINES_PATH+Defines.CATEGORIES_FILE);
		if(Main.Debug){
			System.out.println("category exists: "+Gdx.files.internal(Defines.DEFINES_PATH+Defines.CATEGORIES_FILE).exists());
		}
		
		for(String tile : file.readString().split(";")){
			categoryLoader = json.fromJson(BuildingCategory.class, tile);
			if(Main.Debug){
				System.out.println("builds id: "+categoryLoader.getBuildingsID().length);
			}
			categories_map.add(new BuildingCategory(categoryLoader.getName(), categoryLoader.getBuildingsID()));
			if(Main.Debug){
				System.out.println("category "+categoryLoader.getName()+" loaded");
			}
		}
	}
	
	public static void loadOrganizations() {
		Json json = new Json();
		Organizations organizationsLoader;
		
		if(Main.Debug){
			System.out.println("Loading organizations");
		}
		
		FileHandle file = Gdx.files.internal(Defines.DEFINES_PATH+Defines.ORGANIZATIONS_FILE);
		if(Main.Debug){
			System.out.println("organizations exists: "+Gdx.files.internal(Defines.DEFINES_PATH+Defines.ORGANIZATIONS_FILE).exists());
		}
		
		for(String org : file.readString().split(";")){
			organizationsLoader = json.fromJson(Organizations.class, org);
			organizations_map.add(new Organizations(organizationsLoader.getId(), organizationsLoader.getName(), organizationsLoader.getMaxPopulation(), organizationsLoader.getMaxStockpile()));			
			if(Main.Debug){
				System.out.println("organization "+organizationsLoader.getName()+" loaded");
			}
		}
	}
	
	public static void loadOrganizationCategories() {
		Json json = new Json();
		OrganizationCategories organizationCategoriesLoader;
		
		if(Main.Debug){
			System.out.println("Loading organization categories");
		}
		
		FileHandle file = Gdx.files.internal(Defines.DEFINES_PATH+Defines.ORGANIZATION_CATEGORIES_FILE);
		if(Main.Debug){
			System.out.println("organization categories exists: "+Gdx.files.internal(Defines.DEFINES_PATH+Defines.ORGANIZATION_CATEGORIES_FILE).exists());
		}
		
		for(String tile : file.readString().split(";")){
			organizationCategoriesLoader = json.fromJson(OrganizationCategories.class, tile);

			organizationCategories_map.add(new OrganizationCategories(organizationCategoriesLoader.getName(), organizationCategoriesLoader.getOrganizations()));
			if(Main.Debug){
				System.out.println("organization category "+organizationCategoriesLoader.getName()+" loaded");
			}
		}
	}
}
