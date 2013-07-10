import java.util.Arrays;


public class voxelize {
	public static float voxelSize [][][] = new float[256][256][256];;
	public static void voxelize(){
		
		//Arrays.fill(voxelCount, 0);
	//	Arrays.fill(voxelColor, 0);
		voxelSize = new float[256][256][256];
		//Arrays.fill(voxelSize, 0);
		for(int i=0; i< nbody.numberofparticles; i++){
			//particulate.get(i).updatePositions(timescale);
			if(ParticleArray.posx[i]>1 || ParticleArray.posx[i]<0)continue;
			if(ParticleArray.posy[i]>1 || ParticleArray.posy[i]<0)continue;
			if(ParticleArray.posz[i]>1 || ParticleArray.posz[i]<0)continue;
			voxelSize	[(char) (ParticleArray.posx[i]*256)]
						[(char) (ParticleArray.posy[i]*256)]
						[(char) (ParticleArray.posz[i]*256)] += (ParticleArray.mass[i]);
			/*voxelCount	[(int) ParticleArray.posx[i]*256]
						[(int) ParticleArray.posy[i]*256]
						[(int) ParticleArray.posz[i]*256] += 1;*/
			
		}
	}

}
