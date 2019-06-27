package com.preytor.loldomainplanner.world;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

public class WorldSaver {

	private static int width;
	private static int height;
	private static int organizationType;
	
	private static int currentPopulation;
	private static int maxPopulation;
	private static int currentStorage;
	private static int maxStorage;
	
	private Chunk[][] chunksMap;
	private static Tile[][] tilesMap;
	
	public WorldSaver(){}
	
	public WorldSaver(final int width, final int height, final int organizationType, final int currentPopulation, final int maxPopulation,
			final int currentStorage, final int maxStorage, final Chunk[][] chunksMap, final Tile[][] tilesMap){
		setWidth(width);
		setHeight(height);
		setOrganizationType(organizationType);
		setCurrentPopulation(currentPopulation);
		setMaxPopulation(maxPopulation);
		setCurrentStorage(currentStorage);
		setMaxStorage(maxStorage);
		setChunksMap(chunksMap);
		setTilesMap(tilesMap);
	}
	
	@SuppressWarnings("static-access")
	public WorldSaver(final World world){
		this.width = world.getWIDTH();
		this.height = world.getHEIGHT();
		this.organizationType = world.getOrganizationType();
		this.currentPopulation = world.getCurrentPopulation();
		this.maxPopulation = world.getMaxPopulation();
		this.currentStorage = world.getCurrentStorage();
		this.maxStorage = world.getMaxStorage();
		this.chunksMap = world.getChunksMap();
		this.tilesMap = world.getTilesMap();
	}
	
/**	public void generateSave(final File filePath, final File fileName){	//TODO OLD FILE
		//find a way to save it as a zip with a txt inside
		//find a way to save chunk images inside it

		File file = new File(filePath, fileName.getName());
		
		try {
			FileWriter writer = new FileWriter(file);

			//we write the width
			writer.write(""+width);
			writer.write(";");
			//we write the height
			writer.write(""+height);
			writer.write(";");
			//we write the organizationType
			writer.write(""+organizationType);
			writer.write(";");
			//we write the currentPopulation;
			writer.write(""+currentPopulation);
			writer.write(";");
			//we write the maxPopulation;
			writer.write(""+maxPopulation);
			writer.write(";");
			//we write the currentStorage;
			writer.write(""+currentStorage);
			writer.write(";");
			//we write the maxStorage;
			writer.write(""+maxStorage);
			writer.write(";");
			
			//we write the chunksMap;
			writer.write("chunk_maps");
			writer.write(";");
			//we write the tilesMap;
			
			for(int x = 0; x < World.WIDTH_IN_TILES; x++){
				for(int y = 0; y < World.HEIGHT_IN_TILES; y++){
					if(x==World.WIDTH_IN_TILES-1 && y==World.HEIGHT_IN_TILES-1){
						writer.write(tilesMap[x][y].getId()+"");
					}else{
						writer.write(tilesMap[x][y].getId()+",");
					}
					tilesMap[x][y] = null;
				}
			}
//			writer.write("|");	//ONLY IF WE ADD MORE STUFF
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public File createSaveData(final File filePath, final File fileName){
		File file = new File(filePath, fileName.getName());
		
		try {
			FileWriter writer = new FileWriter(file);

			//we write the width
			writer.write(""+width);
			writer.write(";");
			//we write the height
			writer.write(""+height);
			writer.write(";");
			//we write the organizationType
			writer.write(""+organizationType);
			writer.write(";");
			//we write the currentPopulation;
			writer.write(""+currentPopulation);
			writer.write(";");
			//we write the maxPopulation;
			writer.write(""+maxPopulation);
			writer.write(";");
			//we write the currentStorage;
			writer.write(""+currentStorage);
			writer.write(";");
			//we write the maxStorage;
			writer.write(""+maxStorage);
			writer.write(";");
			
			//we write the chunksMap;
			writer.write("chunk_maps");
			writer.write(";");
			//we write the tilesMap;
			
			for(int x = 0; x < World.WIDTH_IN_TILES; x++){
				for(int y = 0; y < World.HEIGHT_IN_TILES; y++){
					if(x==World.WIDTH_IN_TILES-1 && y==World.HEIGHT_IN_TILES-1){
						writer.write(tilesMap[x][y].getId()+"");
					}else{
						writer.write(tilesMap[x][y].getId()+",");
					}
					tilesMap[x][y] = null;
				}
			}
//			writer.write("|");	//ONLY IF WE ADD MORE STUFF
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	public void generateSave(final File filePath, final File fileName){
		//find a way to save it as a zip with a txt inside
		//find a way to save chunk images inside it

        try {
            // let's create a ZIP file to write data
            FileInputStream fis = new FileInputStream(createSaveData(filePath, fileName));
            ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(fis.toString()));

            zipOS.putNextEntry(new ZipEntry(fileName.getName()));
            byte[] buffer = new byte[1024];
            int byteRead;
            
            while((byteRead=fis.read(buffer))>0){
            	zipOS.write(buffer, 0, byteRead);
            }
            
            zipOS.closeEntry();
            zipOS.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@SuppressWarnings("static-access")
	public void generateSaveImage(World world, final File filePath, final File fileName){		
		Pixmap finalmapPixmap = new Pixmap(world.HEIGHT_IN_TILES*world.TILE_SIZE, world.WIDTH_IN_TILES*world.TILE_SIZE, Format.RGBA8888);
		Texture plainsTexture = new Texture(Gdx.files.internal("textures/buildings/plains.png"));
		Texture tempTexture;
		Tile[][] tilesMap = rotateMatrixBy90DegreeClockwise(world.getTilesMap());
		for(int x = 0; x < world.WIDTH_IN_TILES; x++){
			for(int y = 0; y < world.HEIGHT_IN_TILES; y++){
				//if(!world.containsChunks()){
					plainsTexture.getTextureData().prepare();
					finalmapPixmap.drawPixmap(plainsTexture.getTextureData().consumePixmap(), y*world.TILE_SIZE, x*world.TILE_SIZE);
					if(tilesMap[y][x].getId()>0){
						tempTexture = new Texture(Gdx.files.internal("textures/buildings/"+tilesMap[y][x].getTextureName()+".png"));
						tempTexture.getTextureData().prepare();
						finalmapPixmap.drawPixmap(tempTexture.getTextureData().consumePixmap(), y*world.TILE_SIZE, x*world.TILE_SIZE);
					}
//				}else{
//					//draw the chunks
//				}
			}
		}
		
        FileHandle outputfile = new FileHandle(fileName);
        
		PixmapIO.writePNG(outputfile, finalmapPixmap);
		
		plainsTexture.dispose();
		finalmapPixmap.dispose();
	}
	
    public static void writeToZipFile(String path, ZipOutputStream zipStream) throws FileNotFoundException, IOException {

        System.out.println("Writing file : '" + path + "' to zip file");

        File aFile = new File(path);
        FileInputStream fis = new FileInputStream(aFile);
		
		
        fis.close();
    }
    
    private Tile[][] rotateMatrixBy90DegreeClockwise(Tile[][] matrix) {
    	int totalRowsOfRotatedMatrix = matrix[0].length; //Total columns of Original Matrix
    	int totalColsOfRotatedMatrix = matrix.length; //Total rows of Original Matrix
    	 
    	Tile[][] rotatedMatrix = new Tile[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];
    	 
    	for (int i = 0; i < matrix.length; i++) {
    		for (int j = 0; j < matrix[0].length; j++) {
    			rotatedMatrix[j][ (totalColsOfRotatedMatrix-1)- i] = matrix[i][j]; 
    		}
    	}
    	return rotatedMatrix;
    }
    
	public final static int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		setWidth(width);
	}

	public final static int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		setHeight(height);
	}

	public final static int getOrganizationType() {
		return organizationType;
	}

	public final void setOrganizationType(int organizationType) {
		setOrganizationType(organizationType);
	}

	public final static int getCurrentPopulation() {
		return currentPopulation;
	}

	public final void setCurrentPopulation(int currentPopulation) {
		setCurrentPopulation(currentPopulation);
	}

	public final static int getMaxPopulation() {
		return maxPopulation;
	}

	public final void setMaxPopulation(int maxPopulation) {
		setMaxPopulation(maxPopulation);
	}

	public final static int getCurrentStorage() {
		return currentStorage;
	}

	public final void setCurrentStorage(int currentStorage) {
		setCurrentStorage(currentStorage);
	}

	public final static int getMaxStorage() {
		return maxStorage;
	}

	public final void setMaxStorage(int maxStorage) {
		setMaxStorage(maxStorage);
	}

	public final Chunk[][] getChunksMap() {
		return chunksMap;
	}

	public final void setChunksMap(Chunk[][] chunksMap) {
		this.chunksMap = chunksMap;
	}

	public final Tile[][] getTilesMap() {
		return tilesMap;
	}
	
	public final void setTilesMap(Tile[][] tilesMap) {
		setTilesMap(tilesMap);
	}
}
