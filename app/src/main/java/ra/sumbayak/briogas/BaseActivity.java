package ra.sumbayak.briogas;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

public class BaseActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }
    
    public void showProgressBar (@IdRes int id) {
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById (id);
        avi.smoothToShow ();
    }
    
    public void showProgressBar (@IdRes int id, @IdRes int hide) {
        findViewById (hide).setVisibility (View.INVISIBLE);
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById (id);
        avi.smoothToShow ();
    }
    
    public void dismissProgressBar (@IdRes int id) {
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById (id);
        avi.smoothToHide ();
    }
    
    public void dismissProgressBar (@IdRes int id, @IdRes int hide) {
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById (id);
        avi.smoothToHide ();
        findViewById (hide).setVisibility (View.VISIBLE);
    }
}
