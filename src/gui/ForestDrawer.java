
package gui; 

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import forest.Field;
import forest.Forest;
import forest.Logger;
import forest.Pair;
import forest.Tree;

public class ForestDrawer extends Applet {
	
	private static final long serialVersionUID = 4691202102159528000L;
	
	private final Forest FOREST;
	private final Rectangle2D.Double[][] RECTANGLES;
	private final int MAX_AGE;
	
	private static final Color BROWN         = new Color(140, 70, 20);
	private static final Color ORANGE        = new Color(255, 120, 0);
	private static final Color SAPLING_GREEN = new Color(0, 222, 0);
	private static final Color ADULT_GREEN   = new Color(0, 180, 0);
	private static final Color ELDER_GREEN   = new Color(0, 100, 0);
	private static final Color WHITE         = new Color(255, 255, 255);
	
	private final double RECT_WIDTH;
	private final double RECT_HEIGHT;
	
	private final Dimension DIMENSION;
	
	private final String[] TEXT = {"", "", "", "", "", ""};
	
    ForestDrawer(Forest forest, int age) {
    	int gridSize = forest.getSize();
    	Dimension res = getToolkit().getScreenSize();
    	int max = (int) Math.max(res.getWidth(), res.getHeight());
    	// extra 50 % width are for the log
    	this.DIMENSION = new Dimension((int) ((max / 2) * 1.5), max / 2 + 10);
    	RECT_HEIGHT = (max / 2.0) / gridSize;
    	RECT_WIDTH = (max / 2.0) / gridSize;
    	this.FOREST = forest;
    	this.MAX_AGE = age;
    	this.RECTANGLES = new Rectangle2D.Double[gridSize][gridSize];
    }
    
    public Dimension getSize() {
    	return DIMENSION;
    }
    
    public void init() {
        setBackground(WHITE);
        setupSimulation();
    }
    
    private void setupSimulation() {
    	for (int i = 0; i < FOREST.getSize(); i++) {
    		for (int j = 0; j < FOREST.getSize(); j++) {
    			Pair coord = getCoordinates(i, j);
    			Rectangle2D.Double rect = new Rectangle2D.Double(coord.x, coord.y, RECT_WIDTH, RECT_HEIGHT);
    			RECTANGLES[i][j] = rect;
    		}
    	}	
    }
 
    public void paint(Graphics g) {
    	/*
    	 * Only used for repainting the log as it is too slot in
    	 * coloring the forest, at least on my laptop.
    	 */
        Graphics2D g2 = (Graphics2D) g;
    	Font font = new Font ("Tahoma", Font.BOLD, 18);
		g2.setFont(font);
		g2.setColor(Color.black);
    	int size = FOREST.getSize();
		int x = (int) RECT_WIDTH * size + 85;
    	int y = 25;
    	
    	Logger logger = FOREST.getLogger();
		TEXT[0] = "Month " + FOREST.getTime() + ":";
		TEXT[1] = "Trees in the forest: " + (logger.saplings + logger.adults + logger.elders);
		TEXT[2] = "Lumberjacks in the forest: " + logger.lumberjacks;
		TEXT[3] = "Bears in the forest: " + logger.bears;
		TEXT[4] = "Woord collected this year: " + logger.yearlyWood;
		TEXT[5] = "Lumberjacks eaten this year: " + logger.yearlyMawings;
    	for (String text : TEXT) {
			g2.drawString(text, x, y);
			y += 25;
		}
    }
    
    private void redraw() {
    	/*
    	 * Unlike repaint this one doesn't the not yet drawn part of the
    	 * forest to disappear.
    	 */
    	Graphics2D g2 = (Graphics2D) getGraphics();
    	for (int i = 0; i < FOREST.getSize(); i++) {
        	for (int j = 0; j < FOREST.getSize(); j++) {
        		Field field = FOREST.getFields()[i][j];
        		Rectangle2D.Double rect = RECTANGLES[i][j];
        		g2.setColor(getColor(field));
        		g2.fill(rect);
        		g2.draw(rect); 
        	}
        }
    }
    
    public void runSim() {
    	try {
	    	for (int i = 0; i < MAX_AGE; i++) {
	    		FOREST.tick();
	    		redraw();
	    		
	    		//Only the log:
	    		Dimension size = getSize();
	    		int x = (int) (size.width / 1.5) + 10;
	    		int y = 0;
	    		int width = size.width - x;
	    		int height = size.height;
	    		repaint(x, y, width, height);
	    		
				Thread.sleep(1000);
	    	}
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    private Pair getCoordinates(int i, int j) {
    	return new Pair((int) (i * RECT_WIDTH), (int) (j * RECT_HEIGHT));
    }
    
    private Color getColor(Field field) {
    	if (field.bearPresent()) {
    		return BROWN;
    	} else if (field.lumberJackPresent()) {
    		return ORANGE;
    	} else if (field.treePresent()) {
    		Tree tree = field.getTree().get();
    		if (tree.getAge() < 12) {
    			return SAPLING_GREEN;
    		} else if (tree.getAge() < 120) {
    			return ADULT_GREEN;
    		} else {
    			return ELDER_GREEN;
    		}
    	} else {
    		return WHITE;
    	}
    }
 
    public static void main(String s[]) {
        int gridSize = 100;
        int maxAge = 4000;
        
        Forest forest = new Forest(gridSize);
        ForestDrawer drawer = new ForestDrawer(forest, maxAge);
        drawer.init();
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add("Center", drawer);
        f.pack();
        f.setSize(drawer.getSize());       
        f.setVisible(true);
        
        drawer.runSim();
    }
 
}