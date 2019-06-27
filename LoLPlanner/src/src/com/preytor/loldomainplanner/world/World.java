package com.preytor.loldomainplanner.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.preytor.loldomainplanner.Assets;
import com.preytor.loldomainplanner.utils.Utils;

public class World {

	private int organizationType;
	
	private int currentPopulation;
	private int maxPopulation;
	private int currentStorage;
	private int maxStorage;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private ShapeRenderer shapeRender;
	
	private Chunk[][] chunksMap;
	private Tile[][] tilesMap;

	private MapLayers layers;

	TiledMapTileLayer chunksLayer;
	TiledMapTileLayer terrainLayer;
	TiledMapTileLayer tilesLayer;
	
	static int WIDTH;
	static int HEIGHT;
	public final static int TILE_SIZE = 80;
	public final static int TILES_INSIDE_CHUNK = 16;
	public final static int CHUNK_SIZE = TILE_SIZE*TILES_INSIDE_CHUNK;
	final static int STEP_SIZE = 0;
	
	static int WIDTH_IN_TILES;
	static int HEIGHT_IN_TILES;
	
	public World(final File saveFile){
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(saveFile);
			int save = 0;
			map = new TiledMap();
			layers = map.getLayers();
			boolean mode = true; //TODO TEMPORALLY LIKE THIS
			while(scanner.hasNext()){
				String next = scanner.next();
				String[] nextContent = next.split(";");
				for(int i=0; i<nextContent.length; i++){
					String tile = nextContent[i];
					
					if(save==0){	//map Width
						setWIDTH(Integer.parseInt(tile));
						WIDTH_IN_TILES = WIDTH * TILES_INSIDE_CHUNK;
						save++;
					}else if(save==1){	//map Height
						setHEIGHT(Integer.parseInt(tile));
						HEIGHT_IN_TILES = HEIGHT * TILES_INSIDE_CHUNK;
						save++;
					}else if(save==2){	//map organizationType
						setOrganizationType(Integer.parseInt(tile));
						save++;
					}else if(save==3){	//map currentPopulation
						setCurrentPopulation(Integer.parseInt(tile));
						save++;
					}else if(save==4){	//map maxPopulation
						setMaxPopulation(Integer.parseInt(tile));
						save++;
					}else if(save==5){	//map currentStorage
						setCurrentStorage(Integer.parseInt(tile));
						save++;
					}else if(save==6){	//map maxStorage
						setMaxStorage(Integer.parseInt(tile));
						shapeRender = new ShapeRenderer();
						shapeRender.setAutoShapeType(true);
						shapeRender.setColor(Color.BLACK);
						save++;
					}else if(save==7){	//chunksMap	TODO: GET THE IMAGES FROM THE .lol ZIP FILE > EVENTUALLY
						chunksMap = new Chunk[WIDTH][HEIGHT];
						chunksLayer = new TiledMapTileLayer(WIDTH, HEIGHT, CHUNK_SIZE, CHUNK_SIZE);
						chunksLayer.setName("chunk");
						layers.add(chunksLayer);
						save++;
					}else if(save==8){	//tilesMap
						String[] tilesInsideMap = tile.split(",");
						
						int t = 0;
						tilesMap = new Tile[WIDTH*16][HEIGHT*16];
						terrainLayer = new TiledMapTileLayer(WIDTH_IN_TILES, HEIGHT_IN_TILES, TILE_SIZE, TILE_SIZE);
						terrainLayer.setName("terrain");
						tilesLayer = new TiledMapTileLayer(WIDTH_IN_TILES, HEIGHT_IN_TILES, TILE_SIZE, TILE_SIZE);
						tilesLayer.setName("building");
						ShapeRenderer shaperenderer = new ShapeRenderer();
							
						shaperenderer.begin(ShapeType.Filled);
						shaperenderer.setColor(Color.RED);
						Cell cellTiles;
						Cell cellBuildings;
						for(int x = 0; x < WIDTH_IN_TILES; x++){
							for(int y = 0; y < HEIGHT_IN_TILES; y++){
								tilesMap[x][y] = new Tile(Integer.parseInt(tilesInsideMap[t]));
								if(mode==true){
									cellTiles = new Cell();
									cellTiles.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/plains.png")))));
									terrainLayer.setCell(y, x, cellTiles);
								}
								
								if(tilesInsideMap[t]!=null && Integer.parseInt(tilesInsideMap[t])>0){
									cellBuildings = new Cell();
									cellBuildings.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/"+Utils.getBuildingTextureNameFromId(Integer.parseInt(tilesInsideMap[t]))+".png")))));
									tilesLayer.setCell(y, x, cellBuildings);
								}
								t++;
								shaperenderer.rect(x, y, WIDTH, HEIGHT);
							}
						}			
						layers.add(terrainLayer);
						layers.add(tilesLayer);
						shaperenderer.end();
						setRenderer(new OrthogonalTiledMapRenderer(map));
						save++;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param mode: 0: creative, 1: import chunk images
	 * @param width
	 * @param height
	 */
	public World(final boolean mode, final int width, final int height, final int organizationType){
		setWIDTH(width);
		setHEIGHT(height);
		
		setOrganizationType(organizationType);
		for(Organizations org : Assets.organizations_map){
			if(org.getId()==organizationType){
				this.setCurrentPopulation(0);
				this.setMaxPopulation(org.getMaxPopulation());
				this.setCurrentStorage(0);
				this.setMaxStorage(org.getMaxStockpile());
				break;
			}
		}
		
		WIDTH_IN_TILES = width * TILES_INSIDE_CHUNK;
		HEIGHT_IN_TILES = height * TILES_INSIDE_CHUNK;
		
		map = new TiledMap();
		layers = map.getLayers();
		chunksMap = new Chunk[width][height];
		tilesMap = new Tile[width*16][height*16];
		
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		shapeRender.setColor(Color.BLACK);
		
		chunksLayer = new TiledMapTileLayer(width, height, CHUNK_SIZE, CHUNK_SIZE);
		chunksLayer.setName("chunk");
		terrainLayer = new TiledMapTileLayer(WIDTH_IN_TILES, HEIGHT_IN_TILES, TILE_SIZE, TILE_SIZE);
		terrainLayer.setName("terrain");
		tilesLayer = new TiledMapTileLayer(WIDTH_IN_TILES, HEIGHT_IN_TILES, TILE_SIZE, TILE_SIZE);
		tilesLayer.setName("building");
				
		ShapeRenderer shaperenderer = new ShapeRenderer();
		
		shaperenderer.begin(ShapeType.Filled);
		shaperenderer.setColor(Color.RED);
		
		Cell cellTiles;
		
		for(int x = 0; x < WIDTH_IN_TILES; x++){
			for(int y = 0; y < HEIGHT_IN_TILES; y++){
				
				tilesMap[x][y] = new Tile(0, "", "", 0, new int[]{0}, new int[]{0}, 0, 0, 0, 0);
				if(mode==true){
					cellTiles = new Cell();
					cellTiles.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/plains.png")))));
					terrainLayer.setCell(y, x, cellTiles);
				}
				shaperenderer.rect(x, y, width, height);
			}
		}
		layers.add(chunksLayer);
		layers.add(terrainLayer);
		layers.add(tilesLayer);

		shaperenderer.end();
		setRenderer(new OrthogonalTiledMapRenderer(map));
	}

	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(OrthogonalTiledMapRenderer renderer) {
		this.renderer = renderer;
	}

	public final ShapeRenderer getShapeRender() {
		return shapeRender;
	}

	public final void setShapeRender(ShapeRenderer shapeRender) {
		this.shapeRender = shapeRender;
	}
	
	public final static int getWIDTH() {
		return WIDTH;
	}

	public final void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}
	
	public final static int getHEIGHT() {
		return HEIGHT;
	}

	public final void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}
	
	public void renderTileGrid(ShapeRenderer shapeRenderer){		
		for(int x = 0; x < WIDTH_IN_TILES+1; x++){
			for(int y = 0; y < HEIGHT_IN_TILES+1; y++){
				shapeRenderer.line((x * TILE_SIZE)+STEP_SIZE, 0+STEP_SIZE, (x * TILE_SIZE)+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE);
				shapeRenderer.line(0+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE, (x * TILE_SIZE)+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE);
			}
		}
	}
	
	public void renderChunkGrid(ShapeRenderer shapeRenderer){		
		for(int x = 0; x < WIDTH_IN_TILES+1; x+=TILES_INSIDE_CHUNK){
			for(int y = 0; y < HEIGHT_IN_TILES+1; y+=TILES_INSIDE_CHUNK){
				shapeRenderer.line((x * TILE_SIZE)+STEP_SIZE, 0+STEP_SIZE, (x * TILE_SIZE)+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE);
				shapeRenderer.line(0+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE, (x * TILE_SIZE)+STEP_SIZE, (y * TILE_SIZE)+STEP_SIZE);
			}
		}
	}
	
	public final Chunk[][] getChunksMap() {
		return chunksMap;
	}

	public final void setChunksMap(Chunk[][] chunksMap) {
		this.chunksMap = chunksMap;
	}
	
	public final Chunk getChunk(final int x, final int y) {
		return chunksMap[x][y];
	}
	
	public final void setChunk(final int x, final int y, final Chunk chunk) {
		chunksMap[x][y] = chunk;
	}
	
	public final Tile[][] getTilesMap() {
		return tilesMap;
	}
	
	public final Tile getTile(final int x, final int y) {
		return tilesMap[x][y];
	}

	public final void setTile(final int x, final int y, final Tile tile) {
		tilesMap[x][y] = tile;
	}
	
	public final void setTilesMap(Tile[][] tilesMap) {
		this.tilesMap = tilesMap;
	}

	public final TiledMap getMap() {
		return map;
	}

	public final void setMap(TiledMap map) {
		this.map = map;
	}

	public final void setTilesLayer(TiledMapTileLayer newTileslayer, final String layerName) {
//		for(MapLayer layer : map.getLayers()){
//			if(!layer.getName().equalsIgnoreCase(layerName)){
//				System.out.println("layer name: "+layer.getName());
//				map.getLayers().remove(layer);
//				map.getLayers().add(layer);
//			}else{
//				System.out.println("layer name: "+layer.getName());
//				map.getLayers().remove(map.getLayers().getIndex(layerName));
//				map.getLayers().add(newTileslayer);
//			}
//		}
		
		map.getLayers().remove(map.getLayers().getIndex(layerName));
		map.getLayers().add(newTileslayer);
	}

	public final int getCurrentPopulation() {
		return currentPopulation;
	}

	public final void setCurrentPopulation(int currentPopulation) {
		this.currentPopulation = currentPopulation;
	}
	
	public final void addCurrentPopulation(final int value){
		this.currentPopulation += value;
	}

	public final void removeCurrentPopulation(final int value){
		this.currentPopulation -= value;
	}
	
	public final int getMaxPopulation() {
		return maxPopulation;
	}

	public final void setMaxPopulation(int maxPopulation) {
		this.maxPopulation = maxPopulation;
	}
	
	public final void addMaxPopulation(final int value){
		this.maxPopulation += value;
	}
	
	public final void removeMaxPopulation(final int value){
		this.maxPopulation -= value;
	}

	public final int getCurrentStorage() {
		return currentStorage;
	}

	public final void setCurrentStorage(int currentStorage) {
		this.currentStorage = currentStorage;
	}
	
	public final void addCurrentStorage(final int value){
		this.currentStorage += value;
	}

	public final void removeCurrentStorage(final int value){
		this.currentStorage -= value;
	}
	
	public final int getMaxStorage() {
		return maxStorage;
	}

	public final void setMaxStorage(int maxStorage) {
		this.maxStorage = maxStorage;
	}
	
	public final void addMaxStorage(final int value){
		this.maxStorage += value;
	}

	public final void removeMaxStorage(final int value){
		this.maxStorage -= value;
	}
	
	public final int getOrganizationType() {
		return organizationType;
	}

	public final void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}
	
	public final boolean containsChunks(){
		for(int x = 0; x < WIDTH_IN_TILES; x++){
			for(int y = 0; y < HEIGHT_IN_TILES; y++){
				if(!chunksMap[x][y].isEmpty()){	//GIVES NULLPOINTER EXCEPTION
					return true;
				}
			}
		}
		return false;
	}
}
