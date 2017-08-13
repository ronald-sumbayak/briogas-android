package ra.sumbayak.briogas.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import butterknife.*;
import ra.sumbayak.briogas.BaseActivity;
import ra.sumbayak.briogas.R;
import ra.sumbayak.briogas.api.ApiInterface;
import ra.sumbayak.briogas.api.QRCallback;
import ra.sumbayak.briogas.api.models.DeviceData;
import ra.sumbayak.briogas.views.ProgressiveTextView;
import ra.sumbayak.briogas.views.SmoothDonusProgress;
import ra.sumbayak.briogas.views.VerticalBar;
import retrofit2.Callback;

import static ra.sumbayak.briogas.Constant.SPNAME;

public class MainActivity extends BaseActivity {
    
    @BindView (R.id.methane) SmoothDonusProgress methane;
    @BindView (R.id.pressure) VerticalBar pressure;
    @BindView (R.id.temperature) ProgressiveTextView temperature;
    @BindView (R.id.content) ProgressiveTextView content;
    @BindView (R.id.refresh) Button refresh;
    @BindView (R.id.screen_wake) CheckBox screenWake;
    private Callback<DeviceData> callback;
    private Handler handler;
    private Runnable runnable;
    private boolean exitPending, autoUpdate, refreshed;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);
        
        handler = new Handler ();
        runnable = new Runnable () {
            @Override
            public void run () {
                ApiInterface.Builder
                    .build (MainActivity.this)
                    .retrieve ()
                    .enqueue (callback);
            }
        };
        
        callback = new QRCallback<DeviceData> () {
            @Override
            protected void onSuccessful (@NonNull DeviceData body) {
                setNewData (body);
            }
    
            @Override
            protected void onExit () {
                dismissProgressBar (R.id.refresh_loading, R.id.refresh);
                if (autoUpdate)
                    handler.postDelayed (runnable, 5000);
            }
        };
    }
    
    @Override
    protected void onStart () {
        super.onStart ();
        showProgressBar (R.id.data_initial_loading, R.id.data);
        ApiInterface.Builder
            .build (this)
            .retrieve ()
            .enqueue (new QRCallback<DeviceData> () {
                @Override
                protected void onSuccessful (@NonNull DeviceData body) {
                    setNewData (body);
                    // handler.postDelayed (runnable, 5000);
                    dismissProgressBar (R.id.data_initial_loading, R.id.data);
                    if (autoUpdate)
                        handler.postDelayed (runnable, 5000);
                }
                
                @Override
                protected void onUnauthorized () {
                    logout ();
                }
            });
    }
    
    @Override
    protected void onResume () {
        super.onResume ();
        if (screenWake.isChecked ())
            getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    @Override
    protected void onPause () {
        super.onPause ();
        if (screenWake.isChecked ())
            getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    @Override
    protected void onStop () {
        super.onStop ();
        showProgressBar (R.id.data_initial_loading, R.id.data);
        handler.removeCallbacks (runnable);
    }
    
    @OnCheckedChanged (R.id.screen_wake)
    public void changeScreenWakeState (boolean checked) {
        if (checked)
            getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
    @SuppressLint ("SetTextI18n")
    @OnClick (R.id.refresh)
    public void refreshData () {
        if (autoUpdate) {
            refresh.setText ("Refresh");
            autoUpdate = false;
        }
        else {
            if (!refreshed)
                Toast.makeText (this, "Hold refresh button for Auto-update", Toast.LENGTH_SHORT).show ();
            showProgressBar (R.id.refresh_loading, R.id.refresh);
            handler.postDelayed (runnable, 5000);
            refreshed = true;
        }
    }
    
    @SuppressLint ("SetTextI18n")
    @OnLongClick (R.id.refresh)
    public boolean changeRefreshMode () {
        if (!autoUpdate) {
            refresh.setText ("STOP");
            autoUpdate = true;
        }
        
        refreshed = true;
        handler.postDelayed (runnable, 5000);
        return true;
    }
    
    private void setNewData (DeviceData data) {
        methane.setPercentage (data.methane);
        pressure.setPercentage (data.pressure);
        temperature.setValue (data.temperature);
        content.setValue (data.content);
    }
    
    private void logout () {
        getSharedPreferences (SPNAME, MODE_PRIVATE)
            .edit ()
            .clear ()
            .apply ();
        startActivity (new Intent (this, AuthActivity.class));
        finish ();
    }
    
    @Override
    public void onBackPressed () {
        if (exitPending)
            super.onBackPressed ();
        else {
            exitPending = true;
            Toast.makeText (MainActivity.this, "Press back again to exit.", Toast.LENGTH_SHORT).show ();
            
            new Handler ().postDelayed (
                new Runnable () {
                    @Override
                    public void run () {
                        exitPending = false;
                    }
                },
                2500
            );
        }
    }
}
