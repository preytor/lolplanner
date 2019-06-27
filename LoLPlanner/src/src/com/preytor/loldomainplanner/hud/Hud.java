package com.preytor.loldomainplanner.hud;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.preytor.loldomainplanner.Assets;
import com.preytor.loldomainplanner.Main;
import com.preytor.loldomainplanner.screens.MainMenuScreen;
import com.preytor.loldomainplanner.utils.Utils;
import com.preytor.loldomainplanner.world.BuildingCategory;
import com.preytor.loldomainplanner.world.OrganizationCategories;
import com.preytor.loldomainplanner.world.Tile;
import com.preytor.loldomainplanner.world.WorldSaver;

public class Hud {

	final String discordLink = "https://discord.gg/2dxmPeM";
	
	public enum Displays { MAIN_MENU, CREATING_MAP, CREATED_MAP, LOADING_MAP, SETTINGS, HELP;}
	
	private OrthographicCamera hudCam;
	public final OrthographicCamera getHudCam() {
		return hudCam;
	}

	public final void setHudCam(OrthographicCamera hudCam) {
		this.hudCam = hudCam;
	}

	private Viewport hudport;
	
	private Table optionsTable;

	private Table guiTable;
	private Table itemsTable;
	private Table hotbarTable;
	private Table gridsTable;
	
	private TextureAtlas lolPlannerAtlas;
	private Skin lolPlannerSkin;
	private Texture chunkTexture;
	
	TextButtonStyle papyrusTextButtonStyle = new TextButtonStyle();
	public BitmapFont font = new BitmapFont();

	Window.WindowStyle dialogStyle = new Window.WindowStyle();
	private Window settsDialog;
	private Window helpDialog;
	
	private TextButton createMapButton;
	private TextButton loadMapButton;
	private TextButton settingsButton;
	private TextButton helpButton;
	private TextButton exitButton;
	
	private ImageButton discordButton;
	
	private Label populationLabel;
	private String populationCount;
	private Label storageLabel;
	private String storageCount;
	private Label domainTypeLabel;
	
	private Stage hudStage;
	private Displays displays;
	
	private CheckBox tileGrids;
	private CheckBox chunkGrids;

	private int selectedOrganization = 0;
	
	private Image popicon;
	private Image storageicon;
	
	LabelStyle papyrusLabelStyle;
	
