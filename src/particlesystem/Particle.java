
package particlesystem;

public class Particle {

	private float xPos, yPos, xVel, yVel;
	private int[] gradient;

	public final static int[] FIRE_GRAD = { 0x00000000,  0xaf9f1604, 0xffdf3509, 0xffef6a10, 0xfffc9b11, 0xffffaa22, 0xffffbb33, 0xffffdd66, 0xffffffaa, 0xffffffff};
	public final static int[] SMOKE_GRAD = { 0x00000000, 0x8f666666, 0x8f888888, 0x8f888888, 0x8f666666};
	public final static int[] WATER_GRAD = { 0x00000000, 0xffffffff, 0xffffffff, 0xffffffff, 0x8f1144dd};
	public final static int[] SAND_GRAD = { 0x00000000, 0x8f666666, 0x8f888888, 0x8f888888, 0xfffc9b11};
	
	/*I believe initializing the gradient within the particle class will more easily allow us to use
	 * more than one color of particle
	 */
	

	public Particle(float xPos, float yPos, float xVel, float yVel)
	{   
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;    
	}

	public Particle(int[] gradient, float xPos, float yPos, float xVel, float yVel)
	{
		this.gradient = gradient;
		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;    
	}

	public Particle()
	{
		this.xPos = 0;
		this.yPos = 0;
		this.xVel = 0;
		this.yVel = 0;
	}

	public void setParticle(  float xPos, float yPos, float xVel, float yVel ){

		this.xPos = xPos;
		this.yPos = yPos;
		this.xVel = xVel;
		this.yVel = yVel;

	}

	public float getxPos(){
		return xPos;
	}

	public float getyPos(){
		return yPos;
	}

	public float getxVel(){
		return xVel;
	}

	public float getyVel(){
		return yVel;
	}

	public int[] getGradient(){
		return gradient;
	}
}

