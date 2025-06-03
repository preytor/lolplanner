package com.preytor.loldomainplanner;

import com.badlogic.gdx.Game;
import com.preytor.loldomainplanner.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
	
	public static final boolean Debug = true;

	@Override
	public void create () {
		Assets.loadBuildings();
//		Assets.loadSprites();
		Assets.loadCategories();
		Assets.loadOrganizations();
		Assets.loadOrganizationCategories();
		setScreen(new MainMenuScreen(this));
	}
}
