package com.tianzhuan.healthstatusview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆盘进度条
 */
public class HealthStatusView extends View {
    private Paint bgProgressPaint;//背景画笔
    private Paint drawwProgressPaint;//进度画笔
    private Paint  goodPaint;//优秀画笔
    private Paint  goodbgPaint;//优秀背景画笔
    private Paint scorePaint;//分值画笔
    private Paint scoreTitlePaint;//"分"画笔
    private Paint healthPaint; //健康指数


    private int pointerLenth;//刻度长度
    private int pointerWidth;//刻度宽度
    private int evaluateSize;//"优秀"字体大小
    private int scoreSize;//分值字体大小
    private int socreUnitSize;//"分"字体大小
    private int healthIndexSize;//健康指数字体大小
    private int evaluateBgColor;//评价的背景色，如优秀的背景色
    private int titleColor;//优秀，100分颜色值
    private int healthIndexColor;//健康指数颜色
    private int colors[]={Color.parseColor("#FF3B79"),
            Color.parseColor("#3C96EC"),
            Color.parseColor("#52F4F7")};//渐变颜色数组


    private int centX,centY,radius
            ;
    private int currentScore=100;//当前进度值
    private int refreshCurrentPoints=0;//当前进度值
    private   ValueAnimator valueAnimator;
    private boolean isFinishAnim=true;//当前动画是否已经结束


    public HealthStatusView(Context context) {
        this(context,null);
    }

