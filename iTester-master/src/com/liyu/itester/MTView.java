package com.liyu.itester;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MTView extends SurfaceView implements SurfaceHolder.Callback{
	private static final int MAX_TOUCHPOINTS = 10;
    private Paint textPaint = new Paint();
    private Paint touchPaints[] = new Paint[MAX_TOUCHPOINTS];
    private int colors[] = new int[MAX_TOUCHPOINTS];
    private int width, height;
    private float scale = 1.0f;
    public  int count;
    public MTView(Context context,AttributeSet attrs) {
        super(context,attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true); // ȷ�����ǵ�View�ܻ�����뽹��
        setFocusableInTouchMode(true); // ȷ���ܽ��յ������¼�  
        init();
    }

    private void init() {
        // ��ʼ��10����ͬ��ɫ�Ļ���  
        textPaint.setColor(Color.WHITE);
        colors[0] = Color.BLUE;
        colors[1] = Color.RED;
        colors[2] = Color.GREEN;
        colors[3] = Color.YELLOW;
        colors[4] = Color.CYAN;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.DKGRAY;
        colors[7] = Color.WHITE;
        colors[8] = Color.LTGRAY;
        colors[9] = Color.GRAY;
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            touchPaints[i] = new Paint();
            touchPaints[i].setColor(colors[i]);
        }
    }
    /*  
     * �������¼�  
     */
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
        // �����Ļ��������
       int pointerCount = event.getPointerCount();
       if(count<pointerCount){count=pointerCount;}
       else{}
        Log.v("poinercount",String.valueOf(pointerCount));
        if (pointerCount > MAX_TOUCHPOINTS) {
            pointerCount = MAX_TOUCHPOINTS;
        }
        // ����Canvas,��ʼ������Ӧ�Ľ��洦��  
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
        	
            c.drawColor(Color.BLACK);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // �����뿪��Ļʱ������  
            c.drawText(String.valueOf(count)+" Points", 50, 50,textPaint);
            	count = 0;
            } else {
                // ������Ļ�ϻ�һ��ʮ�֣�Ȼ��һ��Բ  
            	//count=event.getPointerCount();
            	Log.v("count",String.valueOf(count));
                for (int i = 0; i < pointerCount; i++) {
                    // ��ȡһ����������꣬Ȼ��ʼ����  
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCrosshairsAndText(x, y, touchPaints[id], i, id, c);
                }
                for (int i = 0; i < pointerCount; i++) {
                    int id = event.getPointerId(i);
                    int x = (int) event.getX(i);
                    int y = (int) event.getY(i);
                    drawCircle(x, y, touchPaints[id], c);
                }
            }
            // �����unlock  
            getHolder().unlockCanvasAndPost(c);
        }
        return true;
    }
   

	/**  
     * ��ʮ�ּ�������Ϣ  
     * @param x  
     * @param y  
     * @param paint  
     * @param ptr  
     * @param id  
     * @param c  
     */  
    private void drawCrosshairsAndText(int x, int y, Paint paint, int ptr,int id, Canvas c) {
        c.drawLine(0, y, width, y, paint);
        c.drawLine(x, 0, x, height, paint);
        int textY = (int) ((15 + 20 * ptr) * scale);
        c.drawText("x" + ptr  + "=" + x, 10 * scale, textY,paint);
        c.drawText("y" + ptr  + "=" + y, 70 * scale, textY,paint);
        c.drawText("id" + ptr + "=" + id, width - 55 * scale,textY,paint);
    }
    /**  
     * ��Բ  
     * @param x  
     * @param y  
     * @param paint  
     * @param c  
     */  
    private void drawCircle(int x, int y, Paint paint, Canvas c){
        c.drawCircle(x, y, 40 * scale, paint);
    }
    /*  
     * �������ʱ�������ɺ�ɫ��Ȼ��ѡ�START_TEXT��д����Ļ  
     */  
    public void surfaceChanged(SurfaceHolder holder, int format,int Width, int Height) {
    	width = Width;
        height = Height;
        if (Width > Height) {
            this.scale = Width / 480f;
        } else {
            this.scale = Height / 480f;
        }
        textPaint.setTextSize(14 * scale);
        Canvas c = getHolder().lockCanvas();
        if (c != null) {
            // ������ɫ
            c.drawColor(Color.BLACK);
            float tWidth = textPaint.measureText(getResources().getString(R.string.label_multitouch_tips));
            c.drawText(getResources().getString(R.string.label_multitouch_tips), Width / 2 - tWidth / 2 ,Height/2, textPaint);
            getHolder().unlockCanvasAndPost(c);
        }
    }
    public void surfaceCreated(SurfaceHolder holder) {
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    public String getcounts()
    {
    	return String.valueOf(count);
    }
}
