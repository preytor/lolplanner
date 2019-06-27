package com.preytor.loldomainplanner.utils;

import java.io.File;
import java.util.Map;

import com.preytor.loldomainplanner.Assets;
import com.preytor.loldomainplanner.world.Organizations;
import com.preytor.loldomainplanner.world.Tile;

public class Utils {
	
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static String getBuildingNameFromId(final int id){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==id){
    			return buildingsSet.getValue().getName();
    		}
    	}
    	return null;
    }
    
    public static String getBuildingTextureNameFromId(final int id){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==id){
    			return buildingsSet.getValue().getTextureName();
    		}
    	}
    	return null;
    }

    public static int getBuildingDistanceBetweenSameFromId(final int id){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==id){
    			return buildingsSet.getValue().getDistanceBetweenSame();
    		}
    	}
    	return 0;
    }
    
    public static int[] getBuildingNeedsAdjacentFromId(final int id){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==id){
    			return buildingsSet.getValue().getNeedsAdjacent();
    		}
    	}
    	return null;
    }
    
    public static int[] getBuildingCanBeBuiltIntFromId(final int id){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==id){
    			return buildingsSet.getValue().getCanBeBuiltIn();
    		}
    	}
    	return null;
    }
    
    public static String getOrganizationNameFromId(final int id){
    	for(Organizations org : Assets.organizations_map){
    		if(org.getId()==id){
    			return org.getName();
    		}
    	}
    	return null;
    }
    
    public static int getGivesPopulation(final int inHandId){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			return buildingsSet.getValue().getAddsPopulation();
    		}
    	}
		return 0;
    }
    
    public static int getRemovesMaxPopulation(final int inHandId){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			return buildingsSet.getValue().getRemovesPopulation();
    		}
    	}
		return 0;
    }
    
    public static int getGivesStorage(final int inHandId){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			return buildingsSet.getValue().getAddsStorage();
    		}
    	}
		return 0;
    }
    
    public static int getGivesMaxStorage(final int inHandId){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			return buildingsSet.getValue().getRemovesStorage();
    		}
    	}
		return 0;
    }
    
    public static boolean organizationContainsBuilding(final int organizationType, final int inHandId){
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			for(int i = 0; i<buildingsSet.getValue().getCanBeBuiltIn().length; i++){
    				if(buildingsSet.getValue().getCanBeBuiltIn()[i]==organizationType){
    					return true;
    				}
    			}
    			return false;
    		}
    	}
		return false;
    }

	public static int getRemovesMaxStorage(int inHandId) {
    	for(Map.Entry<Integer, Tile> buildingsSet : Assets.buildings_map.entrySet()){
    		if(buildingsSet.getKey()==inHandId){
    			return buildingsSet.getValue().getRemovesStorage();
    		}
    	}
		return 0;
	}
}
