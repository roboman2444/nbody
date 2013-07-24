varying vec2 texCoord;
varying vec2 blurTexCoords[14];
void main(){
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; //The order in which you times matrices and vertices is IMPORTANT.
    texCoord = gl_MultiTexCoord0.xy;
    blurTexCoords[ 0] = texCoord + vec2(-0.028, 0.0);
    blurTexCoords[ 1] = texCoord + vec2(-0.024, 0.0);
    blurTexCoords[ 2] = texCoord + vec2(-0.020, 0.0);
    blurTexCoords[ 3] = texCoord + vec2(-0.016, 0.0);
    blurTexCoords[ 4] = texCoord + vec2(-0.012, 0.0);
    blurTexCoords[ 5] = texCoord + vec2(-0.008, 0.0);
    blurTexCoords[ 6] = texCoord + vec2(-0.004, 0.0);
    blurTexCoords[ 7] = texCoord + vec2( 0.004, 0.0);
    blurTexCoords[ 8] = texCoord + vec2( 0.008, 0.0);
    blurTexCoords[ 9] = texCoord + vec2( 0.012, 0.0);
    blurTexCoords[10] = texCoord + vec2( 0.016, 0.0);
    blurTexCoords[11] = texCoord + vec2( 0.020, 0.0);
    blurTexCoords[12] = texCoord + vec2( 0.024, 0.0);
    blurTexCoords[13] = texCoord + vec2( 0.028, 0.0);
}