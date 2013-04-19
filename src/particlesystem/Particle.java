
package particlesystem;

public class Particle {

	private float xPos, yPos, xVel, yVel;
	private int size;
    private float pxVel, pyVel;
    private int age = 0;
	private int[] gradient;
	
	//seems to be {screen_background, particle trail, particle trail, ... , particle center }

	public final static int[] FIRE_GRAD = { 0xaf9f1604, 0xffdf3509, 0xffef6a10, 0xfffc9b11, 0xffffaa22, 0xffffbb33, 0xffffdd66, 0xffffffaa, 0xffffffff};
	public final static int[] SMOKE_GRAD = { 0x8f666666, 0x8f888888, 0x8f888888, 0x8f666666};
	public final static int[] WATER_GRAD = { 0xffffffff, 0xffffffff, 0xffffffff, 0x8f1144dd};
	public final static int[] SAND_GRAD = { 0x8f666666, 0x8f888888, 0x8f888888, 0xfffc9b11};
	public final static int[] EXP_GRAD = { 0xff0000ff, 0xffdf3509, 0xffef6a10, 0xfffc9b11, 0xffffaa22, 0xffffbb33, 0xffffdd66, 0xffffffaa, 0xffffffff};
	
	//0xff0000ff
	
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
		this.size = size;
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
	
    public float getpxVel(){
        return pxVel;
      }
      public float getpyVel(){
        return pyVel;
      }
      public int getAge(){
        return age;
      }

	public int[] getGradient(){
		return gradient;
	}
	
    public void setxPos(float xp){
        this.xPos = xp;
      }
      public void setyPos(float yp){
        this.yPos = yp;
      }
      public void setxVel(float xv){
        this.xVel = xv;
      }
      public void setyVel(float yv){
        this.yVel = yv;
      }
      public void setpxVel(float pxv){
        this.pxVel = pxv;
      }
      public void setpyVel(float pyv){
        this.pyVel = pyv;
      }
      public void setAge(int a){
        this.age = a;
      }
	

}

