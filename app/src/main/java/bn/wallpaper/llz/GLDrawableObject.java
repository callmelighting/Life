package bn.wallpaper.llz;

import android.opengl.Matrix;

// 每个需要绘制的物体必须用这个类包起来并添加到组中
public class GLDrawableObject {
    private GLDrawable glDrawable;      // GLDrawable对象
    private int textureID;              // 纹理ID
    private float[] selfMatrix;         // 具体变换矩阵
    private boolean drawShadow;         // 是否绘制影子
    private float[] shadowColor;        // 阴影颜色值
    private boolean touchable;          // 是否可触摸
    private float[] positionAdjustment; // 位置调节数组
    private float[] target;             // 目标点
    private float moveStep;             // 移动步长
    private boolean move;               // 是否移动
    private float rotateStep;           // 旋转步长
    private boolean rotate;             // 是否旋转
    private boolean drawable;           // 是否可画
    private boolean visible;            // 是否可见
    private boolean moveStart;          // 是否刚开始移动
    private boolean rotateStart;        // 是否刚开始旋转
    private GLDrawableAction action;    // 自身行为

    // 构造函数
    public GLDrawableObject(GLDrawable glDrawable, float[] selfMatrix, int textureID) {
        if (glDrawable != null && selfMatrix != null) {
            this.glDrawable = glDrawable;
            this.selfMatrix = selfMatrix.clone();
            this.textureID = textureID;

            shadowColor = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
            positionAdjustment = new float[]{0f, 0f, 0f};
            drawable = true;
            visible = true;
            moveStart = true;
            rotateStart = true;
        }
    }

    // 构造函数
    public GLDrawableObject(GLDrawable glDrawable, float[] selfMatrix) {
        this(glDrawable, selfMatrix, -1);
    }

    // 绘制
    public void draw() {
        if (drawable) {
            drawStart();
            if (visible) {
                glDrawable.drawSelf(textureID, selfMatrix, drawShadow, shadowColor);
            }
            if (move) move();
            if (rotate) rotate();
            drawEnd();
        }
    }

    // 移动
    private void move() {
        // 若移动目标为null或移动步长为0, 则返回
        if (target == null || moveStep == 0) return;
        // 若这是第一次移动, 则调用moveStart()方法
        if (moveStart) moveStart();

        // 获取物体现在在世界坐标系中的位置
        float[] nowPosition = getWorldPosition();
        // 获取物体现在的位置到目标点位置的向量
        float[] totalVector = new float[]{
                target[0] - nowPosition[0],
                0f,
                target[2] - nowPosition[2]
        };

        // 若现在的位置与目标位置相同, 则无需计算, 且计算下去会产生NaN, 返回
        if (totalVector[0] == 0 && totalVector[2] == 0) return;

        // 计算到目标点的总距离
        float totalDistance = Matrix.length(totalVector[0], totalVector[1], totalVector[2]);
        // 这次移动的距离
        float thisDistance;
        // 若剩余的总距离大于步长, 则这次移动的距离等于步长,
        // 否则这次移动的距离等于剩余的总距离, 并调用moveEnd()和rotateEnd()方法.
        // 为什么还要调用rotateEnd()呢? 因为物体一旦移动到目标点, 那么物体与目标点之间的
        // 向量则不复存在, 那么物体旋转的方法自然也就无法正常执行了
        if (totalDistance > moveStep) {
            thisDistance = moveStep;
        } else {
            thisDistance = totalDistance;
            moveEnd();
            rotateEnd();
        }
        // 这次的位置到下一步位置之间的的向量
        float[] thisVector = new float[]{
                totalVector[0] / totalDistance * thisDistance,
                0f,
                totalVector[2] / totalDistance * thisDistance
        };

        // 临时变换矩阵, 初始化为单位矩阵
        float[] tempM = ManagerForMatrix.getIdentityM();
        // 临时结果矩阵
        float[] newM = new float[16];
        // 根据thisVector移动临时变换矩阵
        Matrix.translateM(tempM, 0, thisVector[0], thisVector[1], thisVector[2]);
        // 将临时变换矩阵与自身具体变换矩阵相乘, 并将结果放入临时结果矩阵中
        Matrix.multiplyMM(newM, 0, tempM, 0, selfMatrix, 0);

        // 有些意料之外的计算会产生NaN, 为了防止模型突然消失, 返回
        if (Float.isNaN(newM[12])) return;

        // 将自身具体变换矩阵更新为临时结果矩阵
        selfMatrix = newM;
    }

