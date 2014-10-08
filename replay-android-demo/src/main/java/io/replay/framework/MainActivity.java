package io.replay.framework;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    public static final String API_KEY = "2bbf36db-ed6f-4944-92ab-11639c2b74f2";
    @InjectView(R.id.customEventText) TextView customText;
    @InjectView(R.id.flushCountTV) TextView count;
    @InjectView(R.id.log) TextView log;


    private Handler mHandler;


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            count.setText(String.valueOf(ReplayIO.count()));
            mHandler.postDelayed(mStatusChecker, 333);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        ButterKnife.inject(this);

        ReplayIO.init(getApplicationContext());

        mHandler = new Handler();
        mHandler.postDelayed(mStatusChecker, 333);
    }

    @OnClick(R.id.button_a)
    public void AButtonClick() {
        ReplayIO.track("Clicked Button A");
        log.setText(log.getText() + "\nAdded Button_A Job");
    }

    @OnClick(R.id.button_b)
    public void BButtonClick(){
        ReplayIO.track("Clicked Button B");
        log.setText(log.getText() + "\nAdded Button_B Job");
    }

    @OnClick(R.id.button_customEvent)
    public void customButtonClick(){
        String custom = customText.getText().toString();
        ReplayIO.track(custom);
        log.setText(log.getText() + "\nAdded Custom Event Job");
    }

    @OnClick(R.id.button_identify)
    public void identifyButtonClick(){
        Object[] data = new Object[6];
        data[0] = "email";
        data[1] = "example@gmail.com";
        data[2] = "age";
        data[3] = 22;
        data[4] = "gender";
        data[5] = "male";

        ReplayIO.updateTraits(data);
        log.setText(log.getText() + "\nAdded Traits Job");
    }

    @OnClick(R.id.flush)
    public void flushButtonClick(){
        ReplayIO.dispatch();
        mHandler.postDelayed(flushCheck, 1000);
    }

    private Runnable flushCheck = new Runnable(){
        @Override
        public void run() {
            if(ReplayIO.count() == 0){
                log.setText("\nSuccessfully flushed queue!");
            }
        }
    };

}
