package ra.sumbayak.briogas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.briogas.BaseActivity;
import ra.sumbayak.briogas.R;
import ra.sumbayak.briogas.api.ApiInterface;
import ra.sumbayak.briogas.api.QRCallback;
import ra.sumbayak.briogas.api.models.DeviceData;
import retrofit2.Callback;

import static ra.sumbayak.briogas.Constant.SPNAME;

public class MainActivity extends BaseActivity {
    
    @BindView (R.id.toggle_katup) Button btnToggleKatup;
    @BindView (R.id.methane) DonutProgress methane;
    @BindView (R.id.oxygen) DonutProgress oxygen;
    @BindView (R.id.other) DonutProgress other;
    private Callback<DeviceData> callback;
    private Handler handler;
    private Runnable runnable;
    
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
                    handler.postDelayed (runnable, 5000);
                    dismissProgressBar (R.id.data_initial_loading, R.id.data);
                }
                
                @Override
                protected void onUnauthorized () {
                    logout ();
                }
            });
    }
    
    @Override
    protected void onStop () {
        super.onStop ();
        showProgressBar (R.id.data_initial_loading, R.id.data);
        handler.removeCallbacks (runnable);
    }
    
    @OnClick (R.id.toggle_katup)
    public void toggleKatup () {
        showProgressBar (R.id.toggle_katup_loading, R.id.toggle_katup);
        
        ApiInterface.Builder
            .build (this)
            .toggle ()
            .enqueue (new QRCallback<DeviceData> () {
                @Override
                protected void onSuccessful (@NonNull DeviceData body) {
                    setNewData (body);
                }
    
                @Override
                protected void onFailure () {
                    Toast.makeText (MainActivity.this, "Gagal. Coba lagi.", Toast.LENGTH_SHORT).show ();
                }
    
                @Override
                protected void onExit () {
                    dismissProgressBar (R.id.toggle_katup_loading, R.id.toggle_katup);
                }
            });
    }
    
    private void setNewData (DeviceData data) {
        methane.setDonut_progress (String.valueOf (data.methane));
        oxygen.setDonut_progress (String.valueOf (data.oxygen));
        other.setDonut_progress (String.valueOf (100 - data.methane - data.oxygen));
    
        if (data.katup) btnToggleKatup.setText ("Tutup Katup");
        else btnToggleKatup.setText ("Buka Katup");
    }
    
    private void logout () {
        getSharedPreferences (SPNAME, MODE_PRIVATE)
            .edit ()
            .clear ()
            .apply ();
        startActivity (new Intent (this, AuthActivity.class));
        finish ();
    }
}
