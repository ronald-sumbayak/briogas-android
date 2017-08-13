package ra.sumbayak.briogas.views;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class ProgressiveTextView extends android.support.v7.widget.AppCompatTextView {
    
    private int currentValue;
    
    public ProgressiveTextView (Context context) {
        super (context);
    }
    
    public ProgressiveTextView (Context context, AttributeSet attrs) {
        super (context, attrs);
    }
    
    public ProgressiveTextView (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
    }
    
    public void setValue (final int value) {
        currentValue = Integer.parseInt (getText ().toString ());
        final int progress = value < currentValue ? -1 : 1;
        final Handler handler = new Handler ();
    
        new Thread (new Runnable () {
            @Override
            public void run () {
                while (currentValue != value) {
                    try { Thread.sleep (8); }
                    catch (InterruptedException e) { e.printStackTrace (); }
                
                    currentValue += progress;
                
                    handler.post (new Runnable () {
                        @Override
                        public void run () {
                            setText (String.valueOf (currentValue));
                        }
                    });
                }
            }
        }).start ();
    }
}
