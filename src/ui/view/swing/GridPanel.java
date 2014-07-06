package ui.view.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GridPanel extends JPanel {

	private final MineButton[][] buttons;
	
	private BufferedImage flagImage;
	private BufferedImage mineImage;
	private BufferedImage crossImage;

	public GridPanel(int rows, int columns) {
		super();
		
		setLayout(new GridLayout(rows, columns));
		setBackground(new Color(190, 190, 190));
		
		try {
			flagImage = ImageIO.read(getClass().getResource(
					"images/RedFlag.png"));
			mineImage = ImageIO.read(getClass().getResource("images/Mine.png"));
			crossImage = ImageIO.read(getClass().getResource(
					"images/Icon_cross.png"));

		} catch (IOException e) {
			System.err.println("Could not load image");
			// bad luck, no images will be shown.
		}

		buttons = new MineButton[rows][columns];
		addMineButtons();
	}


	private void addMineButtons() {
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[i].length; j++){
				MineButton button = new MineButton(i, j);
				button.setBackground(new Color(220, 220, 220));
				buttons[i][j] = button;
				add(button);
			}
		}
		
	}
	
	public void addListenersToButtons(ActionListener al, MouseListener ml){
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons[i].length; j++){
				buttons[i][j].addListeners(al, ml);
			}
		}
	}
	
	public void removeListenersFromButtons() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].removeListeners();
			}
		}

	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		int width = buttons[0][0].getWidth();
		int height = buttons[0][0].getHeight();
		int iconWidth = width;
		int iconHeight = height;

		if (iconWidth > iconHeight)
			iconWidth = iconHeight;
		else if (iconHeight > iconWidth)
			iconHeight = iconWidth;

		ImageIcon ii = new ImageIcon(flagImage.getScaledInstance(iconWidth,
				iconHeight, Image.SCALE_SMOOTH));

		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {

				int x = buttons[i][j].getX();
				int y = buttons[i][j].getY();

				if (!buttons[i][j].isVisible()) {

					g2.setColor(new Color(140, 140, 140));
					g2.drawRect(x, y, width - 1, height - 1);

					int minesAround = buttons[i][j].getNumOfMinesAround();
					if (!buttons[i][j].isMined() && !buttons[i][j].isFlagged()
							&& minesAround > 0) {

						String numMinesAround = String.valueOf(minesAround);

						g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15
								+ (int) (0.2 * width) + (int) (0.2 * height)));
						FontMetrics fm = g2.getFontMetrics();

						Point centerPos = getTextCenterPos(x, y, width, height,
								fm, numMinesAround);

						Color c = getNumColor(minesAround);
						g2.setColor(c);
						g2.drawString(numMinesAround, centerPos.x, centerPos.y);

					} else if (buttons[i][j].isMined()
							&& !buttons[i][j].isFlagged()) {

						if (buttons[i][j].isDestroyed()) {
							g2.setColor(Color.RED);
							g2.fillRect(x, y, width, height);
						}

						g2.drawImage(mineImage, x + ((width - iconWidth) / 2),
								y, iconWidth, iconHeight, null);
					} else if (buttons[i][j].isWrong()) {
						g2.drawImage(mineImage, x + ((width - iconWidth) / 2),
								y, iconWidth, iconHeight, null);
						g2.drawImage(crossImage, x, y, width, height, null);
					}
				} else if (buttons[i][j].isFlagged()) {

					buttons[i][j].setIcon(ii);
				} else if (!buttons[i][j].isFlagged()) {
					buttons[i][j].setIcon(null);
				}

			}
		}
	}

	public void removeButton(int x, int y) {
		buttons[x][y].setVisible(false);
	}

	public void displayNumber(int x, int y, int numMinesAround) {
		buttons[x][y].setNumOfMinesAround(numMinesAround);

	}

	public void toggleFlag(int x, int y) {
		buttons[x][y].toggleFlag();
		repaint();
	}

	public void setMine(int x, int y) {

		buttons[x][y].setMine();
	}

	public boolean isFlagged(int x, int y) {
		return buttons[x][y].isFlagged();
	}

	public void destroy(int x, int y) {
		buttons[x][y].destroy();

	}

	public boolean isMined(int x, int y) {
		return buttons[x][y].isMined();
	}

	public void setWrong(int x, int y) {
		buttons[x][y].setWrong();

	}
	
	private static Point getTextCenterPos(int x, int y, int width, int height,
			FontMetrics fm, String numMinesAround) {

		int textWidth = fm.stringWidth(numMinesAround);
		int textHeight = fm.getHeight();

		x += (width - textWidth) / 2;
		y += ((height - textHeight) / 2) + fm.getAscent();

		return new Point(x, y);

	}

	private static Color getNumColor(int n) {
		Color c;

		switch (n) {

		case 1:
			c = new Color(1602765);
			break;
		case 2:
			c = new Color(2330147);
			break;
		case 3:
			c = new Color(16711680);
			break;
		case 4:
			c = new Color(0, 0, 156);
			break;
		case 5:
			c = new Color(139, 35, 35);
			break;
		case 6:
			c = new Color(66, 111, 66);
			break;
		case 7:
			c = new Color(79, 79, 47);
			break;
		default:
			c = Color.BLACK;
		}

		return c;
	}

}
