varying vec2 texCoord;
//varying vec2 blurTexCoords[14];// todo maybe put precomputation of texcoords here
void main(){
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; //The order in which you times matrices and vertices is IMPORTANT.
    texCoord = gl_MultiTexCoord0.xy;
}