	public Hud(SpriteBatch batch) {
		
		hudStage = new Stage();
		
		hudCam = new OrthographicCamera(1024, 600);
		hudport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), hudCam);	//SCRETCHVIEWPORT
		hudport.apply(true);
		
		populationLabel = new Label(populationCount, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		storageLabel = new Label(storageCount, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		domainTypeLabel = new Label(storageCount, new Label.LabelStyle(new BitmapFont(), Color.GOLD));
		
		this.setDisplays(Displays.MAIN_MENU);
		
		lolPlannerAtlas = new TextureAtlas("textures/ui/mainmenu/lolplanner.atlas");
		lolPlannerSkin = new Skin(lolPlannerAtlas);
		
		papyrusLabelStyle = new LabelStyle();
		papyrusLabelStyle.font = font;
		papyrusLabelStyle.background = lolPlannerSkin.getDrawable("backgroundmenu");
		papyrusLabelStyle.fontColor = Color.WHITE;
		
		papyrusTextButtonStyle.up = lolPlannerSkin.getDrawable("backgroundmenu");
		papyrusTextButtonStyle.down = lolPlannerSkin.getDrawable("backgroundmenu");
		papyrusTextButtonStyle.pressedOffsetX = 1;
		papyrusTextButtonStyle.pressedOffsetY = -1;
		papyrusTextButtonStyle.font = font;
		
		final CheckBoxStyle checkboxStyle = new CheckBoxStyle();
		checkboxStyle.checked = lolPlannerSkin.getDrawable("backgroundmenu");
		checkboxStyle.up = lolPlannerSkin.getDrawable("backgroundmenu");
		
		checkboxStyle.checkboxOff = lolPlannerSkin.getDrawable("unchecked");
		checkboxStyle.checkboxOn = lolPlannerSkin.getDrawable("checked");
		checkboxStyle.font = font;
		checkboxStyle.fontColor = Color.WHITE;
		
//		viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
//		stage = new Stage(viewport, batch);

		guiTable = new Table();
		guiTable.top();
		guiTable.setFillParent(true);
		
		optionsTable = new Table();
		optionsTable.setBounds(5, (Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/4)-5, Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/4);
		optionsTable.setBackground(lolPlannerSkin.getDrawable("background2"));
		
		itemsTable = new Table();
		itemsTable.setBounds(5, 5, Gdx.graphics.getWidth()/5, (Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/4)-15);
		itemsTable.setBackground(lolPlannerSkin.getDrawable("background2"));
		
		hotbarTable = new Table();
		hotbarTable.setBounds(Gdx.graphics.getWidth()/4.5F, 5, Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/3F), Gdx.graphics.getHeight()/10);
//		hotbarTable.setBackground(lolPlannerSkin.getDrawable("background2"));
		
		gridsTable = new Table();
		gridsTable.setBounds(Gdx.graphics.getWidth()-(Gdx.graphics.getWidth()/10), 5, Gdx.graphics.getWidth()/15, Gdx.graphics.getHeight()/10);
//		gridsTable.setBackground(lolPlannerSkin.getDrawable("background2"));
		
		createMapButton = new TextButton("Create Map", papyrusTextButtonStyle);
		createMapButton.setName("createButton");
		createMapButton.setBounds(0, 0, 200, 200);
		createMapButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	
		createMapButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {

				itemsTable.clear();
				
				if(!displays.equals(Displays.CREATING_MAP)){
					
					setDisplays(Displays.CREATING_MAP);
					
					final TextFieldStyle papyrusTextFieldStyle = new TextFieldStyle();
					papyrusTextFieldStyle.background = lolPlannerSkin.getDrawable("backgroundmenu");
					papyrusTextFieldStyle.font = font;
					papyrusTextFieldStyle.fontColor = Color.WHITE;
					

					TreeStyle trestyle = new TreeStyle(lolPlannerSkin.getDrawable("more"), lolPlannerSkin.getDrawable("less"), lolPlannerSkin.getDrawable("blank"));	//blank
					
					Tree organizationTree = new Tree(trestyle){
					    @Override
					    public void setStyle(TreeStyle style) {
					        super.setStyle(style);
					        try {
					            java.lang.reflect.Field field = Tree.class.getDeclaredField("indentSpacing");
					            field.setAccessible(true);
					            field.set(this, -8); // This is how much you want each plus and minus indented.
					                                // Normally this is set to something like
					                                //     Math.max(style.plus.getMinWidth(), style.minus.getMinWidth())
					        } catch (NoSuchFieldException e) {
					            e.printStackTrace();
					        } catch (IllegalAccessException e) {
					            e.printStackTrace();
					        }
					    }
					};
					organizationTree.setIconSpacing(20, 20);

					for(OrganizationCategories categ : Assets.organizationCategories_map){
						Tree.Node newCateg = new Tree.Node(new Label(categ.getName(), papyrusLabelStyle));
						newCateg.setSelectable(false);
						for(int organization : categ.getOrganizations()){
							Label label = new Label(Assets.organizations_map.get(organization).getName(), papyrusLabelStyle);
							label.setName(""+Assets.organizations_map.get(organization).getId());
							
							Tree.Node builg = new Tree.Node(label);
							builg.expandTo();
							builg.setExpanded(true);
							builg.setSelectable(true);
							newCateg.add(builg);

						}
						organizationTree.add(newCateg);
					}
					
					final Label organizationTypeLabel = new Label("Selected Organization: ", papyrusLabelStyle);
					organizationTypeLabel.setName("organizationTypeLabel");
					
					organizationTree.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							if(event.getTarget() instanceof Label){
								if(event.getTarget().getName()!=null){
									if(Main.Debug){
										System.out.println("clicked organization with ID: "+event.getTarget().getName());
									}
									selectedOrganization=Integer.parseInt(event.getTarget().getName());
									organizationTypeLabel.setText("Selected Organization: \n"+Utils.getOrganizationNameFromId(Integer.parseInt(event.getTarget().getName())));
								}
							}
						}
					});

					
					
					final Table container2 = new Table();
					container2.setSize(200, 1000);
					container2.bottom();
