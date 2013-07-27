import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class render extends nbody {
	public static boolean PostProcessEnabled = false;
	public static boolean PostProcessCube = false;
	public static boolean CubeProcessEnabled = false;
	public static boolean PostProcessBloom = false;
	public static boolean PostProcessFlare = true;
	public static int PostProcessBloomBlurPasses = 2;
	public static int sizeX = 800;
	public static int sizeY = 600;
	public static float ppwhatx = 0;
	public static float ppwhaty = 0;
	public static void toggleFS() {
		try {
			Display.setFullscreen(!Display.isFullscreen());
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resizeDisplay();

	}
	public static void resizeDisplay(){
		sizeX = Display.getWidth();
		sizeY = Display.getHeight();
		resizeWindow(sizeX, sizeY);
		//System.out.println("resized to " + sizeX +" " + sizeY);
	}
	public static void init () {

		//how do i get a framebuffer by name?
		//ok

		try {
			Display.setDisplayMode(new DisplayMode(sizeX,sizeY));
			Display.create();
			Display.setResizable(true);
			//resizeDisplay();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Shader.initShader();
		Framebuffer.initFramebuffers();
		// init OpenGL

		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel (GL11.GL_SMOOTH);


		resizeDisplay();
	}
	private static void resizeWindow(int x, int y){
		if(x==0)x=1;
		if(y==0)y=1;
		//todo
		GL11.glViewport(0, 0, x, y); 
		GL11.glMatrixMode(GL11.GL_PROJECTION);// Select The Projection Matrix
		GL11.glLoadIdentity();// Reset The Projection Matrix
		gluPerspective(camera.fov,(float)x/(float)y, 0.1f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);                     // Select The Modelview Matrix
		GL11.glLoadIdentity();                           // Reset The Modelview Matrix
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		Framebuffer.framebufferList.get("pp1").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("pp2").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("pp3").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("pp4").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("pp5").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("blurTemp").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("blurOut").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("bloomOut").resizeFramebuffer(x, y);
		Framebuffer.framebufferList.get("flareOut").resizeFramebuffer(x, y);

	}
	private static void drawPixel2 (float x , float y, float z, float size){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x -size, y -size, z + size);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x +size, y -size, z + size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x +size, y +size, z + size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x -size, y +size, z + size);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x -size, y -size, z - size);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x +size, y -size, z - size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x +size, y +size, z - size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x -size, y +size, z - size);


		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x +size, y -size, z-size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x +size, y -size, z+size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x +size, y +size, z+size);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x +size, y +size, z-size);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x -size, y -size, z-size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x -size, y -size, z+size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x -size, y +size, z+size);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x -size, y +size, z-size);


		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x +size, y +size, z-size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x +size, y +size, z+size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x -size, y +size, z+size);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x -size, y +size, z-size);

		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(x +size, y -size, z-size);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(x +size, y -size, z+size);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(x -size, y -size, z+size);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(x -size, y -size, z-size);

		GL11.glEnd();
	}
	private static void drawPixel (float x , float y, float z, float r, float g, float b, float size){
		GL11.glColor3f(r,g,b);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(x -size, y -size, z + size);
		GL11.glVertex3f(x +size, y -size, z + size);
		GL11.glVertex3f(x +size, y +size, z + size);
		GL11.glVertex3f(x -size, y +size, z + size);

		GL11.glVertex3f(x -size, y -size, z - size);
		GL11.glVertex3f(x +size, y -size, z - size);
		GL11.glVertex3f(x +size, y +size, z - size);
		GL11.glVertex3f(x -size, y +size, z - size);


		GL11.glVertex3f(x +size, y -size, z-size);
		GL11.glVertex3f(x +size, y -size, z+size);
		GL11.glVertex3f(x +size, y +size, z+size);
		GL11.glVertex3f(x +size, y +size, z-size);

		GL11.glVertex3f(x -size, y -size, z-size);
		GL11.glVertex3f(x -size, y -size, z+size);
		GL11.glVertex3f(x -size, y +size, z+size);
		GL11.glVertex3f(x -size, y +size, z-size);


		GL11.glVertex3f(x +size, y +size, z-size);
		GL11.glVertex3f(x +size, y +size, z+size);
		GL11.glVertex3f(x -size, y +size, z+size);
		GL11.glVertex3f(x -size, y +size, z-size);

		GL11.glVertex3f(x +size, y -size, z-size);
		GL11.glVertex3f(x +size, y -size, z+size);
		GL11.glVertex3f(x -size, y -size, z+size);
		GL11.glVertex3f(x -size, y -size, z-size);

		GL11.glEnd();
	}
	private static void drawParticles(){
		//ARBShaderObjects.glUseProgramObjectARB(Shader.shaders.get("shader"));


		for(int i=0; i<numberofparticles; i++){
			drawPixel(ParticleArray.posx[i], ParticleArray.posy[i], ParticleArray.posz[i],
					ParticleArray.colr[i], 
					ParticleArray.colg[i], 
					ParticleArray.colb[i], 
					0.01f * (float)Math.sqrt(ParticleArray.mass[i]));
		}

	}
	private static void drawParticles2(){
		//ARBShaderObjects.glUseProgramObjectARB(Shader.shaders.get("shader"));


		for(int i=0; i<numberofparticles; i++){
			drawPixel2(ParticleArray.posx[i], ParticleArray.posy[i], ParticleArray.posz[i],
					0.01f * (float)Math.sqrt(ParticleArray.mass[i]));
		}

	}

	private static void drawRotatingCube(){

		translateCrap(0f, -5f, 0f);
		rotateCrap(ppwhatx, ppwhaty, 0f);
		/*
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, 0);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, 0);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,1, 0);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, 0);
		GL11.glEnd();
		 */

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, -1);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, -1);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,1, -1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, -1);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, 1);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, 1);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,1, 1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, 1);


		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(1,-1, -1);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, +1);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,1, 1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1,1, -1);

		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, -1);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(-1,-1, +1);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(-1,1, 1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, -1);



		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,1, -1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(1,1, 1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, 1);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,1, -1);

		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, -1);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,-1, 1);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,-1, 1);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, -1);


		GL11.glEnd();



		/*
		GL11.glVertex3f(x +size, y +size, z-size);
		GL11.glVertex3f(x +size, y +size, z+size);
		GL11.glVertex3f(x -size, y +size, z+size);
		GL11.glVertex3f(x -size, y +size, z-size);

		GL11.glVertex3f(x +size, y -size, z-size);
		GL11.glVertex3f(x +size, y -size, z+size);
		GL11.glVertex3f(x -size, y -size, z+size);
		GL11.glVertex3f(x -size, y -size, z-size);
		 */


	}

	private static void drawFSQuad(){
		GL11.glLoadIdentity();
		translateCrap(0f, -2f, 0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(-1,-1, 0.5f);
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex3f(1,-1, 0.5f);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(1,1, 0.5f);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(-1,1, 0.5f);
		GL11.glEnd();	
	}
	private static Framebuffer Blur(Framebuffer framebuffer, int passes){ //takes framebuffer to blur, returns output framebuffer
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		switchToOrtho();
		for(int i=0; i < passes; i++){ // todo maybe i can optimize looking up the framebuffers
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("blurTemp").framebufferID);
			GL20.glUseProgram(Shader.shaders.get("gaush"));
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.textureID);
			drawFSQuad();
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("blurOut").framebufferID);
			GL20.glUseProgram(Shader.shaders.get("gausv"));
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Framebuffer.framebufferList.get("blurTemp").textureID);
			drawFSQuad();
			framebuffer = Framebuffer.framebufferList.get("blurOut");// for returns AND it re-uses it in the next pass
		}
		return framebuffer;

	}
	private static Framebuffer Bloom(Framebuffer framebuffer, int blurPasses){
		switchToOrtho();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		Framebuffer blurred = Blur(framebuffer, blurPasses);
		Framebuffer output = Framebuffer.framebufferList.get("bloomOut");
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, output.framebufferID);

		//if(PostProcessEnabled)glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("pp5").framebufferID);
		//else glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		GL11.glColor3f(1f, 1f, 1f);
		GL20.glUseProgram(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, blurred.framebufferID);
		drawFSQuad();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.textureID);
		drawFSQuad();
		return output;
	}
	private static Framebuffer Flare(Framebuffer framebuffer){
		switchToOrtho();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		Framebuffer output = Framebuffer.framebufferList.get("flareOut");
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, output.framebufferID);

		GL11.glColor3f(1f, 1f, 1f);
		GL20.glUseProgram(Shader.shaders.get("lensflare"));
		//GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.textureID);
		drawFSQuad();
		return output;
	}
	private static void PostProcess(){
		Framebuffer framebuffer = Framebuffer.framebufferList.get("pp1");
		if(PostProcessBloom){
			framebuffer = Bloom(framebuffer, PostProcessBloomBlurPasses); //renders bloom and sets framebuffer to output
		}
		if(CubeProcessEnabled)glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("pp5").framebufferID);
		else glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		GL20.glUseProgram(0); //standard
		switchToOrtho();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.textureID);
		drawFSQuad();
		if(PostProcessFlare){
			//framebuffer = Flare(framebuffer); //renders flare and sets framebuffer to output
			framebuffer = Flare(Framebuffer.framebufferList.get("blurOut"));
		}
		if(CubeProcessEnabled)glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("pp5").framebufferID);
		else glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		GL20.glUseProgram(0); //standard
		switchToOrtho();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.textureID);
		drawFSQuad();
	}
	private static void CubeProcess(){
		switchToPerspective();
		GL11.glColor3f(1f, 1f, 1f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST); // depth testing, yo
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // backbuffer
		GL20.glUseProgram(0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Framebuffer.framebufferList.get("pp1").textureID);
		GL11.glLoadIdentity();
		if(PostProcessCube){
			GL11.glClearColor (1.0f, 1.0f, 1.0f, 0.5f);
			GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			drawRotatingCube();
		}
		else{
			camera.AdjustToCamera();
			GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.5f);
			GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			drawParticles2();
		}
	}
	public static void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		GL20.glUseProgram(0);

		switchToPerspective();
		if(PostProcessEnabled || CubeProcessEnabled)glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, Framebuffer.framebufferList.get("pp1").framebufferID);
		else glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.5f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glMatrixMode(GL11.GL_MODELVIEW);//just fo shitsngiggles
		GL11.glLoadIdentity();// again, for shitsngiggles
		camera.AdjustToCamera();
		drawParticles();

		if(PostProcessEnabled){
			PostProcess();
		}
		if(CubeProcessEnabled){
			CubeProcess();
		}
		

		if(Display.wasResized())resizeDisplay();
		Display.update();


	}

	public static void translateCrap(float x, float y, float z){
		GL11.glTranslatef(x, z, y);
	}
	public static void switchToPerspective(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);// Select The Projection Matrix
		GL11.glLoadIdentity();// Reset The Projection Matrix
		gluPerspective(camera.fov,(float)sizeX/(float)sizeY, 0.1f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);                     // Select The Modelview Matrix
		GL11.glLoadIdentity();                           // Reset The Modelview Matrix
	}
	public static void switchToOrtho(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);// Select The Projection Matrix
		GL11.glLoadIdentity();// Reset The Projection Matrix
		GL11.glOrtho(-1, 1, -1, 1, 0.1, 100); //maybe change
		GL11.glMatrixMode(GL11.GL_MODELVIEW);                     // Select The Modelview Matrix
		GL11.glLoadIdentity();                           // Reset The Modelview Matrix
	}
	public static void rotateCrap(float roll, float yaw, float pitch){
		GL11.glRotatef(roll, 0, 0, 1);
		GL11.glRotatef(yaw,  0, 1, 0);
		GL11.glRotatef(pitch, 1, 0, 0);
	}
}
