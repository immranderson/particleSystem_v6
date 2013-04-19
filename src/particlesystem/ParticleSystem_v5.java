// sdurant12
// 11/14/2012

package particlesystem;

import java.awt.Canvas;
import javax.swing.*;




public class ParticleSystem_v5 extends Canvas {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int TICK = 16;

	public boolean paused, quit = false;

	{

		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setUndecorated(false);
		frame.setResizable(false);
		frame.setFocusable(true);

		final RenderClass_v5 ren5 = new RenderClass_v5(WIDTH, HEIGHT);
		frame.add(ren5);

		frame.setVisible(true);

		Thread runThread = new Thread(new Runnable() {
			public void run() {
				if (true) {
					while (true) {
						long time = System.currentTimeMillis();
						ren5.tick();
						ren5.repaint();
						long endtime = System.currentTimeMillis();
						try {
							Thread.sleep( TICK - (endtime - time));
						} catch (Exception e) {
							//System.out.println("Exception e at Thread.sleep");
						}
					}
				}
			}
		});

		runThread.start();
	}
}