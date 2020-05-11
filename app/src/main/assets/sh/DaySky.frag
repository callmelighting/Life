#version 300 es
precision highp float;

uniform int uTime;              // 时间
uniform vec3 uSkyColor1;        // 天空颜色1
uniform vec3 uSkyColor2;        // 天空颜色2
uniform vec3 uCloudColor1;      // 云层颜色1
uniform vec3 uCloudColor2;      // 云层颜色2
uniform float uEmptiness;       // 云层参数1
uniform float uSharpness;       // 云层参数2
uniform sampler2D uTexture0;    // 噪声纹理0
uniform sampler2D uTexture1;    // 噪声纹理1
uniform sampler2D uTexture2;    // 噪声纹理2
uniform sampler2D uTexture3;    // 噪声纹理3
uniform sampler2D uTexture4;    // 噪声纹理4
in vec2 fTexCoor;               // 片元纹理坐标

out vec4 fragColor;             // 片元输出颜色

void main(){
    float speedK=.0002f;        // 速度系数
    float timeK=float(uTime);   // 时间系数
    
    float n0=texture(uTexture0,vec2(fTexCoor.x+speedK*9.f*timeK,fTexCoor.y)).r;
    float n1=texture(uTexture1,vec2(fTexCoor.x+speedK*7.f*timeK,fTexCoor.y)).r;
    float n2=texture(uTexture2,vec2(fTexCoor.x+speedK*5.f*timeK,fTexCoor.y)).r;
    float n3=texture(uTexture3,vec2(fTexCoor.x+speedK*3.f*timeK,fTexCoor.y)).r;
    float n4=texture(uTexture4,vec2(fTexCoor.x+speedK*1.f*timeK,fTexCoor.y)).r;
    float n=.4f*n0+.2f*n1+.2f*n2+.1f*n3+.1f*n4;
    
    float emptiness=uEmptiness;
    float sharpness=uSharpness;
    float c1=(clamp(n,emptiness,sharpness)-emptiness)/(sharpness-emptiness);
    emptiness=uEmptiness-.12f;
    sharpness=uSharpness-.12f;
    float c2=(clamp(n,emptiness,sharpness)-emptiness)/(sharpness-emptiness);
    emptiness=uEmptiness;
    sharpness=uSharpness+.25f;
    float c3=(clamp(n,emptiness,sharpness)-emptiness)/(sharpness-emptiness);
    
    vec3 cloudColor=mix(uCloudColor2*c1,uCloudColor2*c2,.2f);
    vec3 skyColor=mix(uSkyColor1,uSkyColor2,fTexCoor.y);
    
    vec3 color=mix(
        skyColor,
        clamp(skyColor+cloudColor,0.f,1.f),
        clamp(cloudColor,0.f,1.f)
    );
    
    color=mix(color,1.08f*uCloudColor1,c3);
    
    fragColor=vec4(color,1.f);
}
