
public class ParticleArray {
	public static float[] posx;
	public static float[] posy;
	public static float[] posz;
	public static float[] velx;
	public static float[] vely;
	public static float[] velz;
	public static float[] colr;
	public static float[] colb;
	public static float[] colg;
	public static float[] mass;
	public static int[] suns = new int[2];
	public static void spawnSun(int i){
		suns[0] = i;
		posx[i] = 0f;
		posy[i] = 0f;
		posz[i] = 0f;
		velx[i] = 0f;
		vely[i] = 0f;
		velz[i] = 0f;
		mass[i] = 100f;
		
	}
	public static void spawnSun2(int i){
		suns[1] = i;
		posx[i] = 21f;
		posy[i] = 20f;
		posz[i] = 2f;
		velx[i] = -0.01f;
		vely[i] = -0.01f;
		velz[i] = 0f;
		mass[i] = 200f;
		
	}
	public static void spawnGalaxy(int n){
		
		for(int i=0;i < n; i++){
			velx[i] = 0.0f;
			vely[i] = 0.0f;
			velx[i] += (float) -(posy[i]/50);
			vely[i] += (float) (posx[i]/50);
			velz[i] = 0.0f;
			posz[i] = 0.0f;
		}
	}
	public static void spawnGalaxy2(int n){
		
		for(int i=0;i < n; i++){
			velx[i] = -0.01f;
			vely[i] = -0.01f;
			velx[i] += (float) -(posy[i]/30);
			vely[i] += (float) (posx[i]/30);
			velz[i] = 0.0f;
			posz[i] = 0.0f;
			posx[i] += 21;
			posy[i] += 20;
			posz[i] += 2;
		}
	}
	public static void spawnParticleRandom(int n){
		posx = new float[n];
		posy = new float[n];
		posz = new float[n];
		velx = new float[n];
		vely = new float[n];
		velz = new float[n];
		colr = new float[n];
		colb = new float[n];
		colg = new float[n];
		mass = new float[n];
		for(int i=0;i < n; i++){
			posx[i] = (float) (Math.random()-0.5);
			posy[i] = (float) (Math.random()-0.5);
			posz[i] = (float) (Math.random()-0.5);
			velx[i] = (float) ((Math.random()-0.5)/500);
			vely[i] = (float) ((Math.random()-0.5)/500);
			velz[i] = (float) ((Math.random()-0.5)/500);
			colr[i] = (float) (Math.random());
			colg[i] = (float) (Math.random());
			colb[i] = (float) (Math.random());
			mass[i] = (float) (Math.random());
		}
	}
	public static void getnBodyVelocityChange(float timescale,int p){
		float acceleration, deltax, deltay, deltaz, distance;
		for(int i=0; i< nbody.numberofparticles; i++){
			if(i == p) continue;//dont do crackulations against self
			deltax = posx[p] - posx[i];
			deltay = posy[p] - posy[i];
			deltaz = posz[p] - posz[i];
			//distance =  (float) Math.sqrt(Math.pow(deltax, 2)+Math.pow(deltay, 2)+Math.pow(deltaz, 2)); // shits SLOW 
			distance =  (float) Math.sqrt((deltax * deltax)+(deltay*deltay)+(deltaz*deltaz));
			acceleration = (mass[i] / (distance))/10000000;
			velx[p] -= (deltax/distance) * acceleration;
			vely[p] -= (deltay/distance) * acceleration;
			velz[p] -= (deltaz/distance) * acceleration;
		}
	}
	
	public static void updatePositions(float timescale, int p){
		posx[p] += velx[p]*timescale;
		posy[p] += vely[p]*timescale;
		posz[p] += velz[p]*timescale;
	}

}
