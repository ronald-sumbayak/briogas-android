package ra.sumbayak.briogas;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    
    BaseActivity activity;
    
    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        activity = (BaseActivity) context;
    }
}
