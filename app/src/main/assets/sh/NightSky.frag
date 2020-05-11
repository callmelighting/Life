#version 300 es
precision mediump float;

uniform sampler2D sTexture;     // 纹理内容数据
in vec2 fTexCoor;               // 传递给片元着色器的纹理坐标

out vec4 fragColor;             // 片元最终颜色

void main(){
    // 从纹理中采样出颜色值
    vec4 finalColor=texture(sTexture,fTexCoor);
    // 给此片元颜色值
    fragColor=finalColor;
}
