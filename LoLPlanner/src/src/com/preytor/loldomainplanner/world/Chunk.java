package com.preytor.loldomainplanner.world;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Chunk{

	private Drawable texture;
	private int x;
	private int y;
	
	public Chunk() {
	}

	public Chunk(final Drawable texture) {
		this.texture = texture;
	}
	
	public Chunk(final Drawable Drawable, final int x, final int y) {
		setTexture(texture);
		setX(x);
		setY(y);
	}

	public final Drawable getTexture() {
		return texture;
	}

	public final void setTexture(Drawable texture) {
		this.texture = texture;
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}
	
	public final boolean isEmpty(){
		if(texture==null)
			return true;
		return false;
	}
}
