#version 300 es

uniform mat4 uMVPMatrix;    // 总变换矩阵
in float vSize;             // 尺寸
in vec4 vColor;             // 颜色
in vec3 vPosition;          // 位置

out vec4 fColor;            // 颜色

void main(){
    gl_PointSize=vSize;
    fColor=vColor;
    gl_Position=uMVPMatrix*vec4(vPosition,1.f);
}
