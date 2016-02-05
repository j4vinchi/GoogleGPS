package com.roostera.apps.googlegps.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.roostera.apps.googlegps.R;
import com.roostera.apps.googlegps.eventbus.LocationUpdateEvent;
import com.roostera.apps.googlegps.services.LocationUpdateService;
import de.greenrobot.event.EventBus;

public class MainActivity
        extends AppCompatActivity {

    private Button btnStartService, btnStopService;
    private static final String TAG = MainActivity.class.getSimpleName();
    private EventBus bus = EventBus.getDefault();
    private Intent intent;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        // Register as a subscriber
        bus.register(this);
    }
    private void setupUI() {
        setContentView(R.layout.activity_main);
        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "Buscando tu ubicación", "Espera...", true);
                dialog.show();
                //start service
                intent = new Intent(MainActivity.this, LocationUpdateService.class);
                intent.putExtra("TAG", TAG);
                startService(intent);

            }
        });
        btnStopService = (Button) findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop services
                stopService();
            }
        });
    }
    private void launchActivity(Location location){
        Intent i = new Intent(this, ShowGpsCoordinatesActivity.class);
        i.putExtra("location", location);
        startActivity(i);
    }
    public void onEvent(LocationUpdateEvent event){
        if (TAG.equals(event.getActivity())){
            dialog.dismiss();
            Log.d(TAG, "Location changed!");
            stopService();
            launchActivity(event.getLocation());
        }
    }
    @Override
    protected void onStop() {
        stopService();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        // Unregister
        bus.unregister(this);
        super.onDestroy();
    }
    private void stopService(){
        intent = new Intent(MainActivity.this, LocationUpdateService.class);
        stopService(intent);
    }
}