    // 旋转
    private void rotate() {
        // 若旋转目标为null或旋转步长为0, 则返回
        if (target == null || rotateStep == 0) return;
        // 若这是第一次旋转, 则调用rotateStart()方法
        if (rotateStart) rotateStart();

        // 获取物体现在在世界坐标系中的位置
        float[] nowPosition = getWorldPosition();
        // 获取物体现在的位置到目标点位置的向量
        float[] totalVector = new float[]{
                target[0] - nowPosition[0],
                0f,
                target[2] - nowPosition[2]
        };

        // 若现在的位置与目标位置相同, 则无需计算, 且计算下去会产生NaN, 返回
        if (totalVector[0] == 0 && totalVector[2] == 0) return;

        // 获取物体原本的朝向
        float[] originalVector = glDrawable.getDirection();
        // 下面的代码与着色器中变换法向量的算法类似
        float[] tempV1 = new float[]{
                originalVector[0] + nowPosition[0],
                0f,
                originalVector[2] + nowPosition[2],
                1f
        };
        float[] tempV2 = new float[]{
                nowPosition[0],
                0f,
                nowPosition[2],
                1f
        };
        float[] newV1 = new float[4];
        float[] newV2 = new float[4];
        Matrix.multiplyMV(newV1, 0, selfMatrix, 0, tempV1, 0);
        Matrix.multiplyMV(newV2, 0, selfMatrix, 0, tempV2, 0);
        // 获取物体在经过具体变换矩阵变换后的朝向
        float[] thisVector = new float[]{
                newV1[0] - newV2[0],
                0f,
                newV1[2] - newV2[2]
        };

        // 获取到目标点的总角度
        float totalAngle = UtilForVector.angle(
                thisVector[0], thisVector[1], thisVector[2],
                totalVector[0], totalVector[1], totalVector[2]
        );

        // 这次旋转的角度
        float thisAngle;
        // 若剩余的总角度大于步长, 则这次旋转的角度等于步长,
        // 否则这次旋转的角度等于剩余的总角度, 并调用rotateEnd()方法.
        if (totalAngle > rotateStep) {
            thisAngle = rotateStep;
        } else {
            thisAngle = totalAngle;
            rotateEnd();
        }
        // 获取旋转轴
        float[] axis = UtilForVector.cross(
                thisVector[0], thisVector[1], thisVector[2],
                totalVector[0], totalVector[1], totalVector[2]
        );

        // 若旋转轴为(0,0,0), 则计算下去会产生NaN, 返回
        if (axis[0] == 0 && axis[1] == 0 && axis[2] == 0) return;

        // 临时变换矩阵, 初始化为单位矩阵
        float[] tempM1 = ManagerForMatrix.getIdentityM();
        float[] tempM2 = ManagerForMatrix.getIdentityM();
        float[] tempM3 = ManagerForMatrix.getIdentityM();
        // 临时结果矩阵
        float[] newM1 = new float[16];
        float[] newM2 = new float[16];
        float[] newM3 = new float[16];

        // 根据物体现在的位置将临时变换矩阵推回原点并旋转, 再推回来
        Matrix.translateM(tempM1, 0, -nowPosition[0], -nowPosition[1], -nowPosition[2]);
        Matrix.rotateM(tempM2, 0, (float) Math.toDegrees(thisAngle), axis[0], axis[1], axis[2]);
        Matrix.translateM(tempM3, 0, nowPosition[0], nowPosition[1], nowPosition[2]);

        Matrix.multiplyMM(newM1, 0, tempM2, 0, tempM1, 0);
        Matrix.multiplyMM(newM2, 0, tempM3, 0, newM1, 0);
        Matrix.multiplyMM(newM3, 0, newM2, 0, selfMatrix, 0);

        // 有些意料之外的计算会产生NaN, 为了防止模型突然消失, 返回
        if (Float.isNaN(newM3[12])) return;

        // 将自身具体变换矩阵更新为临时结果矩阵
        selfMatrix = newM3;
    }

    // 绘画开始时执行此方法
    private void drawStart() {
        if (action != null) {
            action.drawStart(this);
        }
    }

    // 移动开始时执行此方法
    private void moveStart() {
        moveStart = false;
        if (action != null) {
            action.moveStart(this);
        }
    }

