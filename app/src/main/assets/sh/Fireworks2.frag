#version 300 es
precision mediump float;

uniform sampler2D uTexture; // 纹理
in vec4 fColor;             // 颜色

out vec4 fragColor;         // 输出片元颜色

void main(){
    vec2 texCoord=gl_PointCoord;
    float alpha=(1.f-texture(uTexture,texCoord).r)*fColor.a;
    fragColor=vec4(fColor.rgb,alpha);
}
