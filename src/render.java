import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class render extends nbody {
	static int colorTextureID;
	static int framebufferID;
	static int depthRenderBufferID;
	//public static int winResX = 800;
	//public static int winResY = 600;
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
		try {
			Display.setDisplayMode(new DisplayMode(sizeX,sizeY));
			Display.create();
			Display.setResizable(true);
			resizeDisplay();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Shader.initShader();
		// init OpenGL
		resizeDisplay();
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST); // depth testing, yo
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glShadeModel (GL11.GL_SMOOTH);
		//framebufferID = glGenFramebuffersEXT();
		//colorTextureID = GL11.glGenTextures();
		//depthRenderBufferID = glGenRenderbuffersEXT();
		//glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		//GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
		//GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, sizeX, sizeY, 0,GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
		//glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, colorTextureID, 0);	

		// initialize depth renderbuffer
		//glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
		//glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, 512, 512); // get the data space for it
		//glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

		//glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
	}
	private static void resizeWindow(int x, int y){
		if(x==0)x=1;
		if(y==0)y=1;
		//todo
		GL11.glViewport(0, 0, x, y); 
		GL11.glMatrixMode(GL11.GL_PROJECTION);// Select The Projection Matrix
		GL11.glLoadIdentity();// Reset The Projection Matrix
		if(nbody.ortho)GL11.glOrtho(0, 800, 0, 600, 1, -1);
		else gluPerspective(camera.fov,(float)x/(float)y, 0f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);                     // Select The Modelview Matrix
		GL11.glLoadIdentity();                           // Reset The Modelview Matrix
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

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
	private static void drawVoxels(){
		//ARBShaderObjects.glUseProgramObjectARB(Shader.shaders.get("shader"));


		for(int x=0; x<256; x++){
			for(int y=0; y<256; y++){
				for(int z=0; z<256; z++){
					if(voxelize.voxelSize[x][y][z] == 0)continue;
					float size = voxelize.voxelSize[x][y][z]/256f;
					float nx = (float)x/256.0f;
					float ny = (float)y/256.0f;
					float nz = (float)z/256.0f;
					//System.out.println("x:"+nx+" y:"+ ny + " z:" + nz + " size:" + size);

					GL11.glColor3f(1f,0,0);

					GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex3f(nx -size, ny -size, nz + size);
					GL11.glVertex3f(nx +size, ny -size, nz + size);
					GL11.glVertex3f(nx +size, ny +size, nz + size);
					GL11.glVertex3f(nx -size, ny +size, nz + size);
					GL11.glEnd();

				}
			}
		}

	}
	private static void drawFSQuad(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glLoadIdentity();
		translateCrap(0f, -5f, 0f);
		rotateCrap(ppwhatx, ppwhaty, 0f);
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

	}
	private static void PostProcess(){
		GL11.glViewport (0, 0, 800, 600);
		//GL11.glOrtho(0, 800, 0, 600, 1, -1);//postprocessing in in ortho
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);

		GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.5f);
		GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		drawFSQuad();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glFlush ();
		//gluPerspective(camera.fov,(float)sizeX/(float)sizeY, 0f,100.0f);
	}
	public static void draw() {
		//GL11.glLoadIdentity();\
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		//glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		GL11.glClearColor (0.0f, 0.0f, 0.0f, 0.5f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glTranslatef(0.0f,0.0f,-6.0f); 
		GL11.glMatrixMode(GL11.GL_MODELVIEW);//just fo shitsngiggles
		GL11.glLoadIdentity();// again, for shitsngiggles
		camera.AdjustToCamera();
		//GL11.glPopMatrix();
		//GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_BLEND);GL11.glEnable(GL11.GL_DEPTH_TEST);
		//voxelize.voxelize();
		//drawVoxels();
		//GL11.glDisable(GL11.GL_DEPTH_TEST);GL11.glEnable(GL11.GL_BLEND);
		drawParticles();
		//PostProcess();
		if(Display.wasResized())resizeDisplay();
		Display.update();
		

	}

	public static void translateCrap(float x, float y, float z){
		GL11.glTranslatef(x, z, y);
	}
	public static void rotateCrap(float roll, float yaw, float pitch){
		GL11.glRotatef(roll, 0, 0, 1);
		GL11.glRotatef(yaw,  0, 1, 0);
		GL11.glRotatef(pitch, 1, 0, 0);
	}
}
