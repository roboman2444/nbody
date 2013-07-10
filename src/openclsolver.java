import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;


public class openclsolver {
	
	// OpenCL variables
	public static CLContext context;
	public static CLPlatform platform;
	public static List<CLDevice> devices;
	public static CLCommandQueue queue;
	public static CLKernel sumKernel;
	public static CLMem velxMemory;
	public static CLMem velyMemory;
	public static CLMem velzMemory;
	public static CLMem posxMemory;
	public static CLMem posyMemory;
	public static CLMem poszMemory;
	public static CLMem massMemory;
	// Used to determine how many units of work to do
	static int size;
	// Error buffer used to check for OpenCL error that occurred while a command was running
	static IntBuffer errorBuff = BufferUtils.createIntBuffer(1);
	// Create a buffer containing our array of numbers, we can use the buffer to create an OpenCL memory object
	static FloatBuffer posxBuff = BufferUtils.createFloatBuffer(size);
	static FloatBuffer posyBuff = BufferUtils.createFloatBuffer(size);
	static FloatBuffer poszBuff = BufferUtils.createFloatBuffer(size);
	static FloatBuffer massBuff = BufferUtils.createFloatBuffer(size);
	
	//This reads the result memory buffer
	static FloatBuffer velxBuff = BufferUtils.createFloatBuffer(size);
	static FloatBuffer velyBuff = BufferUtils.createFloatBuffer(size);
	static FloatBuffer velzBuff = BufferUtils.createFloatBuffer(size);
	
	public static void doSumExample() throws LWJGLException {
		size = nbody.numberofparticles;
		// Create our OpenCL context to run commands
		initializeCL();
		// Create an OpenCL 'program' from a source code file
		CLProgram sumProgram = CL10.clCreateProgramWithSource(context, loadText("openclkernel.cls"), null);
		// Build the OpenCL program, store it on the specified device
		int error = CL10.clBuildProgram(sumProgram, devices.get(0), "", null);
		// Check for any OpenCL errors
		Util.checkCLError(error);
		// Create a kernel instance of our OpenCl program
		sumKernel = CL10.clCreateKernel(sumProgram, "sum", null);


		
		// Create an OpenCL memory object containing a copy of the data buffer
		posxMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, posxBuff, errorBuff);
		posyMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, posyBuff, errorBuff);
		poszMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, poszBuff, errorBuff);
		massMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_WRITE_ONLY | CL10.CL_MEM_COPY_HOST_PTR, massBuff, errorBuff);
		// Check if the error buffer now contains an error
		Util.checkCLError(errorBuff.get(0));

		// Create an empty OpenCL buffer to store the result of adding the numbers together
		velxMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, size*4, errorBuff);
		velyMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, size*4, errorBuff);
		velzMemory = CL10.clCreateBuffer(context, CL10.CL_MEM_READ_ONLY, size*4, errorBuff);

		// Check for any error creating the memory buffer
		Util.checkCLError(errorBuff.get(0));

		// Set the kernel parameters
		sumKernel.setArg(0, size);
		sumKernel.setArg(1, posxMemory);
		sumKernel.setArg(2, posyMemory);
		sumKernel.setArg(3, poszMemory);
		sumKernel.setArg(4, velxMemory);
		sumKernel.setArg(5, velyMemory);
		sumKernel.setArg(6, velzMemory);
		sumKernel.setArg(7, massMemory);
		




		// This should print out 100 lines of result floats, each being 99.
		// Destroy our kernel and program
	//	CL10.clReleaseKernel(sumKernel);
	//	CL10.clReleaseProgram(sumProgram);
		// Destroy our memory objects
	//	CL10.clReleaseMemObject(aMemory);
	//	CL10.clReleaseMemObject(bMemory);
	//	CL10.clReleaseMemObject(resultMemory);
		// Destroy the OpenCL context

	//	destroyCL();
	}