    public HealthStatusView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HealthStatusView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HealthStatusView);
        //刻度的长度
        pointerLenth= (int) array.getDimension(R.styleable.HealthStatusView_pointer_length,dp_px(20));
        //刻度的宽度
        pointerWidth= (int) array.getDimension(R.styleable.HealthStatusView_pointer_width,dp_px(6));
        //"优秀"字体大小
        evaluateSize= (int) array.getDimension(R.styleable.HealthStatusView_evaluate_size,sp_px(12));
        //分值的字体大小
        scoreSize= (int) array.getDimension(R.styleable.HealthStatusView_score_size,sp_px(12));
        socreUnitSize= (int) array.getDimension(R.styleable.HealthStatusView_score_unit_size,12);
        //健康指数字体大小
        healthIndexSize= (int) array.getDimension(R.styleable.HealthStatusView_health_index_size,sp_px(12));
        //评价的背景色，如优秀的背景色
        evaluateBgColor=array.getColor(R.styleable.HealthStatusView_evaluate_background_color,Color.GRAY);
        //优秀，100分颜色值-
        titleColor=array.getColor(R.styleable.HealthStatusView_title_colr,Color.WHITE);
        //健康指数颜色
        healthIndexColor=array.getColor(R.styleable.HealthStatusView_health_index_color,Color.GRAY);
        array.recycle();
        init();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int width,height;
        if(wMode == MeasureSpec.EXACTLY){
            width = wSize;
        }else{
            width=Math.min(wSize,dp_px(100));
        }
        if(hMode == MeasureSpec.EXACTLY){
            height = hSize;
        }else{
            height=Math.min(hSize,dp_px(100));
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centX=w/2;
        centY=h/2;
        radius =Math.min(w,h)/2;
    }

    private void init() {
        bgProgressPaint=new Paint();
        bgProgressPaint.setAntiAlias(true);
        bgProgressPaint.setColor(Color.parseColor("#cccccc"));
        bgProgressPaint.setStyle(Paint.Style.STROKE);
        bgProgressPaint.setStrokeWidth(pointerWidth);

        drawwProgressPaint=new Paint();
        drawwProgressPaint.setAntiAlias(true);
        drawwProgressPaint.setColor(Color.parseColor("#ff00ff"));
        drawwProgressPaint.setStyle(Paint.Style.STROKE);
        drawwProgressPaint.setStrokeWidth(pointerWidth);


        goodPaint=new Paint();
        goodPaint.setColor(titleColor);
        goodPaint.setTextSize(evaluateSize);
        goodPaint.setAntiAlias(true);

        goodbgPaint=new Paint();
        goodbgPaint.setColor(evaluateBgColor);
        goodbgPaint.setAntiAlias(true);
        goodbgPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        scorePaint=new Paint();
        scorePaint.setColor(titleColor);
        scorePaint.setTextSize(scoreSize);
        scorePaint.setAntiAlias(true);
        scorePaint.setStyle(Paint.Style.STROKE);


        scoreTitlePaint=new Paint();
        scoreTitlePaint.setColor(titleColor);
        scoreTitlePaint.setTextSize(socreUnitSize);
        scoreTitlePaint.setAntiAlias(true);
        scoreTitlePaint.setStyle(Paint.Style.STROKE);

        healthPaint=new Paint();
        healthPaint.setColor(healthIndexColor);
        healthPaint.setTextSize(healthIndexSize);
        healthPaint.setAntiAlias(true);
        healthPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制底层圆弧表盘
        canvas.save();
        drawArcBackground(canvas);
        canvas.restore();
        canvas.save();
        drawArcProgress(canvas);
        canvas.restore();
        //绘制上层渐变色圆盘（带动画）
        //绘制中间的文字
        drawCenterText(canvas);
    }

    /**
     * 绘制中心文字
     * @param canvas
     */
    private void drawCenterText(Canvas canvas) {
        // 绘制优秀
        String str1="一般";
        Rect rect=new Rect();
        goodPaint.getTextBounds(str1,0,str1.length(),rect);
        int str1_width=rect.width();
        canvas.save();
        canvas.translate(centX-str1_width/2,centY-radius
                +pointerLenth+dp_px(30));
        int x1=rect.left-dp_px(10);
        int y1=rect.top-dp_px(4);
        int x2=rect.right+dp_px(10);
        int y2=rect.bottom+dp_px(4);
        RectF ovalRect = new RectF(x1,y1,x2,y2);
        canvas.drawRoundRect(ovalRect,dp_px(20),dp_px(20),goodbgPaint);
        canvas.restore();
        canvas.drawText(str1,centX-str1_width/2,centY-radius
                +pointerLenth+dp_px(30),goodPaint);

       //分值绘制
        String str88=String.valueOf(refreshCurrentPoints*4).trim();
        float textWidth = scorePaint.measureText(str88);
        Paint.FontMetrics fontMetrics = scorePaint.getFontMetrics();
        //字绘制以baseline为标准，我们将baseline设置为getHeight()/2后，
        // 文字势必会往上偏，所以我们要想让文字在竖直方向上居中，baseline需要往下一点。
        float y = centY + (Math.abs(fontMetrics.ascent) - fontMetrics.descent)/2;
        canvas.drawText(str88,centX-(textWidth)/2,y+dp_px(10),scorePaint);

        //"分"绘制
        String strScore="分";
       canvas.drawText(strScore,centX+textWidth/2+dp_px(2),y+dp_px(10),scoreTitlePaint);
        //"健康指数"绘制
        String strHealthScore="健康指数";
        Rect rectHealthScore=new Rect();
        healthPaint.getTextBounds(strHealthScore,0,strHealthScore.length(),rectHealthScore);
        int health_width=rectHealthScore.width();

        canvas.drawText(strHealthScore,centX-health_width/2,centY+health_width/2+dp_px(20),healthPaint);
    }

    /**
     * 绘制进度条
     * @param canvas
     */
    private void drawArcProgress(Canvas canvas) {
        canvas.rotate(-135,centX,centX);
        for (int i = 0; i < refreshCurrentPoints; i++) {
            float percentColor=i/25.0f;
            int currentColor= (int) CalcuateColorUtils.getCurrentColor(percentColor,colors[0],
                    colors[1],
                    colors[2]);
            drawwProgressPaint.setColor(currentColor);
            canvas.drawLine(centX,  centY-(radius-pointerLenth), centX,  -radius+centY
                    , drawwProgressPaint);
            canvas.rotate(11.2f,centX,centY);
        }
    }


    /**
     * 绘制弧形表盘
     * @param canvas
     */
    private void drawArcBackground(Canvas canvas) {
        canvas.rotate(-135,centX,centX);
        for (int i = 0; i < 25; i++) {
            canvas.drawLine(centX,  centY-(radius-pointerLenth), centX,  -radius+centY
                    , bgProgressPaint);
            canvas.rotate(11.2f,centX,centY);
        }

    }


    /**
     * dp转px
     *
     * @param values
     * @return
     */
    public int dp_px(int values)
    {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }
    /**
     * sp转px
     *
     * @param spValue
     * @return
     */
    public  int sp_px( float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 监听
     */
    public void refreshView(){
        int currentPoints=(currentScore*25)/100;
         valueAnimator = ValueAnimator.ofInt(0, currentPoints);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                refreshCurrentPoints= (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        if(isFinishAnim){
            valueAnimator.start();
        }


        //动画状态监听
        valueAnimator.addListener(new Animator.AnimatorListener() {
            /**
             * 动画开始监听
             * @param animation
             */
            @Override
            public void onAnimationStart(Animator animation) {
               isFinishAnim=false;
            }

            /**
             * 动画结束监听
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animator animation) {
                isFinishAnim=true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });

    }


    /***
     * 设置当前的分数
     */

    public void setProgress(int score){
        this.currentScore=score;
        refreshView();
    }

    public void setPointerLenth(int pointerLenth) {
        this.pointerLenth = pointerLenth;
    }

    public void setPointerWidth(int pointerWidth) {
        this.pointerWidth = pointerWidth;
    }

    public void setEvaluateSize(int evaluateSize) {
        this.evaluateSize = evaluateSize;
    }

    public void setScoreSize(int scoreSize) {
        this.scoreSize = scoreSize;
    }

    public void setSocreUnitSize(int socreUnitSize) {
        this.socreUnitSize = socreUnitSize;
    }

    public void setHealthIndexSize(int healthIndexSize) {
        this.healthIndexSize = healthIndexSize;
    }

    public void setEvaluateBgColor(int evaluateBgColor) {
        this.evaluateBgColor = evaluateBgColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public void setHealthIndexColor(int healthIndexColor) {
        this.healthIndexColor = healthIndexColor;
    }

    public void onStopAnim(){
        if(valueAnimator!=null){
            valueAnimator.cancel();
            valueAnimator=null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onStopAnim();
    }
}
