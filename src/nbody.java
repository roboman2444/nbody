import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;


public class nbody{
	public static final boolean LINES = false;
	public static final int threads = 8;
	public static int numberofparticles;
	public static final boolean docoolshit = false;
	public static final boolean ortho = false;
	public static final boolean snapMouse = true;
	public static float timescale = 0.1f;
	public static float fpsTotalTime = 0;
	public static int fpsCounter = 0;
	public static boolean paused;
	public static void doMath(float timescale){
		for(int i=0; i< numberofparticles; i++){
			//particulate.get(i).updatePositions(timescale);
			ParticleArray.updatePositions(timescale, i);

		}
	}
	private static void startParticles(int number){
		numberofparticles = number;
		ParticleArray.spawnParticleRandom(number);
		ParticleArray.spawnGalaxy(number);
		ParticleArray.spawnGalaxy2(number/2);
		ParticleArray.spawnSun2(1001);
		ParticleArray.spawnSun(1);
	}
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File("libs/lin").getAbsolutePath());
		startParticles(Integer.parseInt(args[0]));
		render.init();
		/*try {
			openclsolver.doSumExample();
		} catch (LWJGLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		long lasttime = System.currentTimeMillis();
		thread[] t = new thread[threads];
		while(true){
			long time = System.currentTimeMillis();
			timescale = (time-lasttime)/100.0f;
			if(!paused){
				for (int i =0; i< threads; i++) {
					t[i] = new thread((numberofparticles/threads) * i,(numberofparticles/threads) * (i+1));
					t[i].start();
				}
				for (thread th : t) {
					try {
						th.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				doMath((float)timescale);
			}
			render.draw();
			input.getInput();
			gamecode.run();
			fpsTotalTime += timescale;
			fpsCounter ++;
			if(fpsCounter == 100){
				fpsCounter = 0;
				System.out.println("fps: "+ 1000/fpsTotalTime);
				fpsTotalTime = 0;
			}
			lasttime = time;
		}
	}
}
