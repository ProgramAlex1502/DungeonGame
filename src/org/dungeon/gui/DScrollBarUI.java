package org.dungeon.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

class DScrollBarUI extends BasicScrollBarUI {
	
	private static final Dimension zeroDimension = new Dimension(0, 0);
	
	private static final int TRACK_W = 4;
	private static final int THUMB_W = TRACK_W * 2;

	private int calculateX(int x, int areaWidth, int barWidth) {
		return x + (areaWidth - barWidth) / 2;
	}
	
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		if (trackBounds.isEmpty() || !c.isEnabled()) {
			return;
		}
		
		g.setColor(SharedConstants.INSIDE_COLOR);
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
		g.setColor(SharedConstants.SCROLL_COLOR);
		int y = trackBounds.y + TRACK_W;
		int height = trackBounds.height - TRACK_W * 2;
		g.fillRect(calculateX(trackBounds.x, trackBounds.width, TRACK_W), y, TRACK_W, height);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !c.isEnabled()) {
			return;
		}
		
		g.setColor(SharedConstants.MARGIN_COLOR);
		g.fillRect(calculateX(thumbBounds.x, thumbBounds.width, THUMB_W), thumbBounds.y, THUMB_W, thumbBounds.height);
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createZeroButton();
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createZeroButton();
	}
	
	private static JButton createZeroButton() {
		JButton jButton = new JButton();
		jButton.setPreferredSize(zeroDimension);
		jButton.setMinimumSize(zeroDimension);
		jButton.setMaximumSize(zeroDimension);
		return jButton;
	}

}
