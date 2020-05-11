#version 300 es

uniform mat4 uMVPMatrix;        // 总变换矩阵
uniform mat4 uMPCMatrix;        // 投影与观察组合矩阵
uniform mat4 uMMatrix;          // 基本变换矩阵
uniform vec4 uLight1;           // 环境光强度
uniform vec4 uLight2;           // 散射光强度
uniform vec4 uLight3;           // 镜面光强度
uniform vec3 uLightDirection;   // 定向光方向
uniform vec3 uCameraLocation;   // 摄像机位置
uniform float uRoughness;       // 粗糙度
uniform mediump float uShadow;  // 影子绘制标志位(0.0f绘制物体, 否则绘制影子)
in vec3 vPosition;              // 顶点位置
in vec3 vNormalVector;          // 法向量
in vec2 vTexCoor;               // 纹理坐标

out vec2 fTextureCoord;         // 传递给片元着色器的纹理坐标
out vec4 fLight;                // 传递给片元着色器的光照强度

// 定向光光照计算的方法
vec4 directionalLight(
    in mat4 mMatrix,            // 基本变换矩阵
    in vec3 cameraLocation,     // 摄像机位置
    in vec3 lightDirection,     // 定向光方向
    in vec4 ambient,            // 环境光强度
    in vec4 diffuse,            // 散射光强度
    in vec4 specular,           // 镜面光强度
    in float roughness,         // 粗糙度
    in vec3 normal,             // 法向量
    in vec3 position            // 顶点位置
){
    vec4 finalAmbient;          // 环境光最终强度
    vec4 finalDiffuse;          // 散射光最终强度
    vec4 finalSpecular;         // 镜面光最终强度
    
    // 直接得出环境光的最终强度
    finalAmbient=ambient;
    // 求基本变换矩阵左上角的3X3矩阵
    mat3 baseM3=mat3(mMatrix[0].xyz,mMatrix[1].xyz,mMatrix[2].xyz);
    // 求子阵的伴随矩阵
    mat3 adjointM=inverse(baseM3)*determinant(baseM3);
    // 求伴随矩阵的转置矩阵
    mat3 TPM=transpose(adjointM);
    // 求世界坐标系中的法向量
    vec3 newNormal=normalize(TPM*normal);
    // 被照射顶点到摄像机的向量
    vec3 eye=normalize(cameraLocation-(mMatrix*vec4(position,1.f)).xyz);
    // 格式化定向光方向向量
    vec3 vp=normalize(lightDirection);
    // 求视线与光线向量的半向量
    vec3 halfVector=normalize(vp+eye);
    // 求法向量与vp的点积与0的最大值
    float nDotViewPosition=max(0.f,dot(newNormal,vp));
    // 计算散射光的最终强度
    finalDiffuse=diffuse*nDotViewPosition;
    // 法线与半向量的点积
    float nDotViewHalfVector=dot(newNormal,halfVector);
    // 镜面反射光强度因子
    float powerFactor=max(0.f,pow(nDotViewHalfVector,roughness));
    // 计算镜面光的最终强度
    finalSpecular=specular*powerFactor;
    // 将三个光照通道最终强度值求和返回
    return finalAmbient+finalDiffuse+finalSpecular;
}

// 主方法
void main(){
    if(uShadow==0.f){           // 绘制物体
        // 根据总变换矩阵计算顶点最终位置
        gl_Position=uMVPMatrix*vec4(vPosition,1.f);
        // 将接收的纹理坐标传递给片元着色器
        fTextureCoord=vTexCoor;
        // 将最终光照强度传递给片元着色器
        fLight=directionalLight(
            uMMatrix,           // 基本变换矩阵
            uCameraLocation,    // 摄像机位置
            uLightDirection,    // 定向光方向
            uLight1,            // 环境光强度
            uLight2,            // 散射光强度
            uLight3,            // 镜面光强度
            uRoughness,         // 粗糙度
            vNormalVector,      // 法向量
            vPosition);         // 顶点位置
    }else{                      // 绘制影子
        // 定向光方向
        vec3 m=uLightDirection;
        // 投影平面法向量
        vec3 n=vec3(0.f,1.f,0.f);
        // 投影平面上任意一点
        vec3 A=vec3(0.f,-1.05f,0.f);
        // 世界坐标系中顶点的位置
        vec3 V=(uMMatrix*vec4(vPosition,1.f)).xyz;
        // 顶点投影到平面上的位置
        vec3 newV=(V-m)+m*(dot(n,A-V+m)/(dot(n,m)));
        // 根据组合矩阵计算顶点最终位置
        gl_Position=uMPCMatrix*vec4(newV,1.f);
        // 将环境光强度传递给片元着色器
        fLight=uLight1;
    }
}
