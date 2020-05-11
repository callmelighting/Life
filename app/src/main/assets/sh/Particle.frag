#version 300 es
precision mediump float;

uniform sampler2D uTexture; // 粒子纹理
uniform vec4 uColorHead;    // 粒子头部颜色值
uniform vec4 uColorTail;    // 粒子尾部颜色值
in float fColorMixRatio;    // 粒子颜色值混合比例
out vec4 fragColor;         // 输出片元颜色值

void main(){
    vec2 texCoord=gl_PointCoord;
    vec4 addColor=mix(uColorHead,uColorTail,fColorMixRatio);
    
    float ratio=texture(uTexture,texCoord).r;
    float alpha=clamp((1.f-ratio)*(1.f-fColorMixRatio)*addColor.a,0.f,1.f);
    
    if(alpha<=.4f)discard;
    fragColor=vec4(addColor.rgb,alpha);
}
