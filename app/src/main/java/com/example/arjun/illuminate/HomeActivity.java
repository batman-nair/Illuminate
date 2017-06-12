package com.example.arjun.illuminate;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import static com.example.arjun.illuminate.TopicActivity.convertPixelsToDp;
import static com.example.arjun.illuminate.TopicActivity.isInside;


public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";
    private static final String RESOURCES_MESSAGE = "Opening Topics screen. ";
    private static final String EXIT_MESSAGE = "Exiting application. ";
    private static final String HELP_MESSAGE = "Press and hold to know where your finger is. Tap to select.";
    private static final String TEXTANALYSER_MESSAGE = "Opening Text Analyser. ";
    private static final String MAGNIFIER_MESSAGE = "Opening camera for magnifier. ";
    float initialX, initialY;
    long StartTouchTime, EndTouchTime;
    float height, width;
    public static TextToSpeech tts;
    boolean isTouch = false;
    String TouchMessage = "", LastTouchMessage = "", TapMessage = "";

    TextView tvTopic1, tvTopic2, tvTopic3, tvTopic4, tvTopic5, tvTopic6;
    TextView tvBack, tvHome, tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        tvTopic1 = (TextView) findViewById(R.id.topic1);
        tvTopic2 = (TextView) findViewById(R.id.topic2);
        tvTopic3 = (TextView) findViewById(R.id.topic3);
        tvTopic4 = (TextView) findViewById(R.id.topic4);
        tvTopic5 = (TextView) findViewById(R.id.topic5);
        tvTopic6 = (TextView) findViewById(R.id.topic6);
        tvBack = (TextView) findViewById(R.id.back);
        tvHome = (TextView) findViewById(R.id.home);
        tvNext = (TextView) findViewById(R.id.next);

        tvTopic1.setText("Resources");
        tvTopic2.setText("Text Analyser");
        tvTopic3.setText("Magnifier");
        tvBack.setText("Exit");
        tvHome.setText("Help");

        tvTopic4.setVisibility(View.GONE);
        tvTopic5.setVisibility(View.GONE);
        tvTopic6.setVisibility(View.GONE);
        tvNext.setVisibility(View.GONE);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float heightPX = metrics.heightPixels;
        float widthPX = metrics.widthPixels;

        height = convertPixelsToDp(heightPX, getApplicationContext());
        width = convertPixelsToDp(widthPX, getApplicationContext());

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        if (System.currentTimeMillis() - StartTouchTime > 200) {
            isTouch = true;
        }

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                initialY = event.getY();
                isTouch = false;
                LastTouchMessage = "";
                TouchMessage = "";
                TapMessage = "";

                StartTouchTime = System.currentTimeMillis();
                Log.d(TAG, "onTouchEvent: StartTouch at " + StartTouchTime);

//                Log.d(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:


                if (isTouch) {

                    float X = event.getX();
                    float Y = event.getY();

                    if (isInside(X, Y, tvTopic1)) {
                        TouchMessage = tvTopic1.getText().toString();
                    } else if (isInside(X, Y, tvTopic2)) {
                        TouchMessage = tvTopic2.getText().toString();
                    } else if (isInside(X, Y, tvTopic3)) {
                        TouchMessage = tvTopic3.getText().toString();
                    } else if (isInside(X, Y, tvTopic4)) {
                        TouchMessage = tvTopic4.getText().toString();
                    } else if (isInside(X, Y, tvTopic5)) {
                        TouchMessage = tvTopic5.getText().toString();
                    } else if (isInside(X, Y, tvTopic6)) {
                        TouchMessage = tvTopic6.getText().toString();
                    } else if (isInside(X, Y, tvBack)) {
                        TouchMessage = tvBack.getText().toString();
                    } else if (isInside(X, Y, tvHome)) {
                        TouchMessage = tvHome.getText().toString();
                    } else if (isInside(X, Y, tvNext)) {
                        TouchMessage = tvNext.getText().toString();
                    }

                    if (TouchMessage.equals(LastTouchMessage)) {

                    } else {
                        LastTouchMessage = TouchMessage;
                        tts.speak(TouchMessage, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                float finalX = event.getX();
                float finalY = event.getY();
                float X = finalX;
                float Y = finalY;

                EndTouchTime = System.currentTimeMillis();

                float distTravelledinPixel = (float) Math.sqrt(Math.pow((finalX - initialX), 2) + Math.pow((finalY - initialY), 2));
                float distTravelledinDPI = convertPixelsToDp(distTravelledinPixel, getApplicationContext());

                if (EndTouchTime - StartTouchTime < 200 && distTravelledinDPI < 50) {

                    if (isInside(X, Y, tvTopic1)) {

                        tts.speak(RESOURCES_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        Intent topicIntent = new Intent(HomeActivity.this, TopicActivity.class);
                        HomeActivity.this.finish();
                        HomeActivity.this.startActivity(topicIntent);

                    } else if (isInside(X, Y, tvTopic2)) {
                        tts.speak(TEXTANALYSER_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        Intent analyserIntent = new Intent(HomeActivity.this, OcrCaptureActivity.class);
                        HomeActivity.this.startActivity(analyserIntent);

                    } else if (isInside(X, Y, tvTopic3)) {
                        tts.speak(MAGNIFIER_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        Intent analyserIntent = new Intent(HomeActivity.this, OcrCaptureActivity2.class);
                        HomeActivity.this.startActivity(analyserIntent);

                    } else if (isInside(X, Y, tvTopic4)) {
                        TapMessage = tvTopic4.getText().toString();
                    } else if (isInside(X, Y, tvTopic5)) {
                        TapMessage = tvTopic5.getText().toString();
                    } else if (isInside(X, Y, tvTopic6)) {
                        TapMessage = tvTopic6.getText().toString();
                    } else if (isInside(X, Y, tvBack)) {

                        tts.speak(EXIT_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        HomeActivity.this.finish();

                    } else if (isInside(X, Y, tvHome)) {

                        tts.speak(HELP_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);

                    } else if (isInside(X, Y, tvNext)) {
                        TapMessage = tvNext.getText().toString();
                    }

                    if (!TapMessage.equals("")) {
                        tts.speak("You tapped " + TapMessage, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    Log.d(TAG, "onTouchEvent: is a tap");

                } else {

                    Log.d(TAG, "onTouchEvent: is a touch");
                }

                break;


        }

        return super.onTouchEvent(event);
    }
}
