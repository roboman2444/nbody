


public class camera{
	public static float fov = 45;
	public static float posx = 0;
	public static float posy = 0;
	public static float posz = 0;
	public static float rotx = 0;
	public static float rotz = 0;
	public static float roty = 0;//not used in a pan/tilt style
	public static void AdjustToCamera(){
		//all shits is negative because move "world", not "camera"
		render.rotateCrap(-roty, -rotz, -rotx);
		render.translateCrap(-posx,-posy,-posz);
	}

}
