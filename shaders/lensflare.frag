uniform sampler2D texture;
varying vec2 texCoord;
//varying vec2 blurTexCoords[14];
        uniform int                     FlareSamples = 8;
        uniform float                   FlareDispersal = 0.5;
        uniform vec3                    FlareRadialThreshold = vec3(0.4);
        uniform vec3                    FlareHaloThreshold = vec3(0.4);
        uniform float                   FlareHalo = 0.7;
        uniform float                   FlareRadial = 0.2;
        uniform float                   FlareHaloWidth = 0.5;
        uniform vec3                    FlareChroma = vec3(-0.01, 0., 0.01);
        vec3 ChromaTextureDistorted(in sampler2D tex, in vec2 sample_center, in vec2 sample_vector, in vec3 distortion) {
                return vec3(    texture2D(tex, sample_center + sample_vector * distortion.r).r,
                                texture2D(tex, sample_center + sample_vector * distortion.g).g,
                                texture2D(tex, sample_center + sample_vector * distortion.b).b    );
        }
        vec2 FlipTexCoord(in vec2 ftexCoord) {
                return 1.0 - ftexCoord;
        }


void main(){
    //gl_FragColor = texture2D(texture, texCoord);
        vec2    FlareTexCoord   =       FlipTexCoord(texCoord);
        vec2    sample_vector   =       (vec2(0.5) - FlareTexCoord) * FlareDispersal;

	float weight = length(vec2(0.5) - fract(texCoord + vec2(0.5))) / length(vec2(0.5));
	weight = pow(1.0 - weight, 5.0);


        if(FlareRadial != 0){
                for (int i = 0; i < FlareSamples; ++i) {
                        vec2 offset   = sample_vector * float(i);
                        if (FlareChroma != 0.0){
                                gl_FragColor += vec4(clamp(ChromaTextureDistorted(texture,FlareTexCoord + offset, offset, FlareChroma)   - FlareRadialThreshold, 0,1) *FlareRadial, 1);
                        } else {
                                gl_FragColor += clamp(texture2D(texture,FlareTexCoord + offset)- vec4(FlareRadialThreshold,1), 0,1) *FlareRadial;
                        }
                }
        }
        if(FlareHalo != 0){
                vec2    halo_vector     = normalize(sample_vector) * FlareHaloWidth;
                if(FlareChroma != 0.0){
                        gl_FragColor += vec4(clamp(ChromaTextureDistorted(texture, FlareTexCoord + halo_vector, halo_vector, FlareChroma)        - FlareHaloThreshold, 0,1) *FlareHalo * weight, 1);
                } else {
                        gl_FragColor += clamp(texture2D(texture, FlareTexCoord + halo_vector)      - vec4(FlareHaloThreshold,1), 0,1) *FlareHalo * weight;
                }
        }

    
}//main?
