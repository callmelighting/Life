#version 300 es

uniform mat4 uMVPMatrix;    // 总变换矩阵
in vec3 vVertex;            // 顶点位置
in vec2 vTexCoor;           // 顶点纹理坐标

out vec2 fTexCoor;          // 片元纹理坐标

void main(){
    // 根据总变换矩阵计算此次绘制此顶点位置
    gl_Position=uMVPMatrix*vec4(vVertex,1.f);
    // 将接收的纹理坐标传递给片元着色器
    fTexCoor=vTexCoor;
}
