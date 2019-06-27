package com.preytor.loldomainplanner.screens;

import java.awt.Frame;
import java.io.File;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.preytor.loldomainplanner.Main;
import com.preytor.loldomainplanner.hud.Hud;
import com.preytor.loldomainplanner.utils.GameControls;
import com.preytor.loldomainplanner.utils.Utils;
import com.preytor.loldomainplanner.world.World;

public class MainMenuScreen implements Screen {

	@SuppressWarnings("unused")
	private Main main;
	private Stage stage;
	private static OrthographicCamera mainMenuCam;
	private Viewport gameport;
	private Hud hud;
	private SpriteBatch designerBatch;

	//	static TextureAtlas lolPlannerBuildingAtlas = new TextureAtlas("textures/buildings/buildings.atlas");
//	static Skin lolPlannerSkin = new Skin(lolPlannerBuildingAtlas);
	private static GameControls gamecontrols;
	static World world;

	private Vector3 mouse_position = new Vector3(0,0,0);

	private static int inHandId;
	private static int organizationType;
	private static Drawable inHandTexture;

	private static Drawable popIcon;
	private static Drawable storageIcon;
	private static BitmapFont popFont = new BitmapFont();
	private static BitmapFont storageFont = new BitmapFont();

	static boolean loadedMap = false;
	static boolean showTileGrid = true;
	static boolean showChunkGrid = true;

	private float zoomAmount = 0.13F;
	
	public MainMenuScreen(Main main){
		this.main = main;
	}
	
