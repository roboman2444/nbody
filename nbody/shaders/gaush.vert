varying vec2 fsquadcoord;
void main(){
	gl_TexCoord[0]  = gl_MultiTexCoord0;
	gl_Normal = gl_NormalMatrix * gl_Normal; //Note that you can perform operations on matrices and vectors as if they were 
                                                //primitive types. This is useful for simple, readable code like this. 
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; //The order in which you times matrices and vertices is IMPORTANT.
    gl_FrontColor = gl_Color;   //These lines just pass on the color value to the fragment shader.
    gl_BackColor = gl_Color;
    
}