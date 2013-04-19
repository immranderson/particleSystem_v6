package particlesystem;

import java.awt.Canvas;
import javax.swing.*;

// TODO: make resizeable when paused (have getPaused method in renderClass
// and if true setUndecorated(false);, and vice versa

public class ParticleSystem_v7 extends Canvas {

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	//public static final int TICK = 16;

	public boolean paused, quit = false;

	{

		final JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setUndecorated(false);
		frame.setResizable(false);
		frame.setFocusable(true);

		final RenderClass_v7 ren7 = new RenderClass_v7(WIDTH, HEIGHT);
		frame.add(ren7);

		frame.setVisible(true);

		Thread runThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					long time = System.currentTimeMillis();

					paused = ren7.getPaused();

					if( !paused ){
						//ren.tick();
						//frame.setOpacity(1f);
						ren7.repaint();
					} else {
						//frame.setOpacity(.5f);
					}


					quit = ren7.getQuit();

					if( quit ){
						frame.dispose();
					}


					long endtime = System.currentTimeMillis();
					try {
						Thread.sleep(time - endtime);
					} catch (Exception e) {
						// System.out.println("cannot sleep");
					}
				}
			}
		});

		runThread.start();

	}
}
