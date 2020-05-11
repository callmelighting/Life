package bn.wallpaper.llz;

import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReaderForObject {                              // Object读取类
    private static final String TAG = "ReaderForObject";
    public float[] vXYZ;                                    // 顶点坐标数组
    public float[] nXYZ;                                    // 纹理坐标数组
    public float[] tST;                                     // 法向量数组


    public ReaderForObject(String path, Resources r) {      // 从obj文件中读取携带顶点信息的物体
        // 原始顶点坐标列表
        ArrayList<Float> alv = new ArrayList<>();
        // 结果顶点坐标列表
        ArrayList<Float> alvResult = new ArrayList<>();
        // 原始纹理坐标列表
        ArrayList<Float> alt = new ArrayList<>();
        // 结果纹理坐标列表
        ArrayList<Float> altResult = new ArrayList<>();
        // 原始法向量列表
        ArrayList<Float> aln = new ArrayList<>();
        // 结果法向量列表
        ArrayList<Float> alnResult = new ArrayList<>();

        try {
            InputStream in = r.getAssets().open(path);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps;

            // 扫面文件, 根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {       // 读取一行文本

                String[] tempsa = temps.split("[ ]+"); // 将文本行用空格符切分
                if (tempsa[0].trim().equals("v")) {         // 此行为顶点坐标行
                    // 若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                } else if (tempsa[0].trim().equals("vt")) { // 此行为纹理坐标行
                    // 若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
                    alt.add(Float.parseFloat(tempsa[1]));
                    alt.add(1 - Float.parseFloat(tempsa[2]));
                } else if (tempsa[0].trim().equals("vn")) { // 此行为法向量行
                    // 若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
                    aln.add(Float.parseFloat(tempsa[1])); // 放进aln列表中
                    aln.add(Float.parseFloat(tempsa[2])); // 放进aln列表中
                    aln.add(Float.parseFloat(tempsa[3])); // 放进aln列表中
                } else if (tempsa[0].trim().equals("f")) { // 此行为三角形面
                    // 计算第0个顶点的索引, 并获取此顶点的XYZ三个坐标
                    int index = Integer.parseInt(tempsa[1].split("/")[0]) - 1;
                    float x0 = alv.get(3 * index);
                    float y0 = alv.get(3 * index + 1);
                    float z0 = alv.get(3 * index + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    // 计算第1个顶点的索引, 并获取此顶点的XYZ三个坐标
                    index = Integer.parseInt(tempsa[2].split("/")[0]) - 1;
                    float x1 = alv.get(3 * index);
                    float y1 = alv.get(3 * index + 1);
                    float z1 = alv.get(3 * index + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    // 计算第2个顶点的索引, 并获取此顶点的XYZ三个坐标
                    index = Integer.parseInt(tempsa[3].split("/")[0]) - 1;
                    float x2 = alv.get(3 * index);
                    float y2 = alv.get(3 * index + 1);
                    float z2 = alv.get(3 * index + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    // 将纹理坐标组织到结果纹理坐标列表中
                    // 第0个顶点的纹理坐标
                    int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    // 第1个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    // 第2个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));

                    // =================================================
                    // 计算第0个顶点的法向量索引
                    int indexN = Integer.parseInt(tempsa[1].split("/")[2]) - 1; // 获取法向量编号
                    float nx0 = aln.get(3 * indexN); // 获取法向量的x值
                    float ny0 = aln.get(3 * indexN + 1); // 获取法向量的y值
                    float nz0 = aln.get(3 * indexN + 2); // 获取法向量的z值
                    alnResult.add(nx0); // 放入alnResult列表
                    alnResult.add(ny0); // 放入alnResult列表
                    alnResult.add(nz0); // 放入alnResult列表

                    // 计算第1个顶点的索引, 并获取此顶点的XYZ三个坐标
                    indexN = Integer.parseInt(tempsa[2].split("/")[2]) - 1;
                    float nx1 = aln.get(3 * indexN);
                    float ny1 = aln.get(3 * indexN + 1);
                    float nz1 = aln.get(3 * indexN + 2);
                    alnResult.add(nx1);
                    alnResult.add(ny1);
                    alnResult.add(nz1);

                    // 计算第2个顶点的索引, 并获取此顶点的XYZ三个坐标
                    indexN = Integer.parseInt(tempsa[3].split("/")[2]) - 1;
                    float nx2 = aln.get(3 * indexN);
                    float ny2 = aln.get(3 * indexN + 1);
                    float nz2 = aln.get(3 * indexN + 2);
                    alnResult.add(nx2);
                    alnResult.add(ny2);
                    alnResult.add(nz2);
                }
            }

            // 生成顶点坐标数组
            int size = alvResult.size();
            vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i);
            }

            // 生成纹理坐标数组
            size = altResult.size();
            tST = new float[size];
            for (int i = 0; i < size; i++) {
                tST[i] = altResult.get(i);
            }

            // 生成法向量数组
            size = alnResult.size();
            nXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                nXYZ[i] = alnResult.get(i);
            }

        } catch (Exception e) {
            Log.d(TAG, "load error!");
            e.printStackTrace();
        }
    }
}
