import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;


public class Shader{
	public static HashMap<String, Integer> shaders = new HashMap<String, Integer>();
	private static ArrayList<String> findShaders(File dir){
		//if(!dir.isDirectory()) return null; // returning null kinda borks some stuff
		File[] files = dir.listFiles();
		ArrayList<String> returnnames = new ArrayList();
		/*for(int i = 0; i < files.length; i++){
			if(!files[i].getName().contains(".frag")){
				continue;
			}
			i++;
			if(!files[i].getName().contains(".vert")){
				continue;
			}
			returnnames.add(files[i].getName().split(".vert")[0]);
		}*/
		return returnnames;	//this should return an ArrayList of 0 length if none, which is what i wanted
	}
	public static void initShader(){
		File directory = new File("shaders/");
		ArrayList<String> ShaderNames = findShaders(directory);
		for(int i=0; i< ShaderNames.size(); i++){
			int vertShader = 0, fragShader = 0;
			try {
				vertShader = createShader("shaders/" + ShaderNames.get(i) + ".vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
				fragShader = createShader("shaders/" + ShaderNames.get(i) + ".frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
			} catch(Exception exc) {
				exc.printStackTrace();
				return;
			} finally {
				if(vertShader == 0 || fragShader == 0)continue;;
			}
			int tempShaderInt = 0;//need this for error checking
			tempShaderInt = ARBShaderObjects.glCreateProgramObjectARB();
			if(tempShaderInt == 0)continue;
			ARBShaderObjects.glAttachObjectARB(tempShaderInt, vertShader);
			ARBShaderObjects.glAttachObjectARB(tempShaderInt, fragShader);

			ARBShaderObjects.glLinkProgramARB(tempShaderInt);
			if (ARBShaderObjects.glGetObjectParameteriARB(tempShaderInt, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
				System.err.println(getLogInfo(tempShaderInt));
				continue;
			}

			ARBShaderObjects.glValidateProgramARB(tempShaderInt);
			if (ARBShaderObjects.glGetObjectParameteriARB(tempShaderInt, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
				System.err.println(getLogInfo(tempShaderInt));
				continue;
			}
			shaders.put(ShaderNames.get(i), tempShaderInt);
			System.out.print("Loaded Shader \"" + ShaderNames.get(i) + "\" at location " + tempShaderInt + "\n");
		}
	}
	/*
	 * With the exception of syntax, setting up vertex and fragment shaders
	 * is the same.
	 * @param the name and path to the vertex shader
	 */
	private static int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

			return shader;
		}
		catch(Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));

			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}
			catch(Exception exc) {
				exception = exc;
			}
			finally {
				try {
					reader.close();
				}
				catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if(innerExc != null)
				throw innerExc;
		}
		catch(Exception exc) {
			exception = exc;
		}
		finally {
			try {
				in.close();
			}
			catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if(exception != null)
				throw exception;
		}

		return source.toString();
	}
}
