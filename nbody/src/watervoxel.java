
public class watervoxel {
	private static char[][][] pressure;
	private static char[][][] density;
	private char[][][] velocityx;
	private char[][][] velocityy;
	private char[][][] velocityz;
	private static char[][][] pressure2;
	private static char[][][] density2;
	private char[][][] velocity2x;
	private char[][][] velocity2y;
	private char[][][] velocity2z;
	watervoxel(){
		for(int x=0; x < 256; x++){
			for(int y=0; y < 256; y++){
				for(int z=0; z < 256; z++){
					pressure[x][y][z] = (char) (Math.random()*256);
				}
			}
		}
		
	}
	public static void waterphysics(){
		for(int x=0; x < 256; x++){
			for(int y=0; y < 256; y++){
				for(int z=0; z < 256; z++){
					checkparticle(x,y,z);
					pressure = pressure2;
				}
			}
		}
	}
	private static void checkparticle(int x, int y, int z){
		pressure2[x][y][z] = 0;
		for(int xp=-1; xp<2; xp++){
			for(int yp=-1; yp<2; yp++){
				for(int zp=-1; zp<2; zp++){
					pressure2[x][y][z] += pressure[xp][yp][zp];
				}
			}
		}
	}
}
