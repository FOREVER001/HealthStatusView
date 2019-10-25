package com.tianzhuan.healthstatusview;

public class CalcuateColorUtils {
    public static Object getCurrentColor(float fraction, Object startValue, Object endValue){
        int startInt = (Integer) startValue;
        //起始颜色ARGB颜色通道拆分
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >> 8) & 0xff) / 255.0f;
        float startB = (startInt & 0xff) / 255.0f;//透明度
        /*
         * 结束颜色ARGB颜色通道拆分
         */
        int endInt = (Integer) endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >> 8) & 0xff) / 255.0f;
        float endB = (endInt & 0xff) / 255.0f;//透明度




        // 颜色数值线性增加
        startR = (float) Math.pow(startR, 2.2);
        startG = (float) Math.pow(startG, 2.2);
        startB = (float) Math.pow(startB, 2.2);

        endR = (float) Math.pow(endR, 2.2);
        endG = (float) Math.pow(endG, 2.2);
        endB = (float) Math.pow(endB, 2.2);



        /*
         *
         */
        // 根据时间因子，计算出过渡的颜色插值
        float a = startA + fraction * (endA - startA);
        float r = startR + fraction * (endR - startR);
        float g = startG + fraction * (endG - startG);
        float b = startB + fraction * (endB - startB);

        // 再将颜色转换回ARGB[0，255]
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

        //将分离ARGB颜色通道打包装车
        return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
    }

    public static Object getCurrentColor(float fraction, Object startValue, Object middleValue,Object endValue){
        int startInt = (Integer) startValue;
        //起始颜色ARGB颜色通道拆分
        float startA = ((startInt >> 24) & 0xff) / 255.0f;
        float startR = ((startInt >> 16) & 0xff) / 255.0f;
        float startG = ((startInt >> 8) & 0xff) / 255.0f;
        float startB = (startInt & 0xff) / 255.0f;//透明度

        /*
         *中间颜色ARGB颜色通道拆分
         */
        int middleInt = (Integer) middleValue;
        float middleA = ((middleInt >> 24) & 0xff) / 255.0f;
        float middleR = ((middleInt >> 16) & 0xff) / 255.0f;
        float middleG = ((middleInt >> 8) & 0xff) / 255.0f;
        float middleB = (middleInt & 0xff) / 255.0f;//透明度


        // 颜色数值线性增加
        startR = (float) Math.pow(startR, 1.5);
        startG = (float) Math.pow(startG, 1.5);
        startB = (float) Math.pow(startB, 1.5);

        middleR = (float) Math.pow(middleR, 1.5);
        middleG = (float) Math.pow(middleG, 1.5);
        middleB = (float) Math.pow(middleB, 1.5);

        /*
         *
         */
        // 根据时间因子，计算出过渡的颜色插值
        float a = startA + fraction * (middleA - startA);
        float r = startR + fraction * (middleR - startR);
        float g = startG + fraction * (middleG - startG);
        float b = startB + fraction * (middleB - startB);

        // 再将颜色转换回ARGB[0，255]
        a = a * 255.0f;
        r = (float) Math.pow(r, 1.0 / 1.5) * 255.0f;
        g = (float) Math.pow(g, 1.0 / 1.5) * 255.0f;
        b = (float) Math.pow(b, 1.0 / 1.5) * 255.0f;

        //将分离ARGB颜色通道打包装车
        Object startValue1=Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);

        int startInt2 = (Integer) startValue1;
        //起始颜色ARGB颜色通道拆分
        float startA1 = ((startInt2 >> 24) & 0xff) / 255.0f;
        float startR1 = ((startInt2 >> 16) & 0xff) / 255.0f;
        float startG1 = ((startInt2 >> 8) & 0xff) / 255.0f;
        float startB1 = (startInt2 & 0xff) / 255.0f;//透明度


        /*
         * 结束颜色ARGB颜色通道拆分
         */
        int endInt = (Integer) endValue;
        float endA = ((endInt >> 24) & 0xff) / 255.0f;
        float endR = ((endInt >> 16) & 0xff) / 255.0f;
        float endG = ((endInt >> 8) & 0xff) / 255.0f;
        float endB = (endInt & 0xff) / 255.0f;//透明度




        // 颜色数值线性增加
        startR1 = (float) Math.pow(startR1, 1.5);
        startG1 = (float) Math.pow(startG1, 1.5);
        startB1 = (float) Math.pow(startB1, 1.5);

        endR = (float) Math.pow(endR, 1.5);
        endG = (float) Math.pow(endG, 1.5);
        endB = (float) Math.pow(endB, 1.5);



        /*
         *
         */
        // 根据时间因子，计算出过渡的颜色插值
        float a1 = startA1 + fraction * (endA - startA1);
        float r1 = startR1 + fraction * (endR - startR1);
        float g1 = startG1 + fraction * (endG - startG1);
        float b1 = startB1 + fraction * (endB - startB1);

        // 再将颜色转换回ARGB[0，255]
        a1 = a1 * 255.0f;
        r1 = (float) Math.pow(r1, 1.0 / 1.5) * 255.0f;
        g1 = (float) Math.pow(g1, 1.0 / 1.5) * 255.0f;
        b1 = (float) Math.pow(b1, 1.0 / 1.5) * 255.0f;

        //将分离ARGB颜色通道打包装车
        return Math.round(a1) << 24 | Math.round(r1) << 16 | Math.round(g1) << 8 | Math.round(b1);
    }
}
