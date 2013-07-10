public class waterphysics extends nbody {
	private float posx,posy,posz;
	private float velx,vely,velz;
	private float pressurex, pressurey, pressurez;
	private float pressure2x, pressure2y, pressure2z;
	private float pressure;
	
		waterphysics(){
			//spawnParticleGalaxy();
			spawnParticleRandom();
		}
		private void spawnParticleRandom(){
			posx = (float) (Math.random()/2);
			posy = (float) (Math.random()/2);
			posz = (float) (Math.random()/2);
			velx = (float) ((Math.random()-0.5)/500);
			vely = (float) ((Math.random()-0.5)/500);
			velz = (float) ((Math.random()-0.5)/500);
		}
		public float getPosX(){return posx;}
		public float getPosY(){return posy;}
		public float getPosZ(){return posz;}
		public float getVelX(){return velx;}
		public float getVelY(){return vely;}
		public float getVelZ(){return velz;}
		public void setPressure(){
			pressurex = pressure2x;
			pressurey = pressure2y;
			pressurez = pressure2z;
			pressure = pressurex + pressurey + pressurez;
			if(pressure<1)pressure=1;
		}
		public void updatePressure(){
			if(posx > 1) pressure2x = -10;
			if(posx < 0) pressure2x =  10;
			if(posy > 1) pressure2y = -10;
			if(posy < 0) pressure2y =  10;
			if(posz > 1) pressure2z = -10;
			if(posz < 0) pressure2z =  10;
			//pressure
			for(int i=0; i< waterparticulate.size(); i++){
				if(waterparticulate.get(i) == this) continue;
				float deltax, deltay, deltaz, distance;
				deltax = posx - waterparticulate.get(i).posx;
				if(deltax > 0.01)continue;
				deltay = posy - waterparticulate.get(i).posy;
				if(deltay > 0.01)continue;
				deltaz = posz - waterparticulate.get(i).posz;
				if(deltaz > 0.01)continue;
				distance =  (float) Math.sqrt(Math.pow(deltax, 2)+Math.pow(deltay, 2)+Math.pow(deltaz, 2));
				if(distance > 0.01)continue;
				pressure2x = deltax * waterparticulate.get(i).pressure/(distance*distance);
				pressure2y = deltay * waterparticulate.get(i).pressure/(distance*distance);
				pressure2z = deltaz * waterparticulate.get(i).pressure/(distance*distance);
				//acceleration =(waterparticulate.get(i).getMass() / distance)/mass;
				//acceleration /= 10000000f;
				
				
			}
		}
		public void updatePositions(float timescale){
			posx += velx*timescale;
			posy += vely*timescale;
			posz += velz*timescale;
		}
		public void updateVelocities(float timescale){
			velx+=pressurex * timescale;
			vely+=pressurey * timescale;
			velz+=pressurez * timescale;
			velz-= 1 * timescale;
		}
	}
