#version 300 es
precision mediump float;

uniform sampler2D sTexture; // 纹理内容数据
uniform float uShadow;      // 影子绘制标志位(0.0f绘制物体, 否则绘制影子)
uniform vec4 uShadowColor;  // 阴影颜色值

in vec4 fLight;             // 传递给片元着色器的光照强度
in vec2 fTextureCoord;      // 传递给片元着色器的纹理坐标

out vec4 fragColor;         // 片元最终颜色

void main(){
    if(uShadow==0.f){       // 绘制物体
        // 从纹理中采样出颜色值
        vec4 finalColor=texture(sTexture,fTextureCoord);
        // 给此片元颜色值
        fragColor=finalColor*fLight;
    }else{                  // 绘制影子
        // 给此片元颜色值
        fragColor=uShadowColor*fLight;
    }
}
