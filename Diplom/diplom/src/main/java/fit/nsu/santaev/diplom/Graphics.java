package fit.nsu.santaev.diplom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Graphics extends View {

	private List<Double> values = new LinkedList<Double>();
	private List<Long> times = new LinkedList<Long>();
	private long secondsOnView = 10 * 1000;
	private long last = 0;
	private double lineVal = 0;

	public void setLine(double val) {
		lineVal = val;
	}

	public Graphics(Context context) {
		super(context);
	}

	public Graphics(Context context, AttributeSet attrs) {
		super(context, attrs);
		start();
	}

	public void start() {
		last = System.currentTimeMillis();
	}

	public void add(double val, Long time) {
		synchronized (values) {
			values.add(0, val);
			times.add(0, time);
		}
	}

	public void add(double val) {
		synchronized (values) {
			values.add(0, val);
			times.add(0, System.currentTimeMillis() - last);
			last = System.currentTimeMillis();

            double sum = 0;
            for (int i = 0; i < times.size(); i++){
                sum += times.get(i);
                if (sum > secondsOnView){
                    times = times.subList(0, i);
                    values = values.subList(0, i);
                    return;
                }
            }
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (values.isEmpty()) {
			return;
		}
		int w = getWidth();
		int h = getHeight();
		long sum = 0;
		double max = getMax();
		if (max < lineVal) {
			max = lineVal;
		}
		double lastY;
		synchronized (values) {
			lastY = values.get(0);
		}
		double lastX = 0;
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		int secs = (int) (secondsOnView / 1000);
		for (int i = 0; i < secs; i++) {
			canvas.drawLine(w / secs * i, 0, w / secs * i, h, paint);
		}
		paint.setColor(Color.RED);
		synchronized (values) {
			for (int i = 0; i < values.size(); i++) {
				double val = values.get(i);
				canvas.drawLine((float) (lastX), h - (float) (lastY),
						(float) ((float) w / secondsOnView * sum), h
								- (float) ((float) h / max * 3 / 4 * val),
						paint);
				if (lastX > w) {
					synchronized (values) {
						values = values.subList(0, i);
						times = times.subList(0, i);
					}
					break;
				}
				lastX = (float) w / secondsOnView * sum;
				lastY = (float) h / max * 3 / 4 * val;
				synchronized (values) {
					sum += times.get(i);
				}
			}
		}

		paint.setColor(Color.WHITE);
		if ((float) 3 / 4 * lineVal / max * h > h) {
			canvas.drawLine(0, 1, getWidth(), 1, paint);
		} else {
			canvas.drawLine(0, h - (float) ((float) 3 / 4 * h * lineVal / max),
					getWidth(),
					h - (float) ((float) 3 / 4 * h * lineVal / max), paint);
		}
		if (values.size() > 0) {
			// Log.e("myLogs", "Gr " + lineVal + " " + values.get(0));
		}
	}

	private double getMax() {
		double max = 0;
		long sum = 0;
		synchronized (values) {
			for (int i = 0; i < values.size(); i++) {
				double val = values.get(i);
				sum += times.get(i);
				if (val > max) {
					max = val;
				}
				if (sum > secondsOnView) {
					break;
				}
			}
		}
		return max;
	}

}
