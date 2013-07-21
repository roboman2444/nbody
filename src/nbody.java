import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class nbody{
    static {
        String lwjgl_folder = "libs" + File.separator;
        final String OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if (OS.contains("win"))
            lwjgl_folder += "win";
        else if (OS.contains("mac"))
            lwjgl_folder += "osx";
        else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"))
            lwjgl_folder += "lin";
        else if (OS.contains("sunos"))
            lwjgl_folder += "sol";
        System.setProperty("org.lwjgl.librarypath", new File(lwjgl_folder).getAbsolutePath());
    }
    
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
		input.initInput();
		long lasttime = System.currentTimeMillis();

		//cache all parameters so we only have to do the math once
		final ArrayList<Integer> cache_parameter1 = new ArrayList<Integer>();
		final ArrayList<Integer> cache_parameter2 = new ArrayList<Integer>();
		for (int i = 0; i < threads; i++) {
		    cache_parameter1.add((numberofparticles/threads) * i);
		    cache_parameter2.add((numberofparticles/threads) * (i+1));
		}
		
		while(true){
			long time = System.currentTimeMillis();
			timescale = (time-lasttime)/100.0f;
			if(!paused){
			    Parallel.parallelFor(cache_parameter1, new Parallel.ParallelRunner<Integer>() {

                    @Override
                    public void run(Integer object, int index) {
                        int start = object.intValue();
                        int end = cache_parameter2.get(index);
                        
                        for(int i=start; i< end; i++){
                            ParticleArray.getnBodyVelocityChange(nbody.timescale, i);
                        }
                    }
			        
			    }, threads);
			    
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
