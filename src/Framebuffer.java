import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;


public class Framebuffer {
	int width, height;
	int framebufferID, textureID;
	public static HashMap<String, Framebuffer> framebufferList = new HashMap<String, Framebuffer>();
	public Framebuffer(int x, int y){
		//set up texture
		textureID= GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, x, y, 0,GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		//set up fbo
		framebufferID = glGenFramebuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, textureID, 0); //attach to fbo
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);//unbind
		System.out.println("fbid: " + framebufferID +" texid: " + textureID);
		width = x;
		height = y;
	}
	public int resizeFramebuffer(int x, int y){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, x, y, 0,GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		width = x;
		height = y;
		/*//todo error checking
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, 0, 0); //attach to fbo
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, textureID, 0); //attach to fbo
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);//unbind
		*/
		return 1;

	}
	public static void initFramebuffers(){
		framebufferList.put("pp1", new Framebuffer(render.sizeX, render.sizeY));
		framebufferList.put("pp2", new Framebuffer(render.sizeX, render.sizeY));
		framebufferList.put("pp3", new Framebuffer(render.sizeX, render.sizeY));
		framebufferList.put("pp4", new Framebuffer(render.sizeX, render.sizeY));
		framebufferList.put("pp5", new Framebuffer(render.sizeX, render.sizeY));

	}
}