    // 旋转开始时执行此方法
    private void rotateStart() {
        rotateStart = false;
        if (action != null) {
            action.rotateStart(this);
        }
    }

    // 绘画结束时执行此方法
    private void drawEnd() {
        if (action != null) {
            action.drawEnd(this);
        }
    }

    // 移动结束时执行此方法
    private void moveEnd() {
        move = false;
        moveStart = true;
        if (action != null) {
            action.moveEnd(this);
        }
    }

    // 旋转结束时执行此方法
    private void rotateEnd() {
        rotate = false;
        rotateStart = true;
        if (action != null) {
            action.rotateEnd(this);
        }
    }

    // 被触摸时执行此方法
    public void beTouched(float x, float y, float z) {
        if (action != null) {
            action.beTouched(this, x, y, z);
        }
    }

    // 获取射线起始点与物体的相交点之间的距离
    public float getIntersectionT(float[] ray) {
        // 若物体不可触摸或没有AABB包围盒, 则返回正无穷
        // 否则将世界坐标系中的射线转换为物体坐标系中的射线
        // 然后使用新射线与物体的AABB包围盒进行相交计算
        if (!touchable || glDrawable.getAABB() == null) {
            return Float.POSITIVE_INFINITY;
        } else {
            float[] oldRayStart = new float[]{ray[0], ray[1], ray[2]};
            float[] oldRayEnd = new float[]{ray[3], ray[4], ray[5]};
            float[] newRayStart = ManagerForMatrix.fromGToO(oldRayStart, selfMatrix);
            float[] newRayEnd = ManagerForMatrix.fromGToO(oldRayEnd, selfMatrix);
            return glDrawable.getAABB().getIntersectionT(newRayStart, newRayEnd);
        }
    }

    public void setGlDrawable(GLDrawable glDrawable) {
        this.glDrawable = glDrawable;
    }

    public GLDrawable getGlDrawable() {
        return glDrawable;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setSelfMatrix(float[] selfMatrix) {
        System.arraycopy(selfMatrix, 0, this.selfMatrix, 0, selfMatrix.length);
    }

    public float[] getSelfMatrix() {
        return selfMatrix;
    }

    public void setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
    }

    public boolean getDrawShadow() {
        return drawShadow;
    }

    public void setShadowColor(float r, float g, float b, float a) {
        shadowColor[0] = r;
        shadowColor[1] = g;
        shadowColor[2] = b;
        shadowColor[3] = a;
    }

    public float[] getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(float[] shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean getTouchable() {
        return touchable;
    }

    public void setPositionAdjustment(float x, float y, float z) {
        positionAdjustment[0] = x;
        positionAdjustment[1] = y;
        positionAdjustment[2] = z;
    }

    public float[] getPositionAdjustment() {
        return positionAdjustment;
    }

    // 获取自身在世界坐标系中的坐标
    public float[] getWorldPosition() {
        // 首先获取物体的初始位置, 加上位置调节参数
        // 然后与具体变换矩阵计算获取自身在世界坐标系中的坐标
        float[] originalPosition = glDrawable.getPosition();
        float[] oldPosition = new float[]{
                originalPosition[0] + positionAdjustment[0],
                originalPosition[1] + positionAdjustment[1],
                originalPosition[2] + positionAdjustment[2],
                1f
        };
        float[] newPosition = new float[4];
        Matrix.multiplyMV(newPosition, 0, selfMatrix, 0, oldPosition, 0);
        return new float[]{newPosition[0], newPosition[1], newPosition[2]};
    }

    public void setTarget(float x, float y, float z) {
        if (target == null) {
            target = new float[3];
        }
        target[0] = x;
        target[1] = y;
        target[2] = z;
    }

    public float[] getTarget() {
        return target;
    }

    public void setMoveStep(float moveStep) {
        this.moveStep = moveStep;
    }

    public float getMoveStep() {
        return moveStep;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public boolean getMove() {
        return move;
    }

    public void setRotateStep(float rotateStep) {
        this.rotateStep = rotateStep;
    }

    public float getRotateStep() {
        return rotateStep;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public boolean getRotate() {
        return rotate;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    public boolean getDrawable() {
        return drawable;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setAction(GLDrawableAction action) {
        this.action = action;
    }

    public GLDrawableAction getAction() {
        return action;
    }
}
