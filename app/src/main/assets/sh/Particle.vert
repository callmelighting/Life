#version 300 es

uniform mat4 uMVPMatrix;        // 总变换矩阵
uniform float uCurrentTime;     // 现在时间
uniform float uSize;            // 粒子尺寸
uniform vec3 uGravity;          // 粒子在XYZ方向上的加速度
in float vLiveTime;             // 粒子寿命
in vec3 vPosition;              // 粒子初始位置
in vec3 vSpeed;                 // 粒子在XYZ方向上的分速度
out float fColorMixRatio;       // 粒子颜色值混合比例

void main(){
    float t=mod(uCurrentTime,vLiveTime);
    float pX=vPosition.x+vSpeed.x*t+1.f/2.f*uGravity.x*pow(t,2.f);
    float pY=vPosition.y+vSpeed.y*t+1.f/2.f*uGravity.y*pow(t,2.f);
    float pZ=vPosition.z+vSpeed.z*t+1.f/2.f*uGravity.z*pow(t,2.f);
    
    fColorMixRatio=pow(t/vLiveTime,2.f);
    
    gl_Position=uMVPMatrix*vec4(pX,pY,pZ,1.f);
    gl_PointSize=uSize;
}
