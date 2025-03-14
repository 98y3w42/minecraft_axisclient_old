package org.newdawn.slick.tests;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 * A test to demonstrate distance fields generated by Hiero being applied to
 * scaled fonts
 *
 * @author kevin
 */
public class DistanceFieldTest extends BasicGame {
	/** The font */
	private AngelCodeFont font;

	/**
	 * Create a new tester for the clip plane based clipping
	 */
	public DistanceFieldTest() {
		super("DistanceMapTest Test");
	}

	/**
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		font = new AngelCodeFont("testdata/distance.fnt", "testdata/distance-dis.png");
		container.getGraphics().setBackground(Color.black);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer,
	 *      int)
	 */
	public void update(GameContainer container, int delta) throws SlickException {
	}

	/**
	 * @see org.newdawn.slick.Game#render(org.newdawn.slick.GameContainer,
	 *      org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		String text = "abc";
		font.drawString(610, 100, text);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.5f);
		font.drawString(610, 150, text);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);

		g.translate(-50, -130);
		g.scale(10, 10);
		font.drawString(0, 0, text);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.5f);
		font.drawString(0, 26, text);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);

		g.resetTransform();
		g.setColor(Color.lightGray);
		g.drawString("Original Size on Sheet", 620, 210);
		g.drawString("10x Scale Up", 40, 575);

		g.setColor(Color.darkGray);
		g.drawRect(40, 40, 560, 530);
		g.drawRect(610, 105, 150, 100);

		g.setColor(Color.white);
		g.drawString("512x512 Font Sheet", 620, 300);
		g.drawString("NEHE Charset", 620, 320);
		g.drawString("4096x4096 (8x) Source Image", 620, 340);
		g.drawString("ScanSize = 20", 620, 360);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv
	 *            The arguments to pass into the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new DistanceFieldTest());
			container.setDisplayMode(800, 600, false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
