uniform sampler2D texture;
varying vec2 texCoord;
varying vec2 blurTexCoords[14];

void main(){
    //gl_FragColor = texture2D(texture, texCoord);

   
   	gl_FragColor = vec4(0.0);
    gl_FragColor += texture2D(texture, blurTexCoords[ 0])*0.0044299121055113265;
    gl_FragColor += texture2D(texture, blurTexCoords[ 1])*0.00895781211794;
    gl_FragColor += texture2D(texture, blurTexCoords[ 2])*0.0215963866053;
    gl_FragColor += texture2D(texture, blurTexCoords[ 3])*0.0443683338718;
    gl_FragColor += texture2D(texture, blurTexCoords[ 4])*0.0776744219933;
    gl_FragColor += texture2D(texture, blurTexCoords[ 5])*0.115876621105;
    gl_FragColor += texture2D(texture, blurTexCoords[ 6])*0.147308056121;
    gl_FragColor += texture2D(texture, texCoord         )*0.159576912161;
    gl_FragColor += texture2D(texture, blurTexCoords[ 7])*0.147308056121;
    gl_FragColor += texture2D(texture, blurTexCoords[ 8])*0.115876621105;
    gl_FragColor += texture2D(texture, blurTexCoords[ 9])*0.0776744219933;
    gl_FragColor += texture2D(texture, blurTexCoords[10])*0.0443683338718;
    gl_FragColor += texture2D(texture, blurTexCoords[11])*0.0215963866053;
    gl_FragColor += texture2D(texture, blurTexCoords[12])*0.00895781211794;
    gl_FragColor += texture2D(texture, blurTexCoords[13])*0.0044299121055113265;
}