#version 300 es
precision mediump float;

uniform mat4 uMPCMatrix;
uniform sampler2D uMirrorTex;
uniform sampler2D uWaterTex;
in vec4 fVertex;
in vec2 fTexCoor;
in vec4 fLight;
in vec3 fNormal;

out vec4 fragColor;

void main(){
    vec4 waterColor=texture(uWaterTex,fTexCoor);
    
    vec4 mpcVertex=uMPCMatrix*fVertex;
    mpcVertex=mpcVertex/mpcVertex.w;
    float s=1.f-(mpcVertex.s/2.f+.5f);
    float t=mpcVertex.t/2.f+.5f;
    
    float tempK=.02f;
    s=s+tempK*fNormal.x;
    t=t-tempK*fNormal.y;
    
    if(s>=0.f&&s<=1.f&&t>=0.f&&t<=1.f){
        vec4 mirrorColor=texture(uMirrorTex,vec2(s,t));
        fragColor=mix(waterColor,mirrorColor,.7f)*fLight;
    }else{
        fragColor=waterColor*fLight;
    }
}
