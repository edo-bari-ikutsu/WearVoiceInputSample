package com.bari_ikutsu.wearableeval1;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MyActivity extends Activity {

    private static final String TAG = "WearableEval1";

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

                // Intentを評価
                evalIntent(getIntent());
            }
        });
    }

    /**
     * Intentを評価
     * @param intent
     */
    protected void evalIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, intent.toString());

            if (intent.getAction() != null) {
                Log.d(TAG, intent.getAction().toString());
            }
            if (intent.getCategories() != null) {
                Log.d(TAG, intent.getCategories().toString());
            }
            if (intent.getExtras() != null) {
                for (String key : intent.getExtras().keySet()) {
                    Log.d(TAG, "key: " + key);
                }
            }
            if (intent.getType() != null) {
                Log.d(TAG, intent.getType());
            }
            if (intent.getData() != null) {
                ContentResolver cr = this.getContentResolver();
                String mime = cr.getType(intent.getData());
                Log.d(TAG, mime);
            }
            // タクシーを呼ぶ
            if (intent.getAction().equals("com.google.android.gms.actions.RESERVE_TAXI_RESERVATION")) {
                mTextView.setText("タクシーなんかより私に乗ってくださいよマイケル。");
            }
            // メモ
            else if (intent.getAction().equals("android.intent.action.SEND") && intent.getCategories().contains("com.google.android.voicesearch.SELF_NOTE")) {
                mTextView.setText("メモ入力：\n" + intent.getStringExtra(Intent.EXTRA_TEXT));
            }
            // アラーム設定
            else if (intent.getAction().equals("android.intent.action.SET_ALARM")) {
                mTextView.setText("アラーム設定：\n" + intent.getIntExtra(AlarmClock.EXTRA_HOUR, 0) + "時" + intent.getIntExtra(AlarmClock.EXTRA_MINUTES, 0) + "分");
            }
            // タイマー設定
            else if (intent.getAction().equals("android.intent.action.SET_TIMER")) {
                mTextView.setText("タイマー設定：\n" + intent.getIntExtra(AlarmClock.EXTRA_LENGTH, 0) + "秒");
            }
            else if (intent.getAction().equals("vnd.google.fitness.TRACK")) {
                // 自転車
                if (intent.getType().equals("vnd.google.fitness.activity/biking")) {
                    String actionStatus = intent.getStringExtra("actionStatus");
                    if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("自転車：開始");
                    }
                    else if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("自転車：終了");
                    }
                    else {
                        mTextView.setText("自転車：その他");
                    }
                }
                // ランニング
                else if (intent.getType().equals("vnd.google.fitness.activity/running")) {
                    String actionStatus = intent.getStringExtra("actionStatus");
                    if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("ランニング：開始");
                    }
                    else if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("ランニング：終了");
                    }
                    else {
                        mTextView.setText("ランニング：その他");
                    }
                }
                // その他の運動
                else if (intent.getType().equals("vnd.google.fitness.activity/other")) {
                    String actionStatus = intent.getStringExtra("actionStatus");
                    if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("運動：開始");
                    }
                    else if (actionStatus.equals("ActiveActionStatus")) {
                        mTextView.setText("運動：終了");
                    }
                    else {
                        mTextView.setText("運動：その他");
                    }
                }
            }
            else if (intent.getAction().equals("vnd.google.fitness.VIEW")) {
                // 心拍計を表示
                if (intent.getType().equals("vnd.google.fitness.data_type/com.google.heart_rate.bpm")) {
                    mTextView.setText("心拍計を表示");
                }
                // 歩数計を表示
                else if (intent.getType().equals("vnd.google.fitness.data_type/com.google.step_count.cumulative")) {
                    mTextView.setText("歩数計を表示");
                }
            }
        }
    }
}