	@Override
	public void show() {

		stage = new Stage();
		designerBatch = new SpriteBatch();
		hud = new Hud(designerBatch);
		Gdx.input.setInputProcessor(stage);
		
		mainMenuCam = new OrthographicCamera(1024, 600);
		gameport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), mainMenuCam);
		gameport.apply(true);

		stage.getCamera().update();
		
		popIcon = hud.getLolPlannerSkin().getDrawable("pop");
		storageIcon = hud.getLolPlannerSkin().getDrawable("stock");
	}
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		mainMenuCam.unproject(mouse_position);
		

		if(loadedMap){

			world.getRenderer().setView(mainMenuCam);
			world.getRenderer().render();
			
			if(showTileGrid || showChunkGrid){
				world.getShapeRender().begin();
				world.getShapeRender().setProjectionMatrix(mainMenuCam.combined);
				if(showTileGrid){
					world.renderTileGrid(world.getShapeRender());
				}
				if(showChunkGrid){
					world.renderChunkGrid(world.getShapeRender());
				}
				world.getShapeRender().end();
			}
			//TODO TAKE A LOOK AT THIS IF IT IS WORKING PROPERLY
			gamecontrols.onUpdate(mouse_position, inHandId, hud, world, mainMenuCam, inHandTexture);
		}
		
		//map
		stage.act(delta);
		stage.draw();
		
		hud.getStage().act(delta);
		hud.getStage().draw();
		
		designerBatch.begin();
		//hud
		if(!hud.getDisplays().equals(Hud.Displays.CREATED_MAP)){
			hud.onUpdate(mainMenuCam, mouse_position);
		}else{
			hud.onUpdate(mainMenuCam, mouse_position, world.getOrganizationType(), world.getCurrentPopulation(), world.getMaxPopulation(), world.getCurrentStorage(), world.getMaxStorage());
		}
		designerBatch.setProjectionMatrix(mainMenuCam.combined);

		if(inHandTexture!=null){
			if(gamecontrols.isDrawingBuilding()){
				inHandTexture.draw(designerBatch, mouse_position.x-40, mouse_position.y-40, 80, 80);
				//DRAW WHAT POPULATION GIVES AND STORAGE
				String popsText = "";
				String storageText = "";
				
				if(Utils.getGivesPopulation(inHandId)>0){
					popFont.setColor(Color.GREEN);
					popsText = "+"+Utils.getGivesPopulation(inHandId);
				}
	
				if(Utils.getRemovesMaxPopulation(inHandId)>0){
					popFont.setColor(Color.RED);
					popsText = "-"+Utils.getRemovesMaxPopulation(inHandId);
				}
				
				if(Utils.getGivesStorage(inHandId)>0){
					storageFont.setColor(Color.GREEN);
					storageText = "+"+Utils.getGivesStorage(inHandId);
				}
				
				if(Utils.getRemovesMaxStorage(inHandId)>0){
					storageFont.setColor(Color.RED);
					storageText = "-"+Utils.getRemovesMaxStorage(inHandId);
				}
				
				popFont.draw(designerBatch, popsText, mouse_position.x-60, mouse_position.y+15);
				if(!popsText.equalsIgnoreCase("")){
					//DRAW POP ICON
					popIcon.draw(designerBatch, mouse_position.x-85, mouse_position.y+6, 20, 20);
				}
				storageFont.draw(designerBatch, storageText, mouse_position.x-60, mouse_position.y);
				if(!storageText.equalsIgnoreCase("")){
					//DRAW storage ICON
					storageIcon.draw(designerBatch, mouse_position.x-85, mouse_position.y-18, 20, 20);
				}
			}
			if(gamecontrols.isDrawingChunk()){
				inHandTexture.draw(designerBatch, mouse_position.x-40, mouse_position.y-40, 160, 160);
			}
		}
		designerBatch.end();
		
		if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
				try {
					mainMenuCam.position.x-=1F;
					Thread.sleep(10);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
				try {
					mainMenuCam.position.x+=1F;
					Thread.sleep(10);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
				try {
					mainMenuCam.position.y+=1F;
					Thread.sleep(10);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
				try {
					mainMenuCam.position.y-=1F;
					Thread.sleep(10);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			mainMenuCam.position.x-=10F;
			hud.getHudCam().position.x-=10F;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			mainMenuCam.position.x+=10F;
			hud.getHudCam().position.x+=10F;
		if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
			mainMenuCam.position.y+=10F;
			hud.getHudCam().position.y+=10F;
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
			mainMenuCam.position.y-=10F;
			hud.getHudCam().position.y-=10F;
		
		
			
		if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN))
			try {
				mainMenuCam.zoom+=zoomAmount;
				Thread.sleep(60);
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP))
			try {
				mainMenuCam.zoom-=zoomAmount;
				Thread.sleep(60);
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
	@Override
	public void resize(int width, int height) {
	    stage.getViewport().update(width, height);
	    hud.resize(width, height);
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		if(Main.Debug){
			System.out.println("mainmenu screen disposed");
		}
		//TODO TAKE A LOOK AT THIS IF IT IS WORKING PROPERLY
//		main.dispose();
//		stage.dispose();
//		hud.dispose();
//		designerBatch.dispose();
//		lolPlannerBuildingAtlas.dispose();
//		lolPlannerSkin.dispose();
//		inHand = 0;
	}
	
	public static void loadWorld(final File saveFile){	
		world = new World(saveFile);
		loadedMap = true;
		if(Main.Debug){
			System.out.println("loaded world");
		}
		gamecontrols = new GameControls(world);
	}
	
	public static void beginWorld(final boolean mode, final int worldWidth, final int worldHeight, final int organizationType){	
		world = new World(mode, worldWidth, worldHeight, organizationType);
		loadedMap = true;
		if(Main.Debug){
			System.out.println("created world");
		}
		gamecontrols = new GameControls(world);
	}
	
	public static void setIsPainting(final int id){
		setInHandId(id);
		if(id>0){
			if(Gdx.files.internal("textures/buildings/"+Utils.getBuildingTextureNameFromId(inHandId)+".png").exists()){
				inHandTexture=new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("textures/buildings/"+Utils.getBuildingTextureNameFromId(inHandId)+".png"))));
		//		inHandTexture=lolPlannerSkin.getDrawable(Utils.getTextureNameFromId(inHandId));
			}else{
				Frame frame = new Frame();
				frame.setAlwaysOnTop(true);
				JOptionPane.showMessageDialog(frame, "couldn't find textures/buildings/"+Utils.getBuildingTextureNameFromId(inHandId)+".png");
			}
		}
		gamecontrols.setDrawingBuilding(true);
	}	
	
	public final int getInHandId() {
		return inHandId;
	}

	public final static void setInHandId(final int inHandId) {
		MainMenuScreen.inHandId = inHandId;
	}

	public static int getOrganizationType() {
		return organizationType;
	}

	public static void setOrganizationType(int organizationType) {
		MainMenuScreen.organizationType = organizationType;
	}
	
	public static final Drawable getPopIcon() {
		return popIcon;
	}

	public static final void setPopIcon(Drawable popIcon) {
		MainMenuScreen.popIcon = popIcon;
	}

	public static final Drawable getStorageIcon() {
		return storageIcon;
	}

	public static final void setStorageIcon(Drawable storageIcon) {
		MainMenuScreen.storageIcon = storageIcon;
	}

	public static final Drawable getInHandTexture() {
		return inHandTexture;
	}

	public static final void setInHandTexture(Drawable inHandTexture) {
		MainMenuScreen.inHandTexture = inHandTexture;
	}

	public static final boolean isShowTileGrid() {
		return showTileGrid;
	}

	public static final void setShowTileGrid(boolean showTileGrid) {
		MainMenuScreen.showTileGrid = showTileGrid;
	}

	public static final boolean isShowChunkGrid() {
		return showChunkGrid;
	}

	public static final void setShowChunkGrid(boolean showChunkGrid) {
		MainMenuScreen.showChunkGrid = showChunkGrid;
	}
	
	public static final boolean isDrawingBuilding() {
		return gamecontrols.isDrawingBuilding();
	}

	public static final void setDrawingBuilding(boolean isDrawingBuilding) {
		gamecontrols.setDrawingBuilding(isDrawingBuilding);
	}

	public static final boolean isDrawingChunk() {
		return gamecontrols.isDrawingChunk();
	}

	public static final void setDrawingChunk(boolean isDrawingChunk) {
		gamecontrols.setDrawingChunk(isDrawingChunk);
	}
	
	public static final World getWorld() {
		return world;
	}

	public static final void setWorld(World world) {
		MainMenuScreen.world = world;
	}
	
	public static final OrthographicCamera getMainMenuCam() {
		return mainMenuCam;
	}

}
