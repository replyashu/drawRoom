package com.b75f.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.b75f.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import static android.R.attr.left;
import static android.R.attr.right;
import static com.b75f.R.id.bottom;
import static com.b75f.R.id.top;

/**
 * Created by apple on 24/03/17.
 */

public class CustomView extends View {

    JSONArray data;

    String [] name;
    float [] x;
    float [] y;
    float [] w;
    float [] h;

    public CustomView(Context context, JSONArray floor_data) {
        super(context);
        this.data = floor_data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int len = data.length();

        initVals(len);

        for(int i = 0; i < len; i++){
            try {
                name[i] = data.getJSONObject(i).get("name").toString();
                JSONObject obj = (JSONObject)data.getJSONObject(i).get("position");
                x[i] = (float)obj.getDouble("x");
                y[i] = (float)obj.getDouble("y");
                w[i] = (float)obj.getDouble("width");
                h[i] = (float)obj.getDouble("height");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        drawCanvas(canvas, name, x, y, w, h, len);
    }

    private void initVals(int len){
        name = new String[len];
        x = new float[len];
        y = new float[len];
        w = new float[len];
        h = new float[len];

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawCanvas(Canvas c, String [] name, float [] x, float [] y, float [] w, float [] h, int len){

        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.YELLOW);
        myPaint.setStrokeWidth(2);

        Paint p =new Paint();
        Bitmap b= BitmapFactory.decodeResource(getResources(), R.drawable.back);
        b = Bitmap.createScaledBitmap(b, 780, 480, true);
        c.translate(getWidth()/2 - b.getWidth()/2, getHeight()/2 - b.getHeight()/2);
        c.drawBitmap(b, 0, 0, p);

        for(int i = 0; i < len; i++) {

            myPaint.setColor(Color.rgb(0, 0, 0));
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setColor(Color.argb(80,255,255,0));

            myPaint.setStrokeWidth(2);
            c.drawRect(x[i], y[i] , (x[i] + w[i]) ,(y[i] + h[i]) , myPaint);
            myPaint.setColor(Color.BLACK);
            myPaint.setStrokeWidth(1);
            c.drawText(name[i],x[i], y[i] + 10, myPaint);
        }
    }
}