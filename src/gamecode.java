

public class gamecode {
	public static float makeVectorsX;
	public static float makeVectorsY;
	public static float makeVectorsZ;
	private static boolean pauseReleased, mReleased, fReleased, pReleased, cReleased, bReleased;
	private static float mouseSensitivity = 0.1f;
	public static void run(){
		render.ppwhatx += 1f * nbody.timescale;
		render.ppwhaty += 2f * nbody.timescale;
		camera.rotz -= input.mouseDeltaX*mouseSensitivity;
		camera.rotx += input.mouseDeltaY*mouseSensitivity;
		if(camera.rotx > 90) camera.rotx = 90;
		if(camera.rotx < -90) camera.rotx = -90;
		if(input.keyESC)System.exit(0);
		if(input.keyW){
			makeVectors(camera.rotz, camera.rotx);
			camera.posx += makeVectorsX*nbody.timescale;
			camera.posy += makeVectorsY*nbody.timescale;
			camera.posz -= makeVectorsZ*nbody.timescale;
		}
		if(input.keyA){
			makeVectors(camera.rotz + 90, camera.rotx);
			camera.posx += makeVectorsX*nbody.timescale;
			camera.posy += makeVectorsY*nbody.timescale;
			camera.posz -= makeVectorsZ*nbody.timescale;
			//camera.posy += 0.01;
			//camera.posy += Math.sin();
		}
		if(input.keyS){
			makeVectors(camera.rotz, camera.rotx);
			camera.posx -= makeVectorsX*nbody.timescale;
			camera.posy -= makeVectorsY*nbody.timescale;
			camera.posz += makeVectorsZ*nbody.timescale;

		}
		if(input.keyD){
			makeVectors(camera.rotz - 90, camera.rotx);
			camera.posx += makeVectorsX*nbody.timescale;
			camera.posy += makeVectorsY*nbody.timescale;
			camera.posz -= makeVectorsZ*nbody.timescale;
		}
		if(input.keySPACE){
			if(pauseReleased)nbody.paused = !nbody.paused;
			pauseReleased = false;

		} else pauseReleased = true;
		if(input.keyM){
			if(mReleased){
				input.toggleMouseSnap();
			}
			mReleased = false;
		} else mReleased = true;
		
		if(input.keyP){
			if(pReleased){
				render.PostProcessEnabled = !render.PostProcessEnabled;
			}
			pReleased = false;
		} else pReleased = true;
		
		if(input.keyC){
			if(cReleased){
				render.PostProcessCube = !render.PostProcessCube;
			}
			cReleased = false;
		} else cReleased = true;
		
		if(input.keyB){
			if(bReleased){
				render.PostProcessBloom = !render.PostProcessBloom;
			}
			bReleased = false;
		} else bReleased = true;
		
		if(input.keyF){
			if(fReleased){
				render.toggleFS();
				System.out.println("i dont need this shit");
			}
			fReleased = false;
		} else fReleased = true;

		//camera.posy += 0.1f;
	}
	/*
	 * makeVectors(float pan, float tilt);
	 * makes a vector of length 1 for each rotation
	 * Returns nothing, x y and z vectors are in public var makeVectorsX etc.
	 */
	public static void makeVectors(float pan, float tilt){
		double radtilt = Math.toRadians((float)tilt);
		double radpan = Math.toRadians((float)pan);
		double tX = 0 ,tY = 1,tZ = 0;
		double ttX, ttY, ttZ;
		//rotate around Z
		ttX = Math.cos(radpan)*tX - Math.sin(radpan)*tY;
		ttY = Math.sin(radpan)*tX - Math.cos(radpan)*tY;
		tX = ttX;
		tY = ttY;
		//rotate around X
		ttY = Math.cos(radtilt)*tY - Math.sin(radtilt)*tZ;
		ttZ = Math.sin(radtilt)*tY + Math.cos(radtilt)*tZ;
		tY = ttY;
		tZ = ttZ;
		makeVectorsX = (float)tX;
		makeVectorsY = (float)tY;
		makeVectorsZ = (float)tZ;
	}
}