package com.preytor.loldomainplanner.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.preytor.loldomainplanner.hud.Hud;
import com.preytor.loldomainplanner.world.Chunk;
import com.preytor.loldomainplanner.world.Tile;
import com.preytor.loldomainplanner.world.World;

public class GameControls implements InputProcessor {

	World world;
	TiledMap map;
	private boolean isDrawingBuilding = false;
	private boolean isDrawingChunk = false;
	

	
	public GameControls(World world){
		this.world = world;
		this.map = world.getMap();
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	//EL WIDTH HAY QUE HACERLO ESCALABLE RESPECTO AL RESTO, QUE SI NO NO LO DETECTA UNA VEZ RESIZEADO
	public void onUpdate(final Vector3 mouse_position, final int inHandId, Hud hud, final World world, OrthographicCamera mainMenuCam, final Drawable inHandTexture){
		if(mouse_position.x>=0 && mouse_position.y >=0 && mouse_position.x<=(World.TILES_INSIDE_CHUNK*World.TILE_SIZE*World.getWIDTH()) && mouse_position.y<=(World.TILES_INSIDE_CHUNK*World.TILE_SIZE*World.getHEIGHT())){
			if(!(Gdx.input.getX()>=0 && Gdx.input.getY() >=0 && Gdx.input.getX()<=hud.getItemsTable().getWidth() && Gdx.input.getY()<=Gdx.graphics.getHeight())){
				if(isDrawingBuilding()){
					if (Gdx.input.isButtonPressed(Buttons.LEFT)){
						try {
							getSelectedTile(mouse_position);
							setTile(mouse_position, inHandId);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){//IS SHIFTING
							setDrawingBuilding(false);
						}
					}
				}
				if(isDrawingChunk()){
					if (Gdx.input.isButtonPressed(Buttons.LEFT)){
						try {
							setChunk(mouse_position, inHandTexture, hud);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){//IS SHIFTING
							setDrawingChunk(false);
						}
					}
				}
				if(Gdx.input.isButtonPressed(Buttons.RIGHT)){
					try {
						removeTile(mouse_position);
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
//				if(Gdx.input.isButtonPressed(Buttons.MIDDLE)){	//TODO: FOR TESTING
//					System.out.println("saving map");
//					try {
//						ExecutorService pool = Executors.newSingleThreadExecutor();
//						final Json json = new Json();
//						Runnable run = new Runnable(){
//
//							@Override
//							public void run() {
//
//								System.out.println(json.prettyPrint(world));
//								
//							}
//						};
//						pool.submit(run);
//						pool.execute(run);
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
	}

	private void setChunk(final Vector3 mouse_position, final Drawable inHandTexture, Hud hud) {
		int coordX = (int) (mouse_position.x/World.CHUNK_SIZE);
		int coordY = (int) (mouse_position.y/World.CHUNK_SIZE);
		
		for(MapLayer tlay : world.getMap().getLayers()){
			System.out.println("tlay chunk name 1: "+tlay.getName());
		}
		
		if(inHandTexture!=null){

			world.setChunk(coordY, coordX, new Chunk(inHandTexture, coordX, coordY));

			MapLayer tempLayer = world.getMap().getLayers().get(world.getMap().getLayers().getIndex("chunk"));
			Cell cell = new Cell();

			hud.getChunkTexture().getTextureData().prepare();
			Pixmap pixmapfinished = new Pixmap(hud.getChunkTexture().getWidth()*2, hud.getChunkTexture().getHeight()*2 ,hud.getChunkTexture().getTextureData().getFormat());
			pixmapfinished.drawPixmap(hud.getChunkTexture().getTextureData().consumePixmap(), 
					0, 0, hud.getChunkTexture().getWidth(), hud.getChunkTexture().getHeight(),
					0, 0, hud.getChunkTexture().getWidth()*2, hud.getChunkTexture().getHeight()*2);
			Texture texture = new Texture(pixmapfinished);
			pixmapfinished.dispose();
			
			cell.setTile(new StaticTiledMapTile(new TextureRegion(texture)));
			
			((TiledMapTileLayer) tempLayer).setCell(coordX, coordY, cell);
			world.setTilesLayer((TiledMapTileLayer) tempLayer, "chunk");
			
			for(MapLayer tlay : world.getMap().getLayers()){
				System.out.println("tlay chunk name 2: "+tlay.getName());
			}
			setDrawingChunk(false);
		}		
	}

	private final Tile getSelectedTile(final Vector3 mouse_position){
		
		int coordX = (int) (mouse_position.x/World.TILE_SIZE);
		int coordY = (int) (mouse_position.y/World.TILE_SIZE);
		
		System.out.println("coordX: "+coordX+" coordY: "+coordY);
		
		if(world.getTile(coordY, coordX)!=null){
			System.out.println("selected tile: "+world.getTile(coordY, coordX).getName());
		}else{
			System.out.println("selected tile is null");
		}
		return null;
	}
	
	
///////TODO	IN SET TILE AND DELETE TILE, CREATE A NEW THREAD THAT DOES IT SO IT DOESNT LAG
	private void setTile(final Vector3 mouse_position, final int inHandId){
		
		int coordX = (int) (mouse_position.x/World.TILE_SIZE);
		int coordY = (int) (mouse_position.y/World.TILE_SIZE);
		
		for(MapLayer tlay : world.getMap().getLayers()){
			System.out.println("tlay building name 1: "+tlay.getName());
		}
		
		if(world.getTile(coordY, coordX)!=null){
			if(world.getTile(coordY, coordX).getId()!=inHandId){
				//TODO GET IF TILE IS ADJACENT IF IT CAN BE PLACED
				
				world.removeCurrentPopulation(world.getTile(coordY, coordX).getAddsPopulation());
				world.addMaxPopulation(world.getTile(coordY, coordX).getRemovesPopulation());
				world.removeCurrentStorage(world.getTile(coordY, coordX).getAddsStorage());
				world.addMaxStorage(world.getTile(coordY, coordX).getRemovesStorage());
				
				world.setTile(coordY, coordX, new Tile(inHandId, Utils.getBuildingNameFromId(inHandId), Utils.getBuildingTextureNameFromId(inHandId), 
														Utils.getBuildingDistanceBetweenSameFromId(inHandId), Utils.getBuildingNeedsAdjacentFromId(inHandId), 
														Utils.getBuildingCanBeBuiltIntFromId(inHandId), Utils.getGivesPopulation(inHandId), 
														Utils.getRemovesMaxPopulation(inHandId), Utils.getGivesStorage(inHandId), Utils.getGivesMaxStorage(inHandId)));
				MapLayer tempLayer = world.getMap().getLayers().get(world.getMap().getLayers().getIndex("building"));
				Cell cell = new Cell();
	
				cell.setTile(new StaticTiledMapTile(new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/"+Utils.getBuildingTextureNameFromId(inHandId)+".png")))));
	
				world.addCurrentPopulation(Utils.getGivesPopulation(inHandId));
				world.removeMaxPopulation(Utils.getRemovesMaxPopulation(inHandId));
				world.addCurrentStorage(Utils.getGivesStorage(inHandId));
				world.removeMaxStorage(Utils.getGivesMaxStorage(inHandId));
				
				((TiledMapTileLayer) tempLayer).setCell(coordX, coordY, cell);
				world.setTilesLayer((TiledMapTileLayer) tempLayer, "building");
				
				for(MapLayer tlay : world.getMap().getLayers()){
					System.out.println("tlay building name2: "+tlay.getName());
				}
			}else{
				System.out.println("selected tile is already the same");
			}
		}else{
			System.out.println("selected tile is null");
		}
	}
	
	private void removeTile(final Vector3 mouse_position){
		
		int coordX = (int) (mouse_position.x/World.TILE_SIZE);
		int coordY = (int) (mouse_position.y/World.TILE_SIZE);
		
		if(world.getTile(coordY, coordX)!=null){
			MapLayer tempLayer = world.getMap().getLayers().get(world.getMap().getLayers().getCount()-1);
			Cell cell = new Cell();
			//TODO: FOR SOME REASON DOESNT REMOVE THE CURRENT POPULATION, ADDS THE MAX POPULATION OR REMOVES THE CURRENT STORAGE
			cell.setTile(null);
			//Utils.getGivesPopulation(inHandId)
			world.removeCurrentPopulation(world.getTile(coordY, coordX).getAddsPopulation());
			world.addMaxPopulation(world.getTile(coordY, coordX).getRemovesPopulation());
			world.removeCurrentStorage(world.getTile(coordY, coordX).getAddsStorage());
			world.addMaxStorage(world.getTile(coordY, coordX).getRemovesStorage());
			world.setTile(coordY, coordX, new Tile());
			((TiledMapTileLayer) tempLayer).setCell(coordX, coordY, cell);
			world.setTilesLayer((TiledMapTileLayer) tempLayer, "building");
			
			System.out.println("deleted building inside the tile: "+world.getTile(coordX, coordY).getId());
		}else{
			System.out.println("selected tile is null");
		}
	}

	public boolean isDrawingChunk() {
		return isDrawingChunk;
	}

	public void setDrawingChunk(boolean isDrawingChunk) {
		this.isDrawingChunk = isDrawingChunk;
	}

	public boolean isDrawingBuilding() {
		return isDrawingBuilding;
	}

	public void setDrawingBuilding(boolean isDrawingBuilding) {
		this.isDrawingBuilding = isDrawingBuilding;
	}
}
