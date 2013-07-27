import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;


public class Framebuffer {
	int width, height;
	int framebufferID, textureID, depthID;
	public static HashMap<String, Framebuffer> framebufferList = new HashMap<String, Framebuffer>();
	public Framebuffer(int x, int y, boolean depth){
		//set up texture
		textureID= GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, x, y, 0,GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		/*if(depth){
			depthID = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthID);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_DEPTH_TEXTURE_MODE, GL11.GL_INTENSITY);
				//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_COMPARE_MODE, GL11.GL_COMPARE_R_TO_TEXTURE);
				//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, Gl11.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);
				//glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, 256, 256, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, NULL);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, x, y, 0,GL11.GL_DEPTH_COMPONENT, GL11.GL_INT, (java.nio.ByteBuffer) null);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}*/
		//set up fbo
		framebufferID = glGenFramebuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, textureID, 0); //attach to fbo
			//if(depth)glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL11.GL_TEXTURE_2D, depthID, 0); //attach to fbo
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
		framebufferList.put("pp1", new Framebuffer(render.sizeX, render.sizeY, true));
		framebufferList.put("pp2", new Framebuffer(render.sizeX, render.sizeY, false));
		framebufferList.put("pp3", new Framebuffer(render.sizeX, render.sizeY, false));
		framebufferList.put("pp4", new Framebuffer(render.sizeX, render.sizeY, false));
		framebufferList.put("pp5", new Framebuffer(render.sizeX, render.sizeY, false));

		framebufferList.put("blurTemp", new Framebuffer(render.sizeX, render.sizeY, false));
		framebufferList.put("blurOut", new Framebuffer(render.sizeX, render.sizeY, false));

		framebufferList.put("bloomOut", new Framebuffer(render.sizeX, render.sizeY, false));
		framebufferList.put("flareOut", new Framebuffer(render.sizeX, render.sizeY, false));

	}
}