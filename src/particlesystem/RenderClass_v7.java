// sdurant12
// 12/25/2012
// a pathfinding particle system, inspired by pathfinding.avi (youtube.com)

// TODO: use verlet integration for speed, and more elegant collision reaction

// Currently: (temp) dividing densityArray by 2 instead of clearing it to zero

// Currently: (temp) dividing densityArray by two when accessing gradient
// to avoid squares

package particlesystem;

import javax.swing.*;



import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class RenderClass_v7 extends JPanel implements MouseListener, MouseMotionListener, KeyListener{

	private static int TILE_SIZE;
	private static int PARTICLE_SIZE;
	private static int WIDTH;
	private static int HEIGHT;
	private static int TILE_COUNT_X;
	private static int TILE_COUNT_Y;

	private boolean[][] traversable;
	private float[][] distance;
	private float[][] yAccelGrid;
	private float[][] xAccelGrid;

	private boolean paused, quit = false;

	private BufferedImage particleImage;
	private int[] particleRaster;

	private int[][] densityArray;

	// draw on densityRaster using the array for the particles as reference (all ones for fire) (metablobabble texture for water)
	// based on the value in densityRaster transfer to particleRaster based off of the 
	// particle types color gradient array

	private Random r = new Random();

	ArrayList<Particle> particleAL = new ArrayList<Particle>();

	public RenderClass_v7(int width, int height) {

		TILE_SIZE = Integer.parseInt(SimpleGUI.tileTF.getText());
		PARTICLE_SIZE = Integer.parseInt(SimpleGUI.particlesizeTF.getText());

		WIDTH = width;
		HEIGHT = height;

		TILE_COUNT_X = 1 + (WIDTH / TILE_SIZE);
		TILE_COUNT_Y = 1 + (HEIGHT / TILE_SIZE);

		traversable = new boolean[TILE_COUNT_X][TILE_COUNT_Y];
		distance = new float[TILE_COUNT_X][TILE_COUNT_Y];

		particleImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		particleRaster = ((DataBufferInt) particleImage.getRaster().getDataBuffer()).getData();

		densityArray = new int[WIDTH][HEIGHT]; //controls displayed colors

		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);

		init();
	}

	public void init() {

		//Initialize Render World
		for(int xi = 0; xi < TILE_COUNT_X; xi++)
		{
			for(int yi = 0; yi < TILE_COUNT_Y; yi++)
			{
				traversable[xi][yi] = true; //sets all tiles to traversable
				
				//Start adding random non-traversable blocks
				
				if( (xi == TILE_COUNT_X/3 || xi == TILE_COUNT_X*2/3 || xi == TILE_COUNT_X/2 ) && yi != TILE_COUNT_Y/3 /*&& yi != TILE_COUNT_Y*2/3*/ )
				{
					traversable[xi][yi] = false;
				}
				if(r.nextFloat() < .1f && yi != TILE_COUNT_Y/3)
				{
					traversable[xi][yi] = false;
				}
				if(xi < 9 && yi < 5)
				{
					traversable[xi][yi] = true;
				}
				
				//End non-traversable blocks

			}
		}


		distance(TILE_COUNT_X/4,TILE_COUNT_Y/4);

		// spawn particles
		for(int i = 0; i < Integer.parseInt(SimpleGUI.particlenumberTF.getText()); i++){
			float x = 100 + .5f*r.nextFloat()*TILE_SIZE*TILE_COUNT_X;
			float y = 100 + .5f*r.nextFloat()*TILE_SIZE*TILE_COUNT_Y;

			if(traversable[(int)(x/TILE_SIZE)][(int)(y/TILE_SIZE)])
			{
				particleAL.add( new Particle( Particle.WATER_GRAD, x, y , r.nextFloat()*10, r.nextFloat()*10 ));
			}
		}

	}

	// calculates the distance from x,y to every point considered traversable[][]
	public void distance( int startX, int startY ){

		float[][] tempDistance = new float[TILE_COUNT_X][TILE_COUNT_Y];

		for(int xi = 0; xi < TILE_COUNT_X; xi++){
			for(int yi = 0; yi < TILE_COUNT_Y; yi++){
				tempDistance[xi][yi] = 0;
			}
		}

		ArrayList<Node> heap = new ArrayList<Node>();

		//add first node to it.
		if(traversable[startX][startY]){
			heap.add( new Node(startX,startY,0) );
			tempDistance[startX][startY] = .1f;
		}else{
			// startx and starty are outside of acceptable range
		}

		while( heap.size() > 0 ){

			Node n = heap.get(0);

			int x = n.getX();
			int y = n.getY();
			float d = n.getD();


			//Make an array containing the (x,y,d) and then go through it checking x,y

			float[] distGrid // a 3*8 array containing (dx,dy,d) for nodes
			= { 
					-1, 0, 1,
					0,-1, 1,
					0, 1, 1,
					1, 0, 1,
					-1,-1, 1.4f,
					-1, 1, 1.4f,
					1,-1, 1.4f,
					1, 1, 1.4f,};


			for( int i = 0; i < distGrid.length; i+= 3){

				int tempX = x + (int)distGrid[i+0];
				int tempY = y + (int)distGrid[i+1];
				float tempD = d + distGrid[i+2];

				// if traversable[][] && if in map && if new will be smaller than old
				if(tempX >= 0 && tempY >= 0 && tempX < TILE_COUNT_X && tempY < TILE_COUNT_Y && traversable[tempX][tempY] && (tempDistance[tempX][tempY] == 0 || tempDistance[tempX][tempY] > tempD)){
					heap.add( new Node( tempX, tempY, tempD ) );
					tempDistance[tempX][tempY] = tempD;
				}

			}


			heap.remove(0);
		}

		if(traversable[startX][startY]){
			distance = tempDistance;
		}

		acceleration();

	}

	public void acceleration(){

		float[][] tempxAccelGrid = new float[TILE_COUNT_X][TILE_COUNT_Y];
		float[][] tempyAccelGrid = new float[TILE_COUNT_X][TILE_COUNT_Y];

		for(int xi = 0; xi < TILE_COUNT_X; xi++){
			for(int yi = 0; yi < TILE_COUNT_Y; yi++){

				tempxAccelGrid[xi][yi] = 0;
				tempyAccelGrid[xi][yi] = 0;

				int leftTile = xi-1 > 0 ? xi-1 : xi;
				int rightTile = xi+1 < TILE_COUNT_X ? xi+1 : xi;
				int upTile = yi-1 > 0 ? yi-1: yi;
				int downTile = yi+1 < TILE_COUNT_Y ? yi+1 : yi;

				leftTile = traversable[leftTile][yi] ? leftTile : xi;
				rightTile = traversable[rightTile][yi] ? rightTile : xi;
				upTile = traversable[xi][upTile] ? upTile : yi;
				downTile = traversable[xi][downTile] ? downTile : yi;

				tempxAccelGrid[xi][yi] = distance[leftTile][yi] - distance[rightTile][yi] ;
				tempyAccelGrid[xi][yi] = distance[xi][upTile] - distance[xi][downTile] ;

			}
		}

		xAccelGrid = tempxAccelGrid;
		yAccelGrid = tempyAccelGrid;

	}

	public void tick() 
	{

		for(int xi = 0; xi < WIDTH; xi++){
			for(int yi = 0; yi < HEIGHT; yi++){

				densityArray[xi][yi] = 0;
				//densityArray[xi][yi] = densityArray[xi][yi]/2;

			}
		}

		for(int i = 0; i < particleAL.size(); i++){

			// get particle and information
			Particle p = particleAL.get(i);

			float xPos = p.getxPos();
			float yPos = p.getyPos();
			float xVel = p.getxVel();
			float yVel = p.getyVel();

			// if in simulated world (TILE) range
			if(xPos +xVel <= TILE_COUNT_X*TILE_SIZE && xPos+xVel >= 0 && yPos+yVel <= TILE_COUNT_Y*TILE_SIZE && yPos+yVel >= 0){

				xVel += xAccelGrid[(int)xPos/TILE_SIZE][(int)yPos/TILE_SIZE];
				yVel += yAccelGrid[(int)xPos/TILE_SIZE][(int)yPos/TILE_SIZE];

			}else /* if not in simulation any more */{
				particleAL.remove(i);
			}

			// if in viewing range
			if (xPos + xVel < WIDTH - PARTICLE_SIZE && xPos + xVel > PARTICLE_SIZE && yPos + yVel < HEIGHT - PARTICLE_SIZE && yPos + yVel > PARTICLE_SIZE) {

				// if no collision
				if (traversable[ (int) ((xPos + xVel) / TILE_SIZE)][(int) ((yPos + yVel) / TILE_SIZE)]) {

					xPos += xVel;
					yPos += yVel;

					xVel += .2 * (densityArray[(int) xPos][(int) yPos] - densityArray[(int) xPos + PARTICLE_SIZE][(int) yPos]);
					yVel += .2 * (densityArray[(int) xPos][(int) yPos] - densityArray[(int) xPos][(int) yPos + PARTICLE_SIZE]);

					xVel = .9f * xVel;
					yVel = .9f * yVel;

				} else /* if collision */{

					float Vel = .5f * sqrt(xVel * xVel + yVel * yVel);

					// if x making it collide
					if (!traversable[ (int) ((xPos + xVel) / TILE_SIZE)][(int) (yPos / TILE_SIZE)]) {

						yVel = yVel > 0 ? Vel : -Vel;
						xVel = -.5f * xVel;

					}else /* if x not making it collide */{
						xPos += xVel;
					}

					// if y making it collide
					if (!traversable[ (int) (xPos / TILE_SIZE)][(int) ((yPos + yVel) / TILE_SIZE)]) {

						xVel = xVel > 0 ? Vel : -Vel;
						yVel = -.5f * yVel;

					}else /* if y not making it collide */{
						yPos += yVel;
					}

				}
			} else /* if not in viewing range */ {

				xPos += xVel;
				yPos += yVel;

			}
			p.setParticle( xPos, yPos, xVel, yVel );

			// I still simulate if in TILE (simulated) area, but can't draw unless in WIDTH (viewable)
			if(xPos < WIDTH - PARTICLE_SIZE && xPos > PARTICLE_SIZE && yPos < HEIGHT - PARTICLE_SIZE && yPos > PARTICLE_SIZE){ // draw on densityArray

				for(int xi = 0; xi < PARTICLE_SIZE; xi++){
					for(int yi = 0; yi < PARTICLE_SIZE; yi++){
						densityArray[ (int)(xPos+xi)][ (int)(yPos+yi)] += 1;
					}
				}

			}

		}

		//repaint();
	}

	public float sqrt( float x ){
		return x * Float.intBitsToFloat(0x5f3759d5 - (Float.floatToIntBits(x) >> 1));
	}


	public void paint(Graphics g) 
	
	{
		//CLEAR
		tick();

		//SET BACKGROUND TILE COLOR
		for(int xi = 0; xi < TILE_COUNT_X; xi++)
		{
			for(int yi = 0; yi < TILE_COUNT_Y; yi++)
			{
				if(traversable[xi][yi]){

					//Intensity of blue depends on distance of tile from mouse

					//int blue = ((int)(distance[xi][yi]*6) % 512) < 256 ? (int)(distance[xi][yi]*6) % 256 : 255 - ((int)(distance[xi][yi]*6) % 256);
					int blue = 0;

					// gradient distance
					g.setColor( new Color( 0, 0, blue ) );

					// nice gradient
					//g.setColor(new Color(   255*xi/TILE_COUNT_X, 255*yi/TILE_COUNT_Y, 255  ));

				}else{
					g.setColor( new Color ( 0, 200, 0 ) );
				}
				g.fillRect( xi*TILE_SIZE, yi*TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}



		//SET GRADIENT
		//hex colors are alpha, red, green, blue
		int background_color = 0xff000000;
		
		int[] particle_colors = Particle.FIRE_GRAD;
		
		ArrayList<Integer> gradient_list = new ArrayList<Integer>();
		
		gradient_list.add(background_color);
		
		for (int i:particle_colors)
			gradient_list.add(i);
				
				
		// iterate through densityArray and put into particleRaster
		for(int xi = 0; xi < WIDTH; xi++)
		{
			for(int yi = 0; yi < HEIGHT; yi++)
			{
				if (densityArray[xi][yi] < gradient_list.size())
				{
					particleRaster[xi + yi*WIDTH] = gradient_list.get(densityArray[xi][yi]);
				}
				else
				{
					particleRaster[xi + yi*WIDTH] = gradient_list.get(gradient_list.size()-1); //sets center of particle color to last value in gradient array
				}
			}
		}

		g.drawImage(particleImage, 0, 0, WIDTH, HEIGHT, null);
		
		g.setColor(Color.WHITE);    
		g.drawString( "particles: " + particleAL.size(), 10, 10 );

	}

	@Override
	public void mouseClicked(MouseEvent me) {
		distance( me.getX()/TILE_SIZE, me.getY()/TILE_SIZE );
	}

	public boolean getPaused(){
		return paused;
	}

	public boolean getQuit() {
		return quit;
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// NEED TO GET FOCUS HERE SO THAT KEYBOARD EVENTS WORK
		requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mouseDragged(MouseEvent me) {
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		distance( me.getX()/TILE_SIZE, me.getY()/TILE_SIZE );
	}

	@Override
	public void keyTyped(KeyEvent ke) {

		switch (ke.getKeyChar())
		{
		case KeyEvent.VK_SPACE:
			paused = !paused;
			break;

		case KeyEvent.VK_ESCAPE:
			quit = !quit;
			break;

		}

		//		if( ke.getKeyChar() == KeyEvent.VK_ESCAPE ){
		//			paused = !paused;
		//		}

		//		if (ke.getKeyChar() ==  KeyEvent.VK_END){ //Want to make an easy quit key
		//			
		//			System.out.println("End Registered");
		//			quit = !quit;
		//		}

	}

	@Override
	public void keyPressed(KeyEvent ke) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

}