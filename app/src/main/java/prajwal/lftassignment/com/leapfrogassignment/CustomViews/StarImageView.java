package prajwal.lftassignment.com.leapfrogassignment.CustomViews;

/**
 * Created by prajwal on 7/17/2017.
 */


        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.PorterDuff;
        import android.graphics.Region;
        import android.support.annotation.Nullable;
        import android.util.AttributeSet;
        import android.widget.ImageView;

/**
 * Created by prajwal on 7/13/2017.
 */

public class StarImageView extends android.support.v7.widget.AppCompatImageView {
    private static final int STAR_OPP_ANGLE = 72;
    private static final int STAR_ANGLE = 36;
    private static final int STAR_ANGLE_HALF = 18;
    private Path path;
    private int paddingTop = 5;
    private Paint mPaint;

    public StarImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        init();

    }


    public StarImageView(Context context) {
        super(context);
        path = new Path();
        init();}

    private void init() {
        mPaint=new Paint();
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStyle(Paint.Style.STROKE);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.clipPath(path, Region.Op.INTERSECT);
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        super.onDraw(canvas);


    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        calculatePath(Math.min(width / 2f, height / 2f) - 10f);
    }

    private void calculatePath(float v) {
        int minDim = Math.min(getMeasuredWidth()-20,
                getMeasuredHeight()-20);

       /* int minDim = Math.min(160,
                160);*/

        // b = |
        // a = _
        // hyp = \

        // bigHypot = height / cos(18)
        double bigHypot = (minDim / Math.cos(Math.toRadians(STAR_ANGLE_HALF)));
        double bigB = minDim;
        double bigA = Math.tan(Math.toRadians(18)) * bigB;

        // lengths of the little triangles.
        // littleC = littleC + littleC + littleA + littleA
        // cos(72)*C = A
        double littleHypot = bigHypot / (2 + Math.cos(Math.toRadians(STAR_OPP_ANGLE)) + Math.cos(Math.toRadians(STAR_OPP_ANGLE)));
        double littleA = Math.cos(Math.toRadians(STAR_OPP_ANGLE)) * littleHypot;
        double littleB = Math.sin(Math.toRadians(STAR_OPP_ANGLE)) * littleHypot;

        int topXPoint = (getMeasuredWidth()) / 2;
        int topYPoint = paddingTop;

        // start at the top point
        path.moveTo(topXPoint, topYPoint);

        // top to bottom right point
        path.lineTo((int) (topXPoint + bigA), (int) (topYPoint + bigB));

        // bottom right to middle left point
        path.lineTo((int) (topXPoint - littleA - littleB), (int) (topYPoint + littleB));

        // middle left to middle right point
        path.lineTo((int) (topXPoint + littleA + littleB), (int) (topYPoint + littleB));

//		// middle right to bottom left point
        path.lineTo((int) (topXPoint - bigA), (int) (topYPoint + bigB));

//		// bottom left to top point
        path.lineTo(topXPoint, topYPoint);
        path.close();
        invalidate();
    }


}
