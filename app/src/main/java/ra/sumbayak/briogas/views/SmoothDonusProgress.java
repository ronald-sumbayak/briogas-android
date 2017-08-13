package ra.sumbayak.briogas.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class SmoothDonusProgress extends DonutProgress {
    
    private int currentPercentage;
    
    public SmoothDonusProgress (Context context) {
        super (context);
    }
    
    public SmoothDonusProgress (Context context, AttributeSet attrs) {
        super (context, attrs);
    }
    
    public SmoothDonusProgress (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }
    
    public void setPercentage (final int percentage) {
        final int progress = percentage < currentPercentage ? -1 : 1;
        final Handler handler = new Handler ();
    
        new Thread (new Runnable () {
            @Override
            public void run () {
                while (currentPercentage != percentage) {
                    try { Thread.sleep (8); }
                    catch (InterruptedException e) { e.printStackTrace (); }
                
                    currentPercentage += progress;
                    
                    handler.post (new Runnable () {
                        @Override
                        public void run () {
                            setDonut_progress (String.valueOf (currentPercentage));
                        }
                    });
                }
            }
        }).start ();
    }
}
