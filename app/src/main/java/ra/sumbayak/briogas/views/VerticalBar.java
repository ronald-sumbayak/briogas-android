package ra.sumbayak.briogas.views;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class VerticalBar extends View {
    
    int maxHeight;
    int currentPercentage;
    
    public VerticalBar (Context context) {
        super (context);
    }
    
    public VerticalBar (Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
    }
    
    public VerticalBar (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }
    
    public void setPercentage (final int percentage) {
        if (maxHeight <= 0)
            maxHeight = getMeasuredHeight ();
    
        final int progress = percentage < currentPercentage ? -1 : 1;
        final Handler handler = new Handler ();
    
        new Thread (new Runnable () {
            @Override
            public void run () {
                while (currentPercentage != percentage) {
                    try { Thread.sleep (8); }
                    catch (InterruptedException e) { e.printStackTrace (); }
                
                    currentPercentage += progress;
                    int nextHeight = maxHeight * currentPercentage / 100;
                    final LinearLayout.LayoutParams params;
                    params = new LinearLayout.LayoutParams (getLayoutParams ().width, nextHeight);
                
                    handler.post (new Runnable () {
                        @Override
                        public void run () {
                            setLayoutParams (params);
                        }
                    });
                }
            }
        }).start ();
    }
}