public static void runCLStuff(){
	posxBuff.put(ParticleArray.posx);
	posxBuff.rewind();	
	posyBuff.put(ParticleArray.posy);
	posyBuff.rewind();	
	poszBuff.put(ParticleArray.posz);
	poszBuff.rewind();
	massBuff.put(ParticleArray.mass);
	massBuff.rewind();
	CL10.clEnqueueWriteBuffer(queue, posxMemory, CL10.CL_TRUE, 0, posxBuff, null, null);
	CL10.clEnqueueWriteBuffer(queue, posyMemory, CL10.CL_TRUE, 0, posyBuff, null, null);
	CL10.clEnqueueWriteBuffer(queue, poszMemory, CL10.CL_TRUE, 0, poszBuff, null, null);
	CL10.clEnqueueWriteBuffer(queue, massMemory, CL10.CL_TRUE, 0, massBuff, null, null);
	
	// Create a buffer of pointers defining the multi-dimensional size of the number of work units to execute
	final int dimensions = 1; 
	PointerBuffer globalWorkSize = BufferUtils.createPointerBuffer(dimensions);
	globalWorkSize.put(0, size);
	// Run the specified number of work units using our OpenCL program kernel
	CL10.clEnqueueNDRangeKernel(queue, sumKernel, dimensions, null, globalWorkSize, null, null, null);
	CL10.clFinish(queue);
	// We read the buffer in blocking mode so that when the method returns we know that the result buffer is full
	CL10.clEnqueueReadBuffer(queue, velxMemory, CL10.CL_TRUE, 0, velxBuff, null, null);
	CL10.clEnqueueReadBuffer(queue, velyMemory, CL10.CL_TRUE, 0, velyBuff, null, null);
	CL10.clEnqueueReadBuffer(queue, velzMemory, CL10.CL_TRUE, 0, velzBuff, null, null);
	// Print the values in the result buffer
	ParticleArray.velx = velxBuff.array();
	ParticleArray.vely = velyBuff.array();
	ParticleArray.velz = velzBuff.array();
}
	
	
	
	public static void initializeCL() throws LWJGLException { 
		IntBuffer errorBuf = BufferUtils.createIntBuffer(1);
		// Create OpenCL
		CL.create();
		// Get the first available platform
		platform = CLPlatform.getPlatforms().get(0); 
		// Run our program on the GPU
		devices = platform.getDevices(CL10.CL_DEVICE_TYPE_GPU);
		// Create an OpenCL context, this is where we could create an OpenCL-OpenGL compatible context
		context = CLContext.create(platform, devices, errorBuf);
		// Create a command queue
		queue = CL10.clCreateCommandQueue(context, devices.get(0), CL10.CL_QUEUE_PROFILING_ENABLE, errorBuf);
		// Check for any errors
		Util.checkCLError(errorBuf.get(0)); 
	}


	public static void destroyCL() {
		// Finish destroying anything we created
		CL10.clReleaseCommandQueue(queue);
		CL10.clReleaseContext(context);
		// And release OpenCL, after this method call we cannot use OpenCL unless we re-initialize it
		CL.destroy();
	}


	public static String loadText(String name) {
		if(!name.endsWith(".cls")) {
			name += ".cls";
		}
		BufferedReader br = null;
		String resultString = null;
		try {
			// Get the file containing the OpenCL kernel source code
			File clSourceFile = new File(openclsolver.class.getClassLoader().getResource(name).toURI());
			// Create a buffered file reader for the source file
			br = new BufferedReader(new FileReader(clSourceFile));
			// Read the file's source code line by line and store it in a string builder
			String line = null;
			StringBuilder result = new StringBuilder();
			while((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}
			// Convert the string builder into a string containing the source code to return
			resultString = result.toString();
		} catch(NullPointerException npe) {
			// If there is an error finding the file
			System.err.println("Error retrieving OpenCL source file: ");
			npe.printStackTrace();
		} catch(URISyntaxException urie) {
			// If there is an error converting the file name into a URI
			System.err.println("Error converting file name into URI: ");
			urie.printStackTrace();
		} catch(IOException ioe) {
			// If there is an IO error while reading the file
			System.err.println("Error reading OpenCL source file: ");
			ioe.printStackTrace();
		} finally {
			// Finally clean up any open resources
			try {
				br.close();
			} catch (IOException ex) {
				// If there is an error closing the file after we are done reading from it
				System.err.println("Error closing OpenCL source file");
				ex.printStackTrace();
			}
		}

		// Return the string read from the OpenCL kernel source code file
		return resultString;
	}
}
