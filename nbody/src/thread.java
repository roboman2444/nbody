public class thread extends Thread{
	public int start;
	public int end;
	thread(int start, int end){
		this.start= start;
		this.end= end;
	}
	@Override
	public void run() {
		velocitychange();
	}
	public void velocitychange() {
		for(int i=start; i< end; i++){
			//nbody.particulate.get(i).getnBodyVelocityChange(nbody.timescale);
			ParticleArray.getnBodyVelocityChange(nbody.timescale, i);
		}
	}
	public void poschange(){
		for(int i=start; i< end; i++){
			//nbody.particulate.get(i).updatePositions(nbody.timescale);
		}
	}
}