//					container.setLayoutEnabled(true);
//					container.setHeight(10000);
//					container.setBounds(0, 0, 0, 0);
//					itemsTable.add(tree).align(Align.bottom).row();
					
					final CheckBox modeType = new CheckBox("Creative mode", checkboxStyle);
					modeType.setChecked(true);
					
					final Label widthLabel = new Label("Chunk width", papyrusLabelStyle);
					widthLabel.setName("widthLabel");
					widthLabel.setAlignment(Alignment.CENTER.ordinal());
					
					final TextField widthTextField = new TextField("1", papyrusTextFieldStyle);
					widthTextField.setName("widthTextField");
					widthTextField.setAlignment(Align.center);
					widthTextField.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							widthTextField.setText("");
						}
					});
					
					final Label heightLabel = new Label("Chunk height", papyrusLabelStyle);
					heightLabel.setName("heightLabel");
					heightLabel.setAlignment(Alignment.CENTER.ordinal());
					
					final TextField heightTextField = new TextField("1", papyrusTextFieldStyle);
					heightTextField.setAlignment(Align.center);
					heightTextField.setName("heightTextField");
					heightTextField.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							heightTextField.setText("");
						}
					});
	
					final TextButton generateButton = new TextButton("Create", papyrusTextButtonStyle);
					generateButton.setName("generateButton");
					generateButton.setBounds(0, 0, 200, 200);
					generateButton.addListener(new ClickListener(){
						@Override
						public void clicked(InputEvent event, float x, float y) {
							MainMenuScreen.beginWorld(modeType.isChecked(), Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()), selectedOrganization);
							showMappingMenu();
						}
					});

					itemsTable.add(organizationTree).align(Align.top).row();
					itemsTable.add(organizationTypeLabel).align(Align.top).size(100, 15).padTop(5).row();

					container2.add(modeType).size(100, 15).padTop(30).row();
					container2.add(widthLabel).size(100, 15).padTop(5).row();
					container2.add(widthTextField).size(100, 15).padTop(5).row();
					container2.add(heightLabel).size(100, 15).padTop(5).row();
					container2.add(heightTextField).size(100, 15).padTop(5).row();
					
					container2.add(generateButton).size(100, 15).padTop(15).row();
					
					itemsTable.add(container2).align(Align.bottom).row();
				}
			}
		});
		
		loadMapButton = new TextButton("Load Map", papyrusTextButtonStyle);
		loadMapButton.setName("loadButton");
		loadMapButton.setBounds(0, 0, 200, 200);
		loadMapButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	
		loadMapButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            String extension = Utils.getExtension(file);
		            if (extension != null) {
		                if (extension.equals("lol")) {
		                	System.out.println("Opening: " + file.getAbsolutePath());
		                	
		                	//loading map method()
		                	MainMenuScreen.loadWorld(file);
		    				System.out.println("loaded map");
		    				showMappingMenu();//TODO HERE
		                } else {
		                	//cant load this file
		                }
		            }
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		
		settingsButton = new TextButton("Settings", papyrusTextButtonStyle);
		settingsButton.setName("settingsButton");
		settingsButton.setBounds(0, 0, 200, 200);
		settingsButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	
		settingsButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogStyle.background = lolPlannerSkin.getDrawable("backgroundmenu");
				dialogStyle.titleFont = font;
				dialogStyle.titleFontColor = Color.WHITE;
				
				settsDialog = new Window("Settings", dialogStyle);
				Table settingsTable = new Table();
				settsDialog.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
				settsDialog.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Align.center);
				Label screenResolution =  new Label("Screen size", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
				
				
				settingsTable.add(screenResolution).row();
				settsDialog.add(settingsTable);
				settingsTable.debug();
				settsDialog.layout();
				settsDialog.pack();
				hudStage.addActor(settsDialog);

			}
		});
		
		helpButton = new TextButton("Help", papyrusTextButtonStyle);
		helpButton.setName("helpButton");
		helpButton.setBounds(0, 0, 200, 200);
		helpButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	
		helpButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogStyle.background = lolPlannerSkin.getDrawable("backgroundmenu");
				dialogStyle.titleFont = font;
				dialogStyle.titleFontColor = Color.WHITE;
				
				helpDialog = new Window("Help", dialogStyle);
				Table helpTable = new Table();
				helpTable.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
				helpTable.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Align.center);
				helpDialog.add(helpTable);
				helpTable.debug();
				hudStage.addActor(settsDialog);
			}
		});
		
		exitButton = new TextButton("Exit", papyrusTextButtonStyle);
		exitButton.setName("exitButton");
		exitButton.setBounds(0, 0, 200, 200);
		exitButton.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
	
		exitButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				System.exit(1);
			}
		});
		
		discordButton = new ImageButton(lolPlannerSkin.getDrawable("discordicon"));
		discordButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Desktop.isDesktopSupported()) {
				    try {
						Desktop.getDesktop().browse(new URI(discordLink));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		optionsTable.add(createMapButton).size(100, 15).padTop(5).row();
//		optionsTable.getCell(createMapButton);
		optionsTable.add(loadMapButton).size(100, 15).padTop(5).row();
//		optionsTable.add(settingsButton).size(100, 15).padTop(5).row();
//		optionsTable.add(helpButton).size(100, 15).padTop(5).row();
		optionsTable.add(exitButton).size(100, 15).padTop(5).row();
		optionsTable.add(discordButton).size(100, 15).padTop(5).row();
		hudStage.addActor(optionsTable);
		

		
		itemsTable.background(lolPlannerSkin.getDrawable("background3"));
		itemsTable.align(Align.topLeft);
		itemsTable.layout();
		
				
		hudStage.addActor(guiTable);
		hudStage.addActor(itemsTable);		
	}

	public void onUpdate(OrthographicCamera gamecam, final Vector3 mouse_position){
		gamecam.update();
		Gdx.input.setInputProcessor(hudStage);
	}
	
	public void onUpdate(OrthographicCamera gamecam, final Vector3 mouse_position, final int organizationType, final int currentPopulation, final int maxPopulation, final int currentStorage, final int maxStorage){
		gamecam.update();
		
		if(currentPopulation>=maxPopulation){
			populationLabel.setColor(Color.RED);
		}else if(currentPopulation>=maxPopulation*0.75F){
			populationLabel.setColor(Color.ORANGE);
		}else if(currentPopulation<=(maxPopulation*0.75F)){
			populationLabel.setColor(Color.GREEN);
		}
		
		if(currentStorage>=maxStorage){
			storageLabel.setColor(Color.RED);
		}else if(currentStorage>=maxStorage*0.75F){
			storageLabel.setColor(Color.ORANGE);
		}else if(currentStorage<=(maxStorage*0.75F)){
			storageLabel.setColor(Color.GREEN);
		}
		
		populationCount=currentPopulation+"/"+maxPopulation;
		storageCount=currentStorage+"/"+maxStorage;
		populationLabel.setText(populationCount);
		storageLabel.setText(storageCount);
		
		Gdx.input.setInputProcessor(hudStage);
	}

	public void resize (int width, int height) {
		hudStage.getViewport().update(width, height, true);
	}
	
	public Stage getStage() {
		return hudStage;
	}

	public void dispose() {
		
	}
	
	/**
	 * 
	 * @param buildingsID: Array of the category
	 * @param ID: Id of the actual building
	 * @return
	 */
	public boolean categContainsBuilding(final int[] buildingsID, final int ID) {
		for(int i = 0; i < buildingsID.length; i++){
			if(buildingsID[i]==ID){
				return true;
			}
		}
		return false;
	}
	
	public void showMappingMenu(){
		guiTable.clear();
		itemsTable.clear();
		hotbarTable.clear();
		gridsTable.clear();
		domainTypeLabel.setText(Utils.getOrganizationNameFromId(selectedOrganization));
		populationLabel.setText(populationCount);
		storageLabel.setText(storageCount);

		popicon = new Image(lolPlannerSkin.getDrawable("pop"));
		storageicon = new Image(lolPlannerSkin.getDrawable("stock"));
		
		popicon.setPosition(15, 0);
		popicon.setSize(8, 8);
		storageicon.setPosition(15, 0);
		storageicon.setSize(8, 8);
		
		
		guiTable.add(domainTypeLabel).padTop(10).row();
		guiTable.add(populationLabel).padTop(10);
		guiTable.add(popicon).row();
		guiTable.add(storageLabel).padTop(5);
		guiTable.add(storageicon).row();
		
		setDisplays(Displays.CREATED_MAP);		
//		itemsTable.clearChildren();
		
		TreeStyle trestyle = new TreeStyle(lolPlannerSkin.getDrawable("more"), lolPlannerSkin.getDrawable("less"), lolPlannerSkin.getDrawable("blank"));	//blank

		Tree tree = new Tree(trestyle){
		    @Override
		    public void setStyle(TreeStyle style) {
		        super.setStyle(style);
		        try {
		            java.lang.reflect.Field field = Tree.class.getDeclaredField("indentSpacing");
		            field.setAccessible(true);
		            field.set(this, -8); // This is how much you want each plus and minus indented.
		                                // Normally this is set to something like
		                                //     Math.max(style.plus.getMinWidth(), style.minus.getMinWidth())
		        } catch (NoSuchFieldException e) {
		            e.printStackTrace();
		        } catch (IllegalAccessException e) {
		            e.printStackTrace();
		        }
		    }
		};
		tree.setIconSpacing(20, 20);

		for(BuildingCategory categ : Assets.categories_map){
			Tree.Node newCateg = new Tree.Node(new Label(categ.getName(), papyrusLabelStyle));
			newCateg.setSelectable(false);
			for(Entry<Integer, Tile> building : Assets.buildings_map.entrySet()){
				
				if(categContainsBuilding(categ.getBuildingsID(), building.getValue().getId())){
						if(Utils.organizationContainsBuilding(selectedOrganization, building.getValue().getId())){
						Label label = new Label(building.getValue().getName(), papyrusLabelStyle);
						label.setName(""+building.getValue().getId());
						
						Tree.Node builg = new Tree.Node(label);
						builg.expandTo();
						builg.setExpanded(true);
						builg.setSelectable(true);
						newCateg.add(builg);
					}
				}
			}
			tree.add(newCateg);
		}
		
		tree.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(event.getTarget() instanceof Label){
					if(event.getTarget().getName()!=null){
						MainMenuScreen.setIsPainting(Integer.parseInt(event.getTarget().getName()));
						if(Main.Debug){
							System.out.println("clicked building with ID: "+event.getTarget().getName());
						}
					}
				}
			}
		});
		
		ScrollPane treeScroll = new ScrollPane(tree);
		treeScroll.setScrollingDisabled(true, false);
		treeScroll.setScrollbarsOnTop(true);
		treeScroll.setClamp(false);
		treeScroll.setOverscroll(true, false);
		treeScroll.setForceScroll(false, true);
		treeScroll.setFlickScroll(true);
	
		
		itemsTable.add(treeScroll).fill().expand().row();
		
		TextButton loadChunkButton = new TextButton("Load Chunk", papyrusTextButtonStyle);
		loadChunkButton.setName("loadChunkButton");
		loadChunkButton.setBounds(0, 0, 100, 100);
		loadChunkButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				final JFileChooser fc = new JFileChooser();
		        int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            
		            String extension = Utils.getExtension(file);
		            if (extension != null) {
		                if (extension.equals("jpg")) {
		                	System.out.println("Opening: " + file.getName());
		                	MainMenuScreen.setIsPainting(0);
		                	MainMenuScreen.setDrawingBuilding(false);
		                	MainMenuScreen.setDrawingChunk(true);
		                	setChunkTexture(new Texture(file.getAbsolutePath()));
		                	Drawable chunkImage = new Image(getChunkTexture()).getDrawable();
		                	MainMenuScreen.setInHandTexture(chunkImage);
		    				System.out.println("loaded chunk");
		                } else {
		                	System.out.println("can't load chunk");
		                }
		            }
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		itemsTable.add(loadChunkButton).expand(true, false).fill(true, false).row();
		
		TextButton SaveMapButton = new TextButton("Save Map", papyrusTextButtonStyle);
		SaveMapButton.setName("SaveMapButton");
		SaveMapButton.setBounds(0, 0, 100, 100);
		SaveMapButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				JFileChooser fc = new JFileChooser();
				
	        	fc.setDialogTitle("select folder");
	        	fc.setSelectedFile(new File("map.lol"));
	        	fc.setFileFilter(new FileNameExtensionFilter("lol file","lol"));
				//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int returnVal = fc.showSaveDialog(null);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	
		            File folder = fc.getCurrentDirectory();
		            File file = fc.getSelectedFile();
		            
					System.out.println("saving map");
					
					String filename = fc.getSelectedFile().toString();
					if (!filename .endsWith(".lol"))
						filename += ".lol";
					
					WorldSaver worldsaver = new WorldSaver(MainMenuScreen.getWorld());
					worldsaver.generateSave(folder, file);
		            
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		itemsTable.add(SaveMapButton).expand(true, false).fill(true, false).row();
		
		TextButton ExportAsImageButton = new TextButton("Export as image", papyrusTextButtonStyle);
		ExportAsImageButton.setName("ExportAsImageButton");
		ExportAsImageButton.setBounds(0, 0, 100, 100);
		ExportAsImageButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//GET IF IT HAS ANY CHUNK, IF NOT THEN YOU PRINT THE PLAINS INSIDE IT
				//SAME AS RENDERING THE MAP, BUT INSTEAD YOU PRINT THE TEXTURES THERE
				JFileChooser fc = new JFileChooser();
				
	        	fc.setDialogTitle("select folder");
	        	fc.setSelectedFile(new File("map.png"));
	        	fc.setFileFilter(new FileNameExtensionFilter("png file","png"));
				//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int returnVal = fc.showSaveDialog(null);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	
		            File folder = fc.getCurrentDirectory();
		            File file = fc.getSelectedFile();
		            
					System.out.println("exporting map as image");
					
					String filename = fc.getSelectedFile().toString();
					if (!filename .endsWith(".png"))
						filename += ".png";
					
					WorldSaver worldsaver = new WorldSaver(MainMenuScreen.getWorld());
					worldsaver.generateSaveImage(MainMenuScreen.getWorld(), folder, file);
		            
		        } else {
		        	System.out.println("Open command cancelled by user.");
		        }
			}
		});
		itemsTable.add(ExportAsImageButton).expand(true, false).fill(true, false).row();
		
		final CheckBoxStyle checkboxGridStyle = new CheckBoxStyle();
		checkboxGridStyle.checkboxOff = lolPlannerSkin.getDrawable("unchecked");
		checkboxGridStyle.checkboxOn = lolPlannerSkin.getDrawable("checked");
		checkboxGridStyle.font = font;
		checkboxGridStyle.fontColor = Color.GRAY;

		tileGrids = new CheckBox("", checkboxGridStyle);
		tileGrids.setChecked(true);
		tileGrids.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainMenuScreen.setShowTileGrid(tileGrids.isChecked());
			}
		});
		chunkGrids = new CheckBox("", checkboxGridStyle);
		chunkGrids.setChecked(true);
		chunkGrids.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainMenuScreen.setShowChunkGrid(chunkGrids.isChecked());
			}
		});
		
		gridsTable.add(tileGrids).maxSize(15).padRight(6F);
		gridsTable.add(new Image(lolPlannerSkin.getDrawable("griddisplaytiles"))).row();
		gridsTable.add(chunkGrids).maxSize(15).padRight(6F);
		gridsTable.add(new Image(lolPlannerSkin.getDrawable("griddisplaychunks"))).row();

//		hotbarTable.debug();	//DISABLED FOR NOW
		
		hudStage.addActor(hotbarTable);
		hudStage.addActor(gridsTable);
	}
	
	public void setStage(Stage stage) {
		this.hudStage = stage;
	}
	
	public final Table getOptionsTable() {
		return optionsTable;
	}

	public final Table getItemsTable() {
		return itemsTable;
	}

	public final Table getHotbarTable() {
		return hotbarTable;
	}
	
	public final Table getGridsTable() {
		return gridsTable;
	}

	public int getSelectedOrganization() {
		return selectedOrganization;
	}

	public void setSelectedOrganization(int selectedOrganization) {
		this.selectedOrganization = selectedOrganization;
	}

	public final Displays getDisplays() {
		return displays;
	}

	public final void setDisplays(Displays displays) {
		this.displays = displays;
	}

	public final Skin getLolPlannerSkin() {
		return lolPlannerSkin;
	}

	public final CheckBox getTileGrids() {
		return tileGrids;
	}

	public final CheckBox getChunkGrids() {
		return chunkGrids;
	}

	public Texture getChunkTexture() {
		return chunkTexture;
	}

	public void setChunkTexture(Texture chunkTexture) {
		this.chunkTexture = chunkTexture;
	}
}
