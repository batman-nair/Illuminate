package com.example.arjun.illuminate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

import static com.example.arjun.illuminate.HomeActivity.tts;

public class TopicActivity extends Activity {


    private static final String HOME_MESSAGE = "Going back to home screen.";
    private static final String TOPIC_HELP = "Press speak to listen. Diagrams if any, can be felt on touch.";
    private String TAG = TopicActivity.class.getSimpleName();
    float initialX, initialY;
    long StartTouchTime, EndTouchTime;
    float height, width;
    boolean isTouch = false;
    String TouchMessage = "", LastTouchMessage = "", TapMessage = "";

    TextView tvTopic1, tvTopic2, tvTopic3, tvTopic4, tvTopic5, tvTopic6;
    TextView tvBack, tvHome, tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float heightPX = metrics.heightPixels;
        float widthPX = metrics.widthPixels;

        height = convertPixelsToDp(heightPX, getApplicationContext());
        width = convertPixelsToDp(widthPX, getApplicationContext());

        tvTopic1 = (TextView) findViewById(R.id.topic1);
        tvTopic2 = (TextView) findViewById(R.id.topic2);
        tvTopic3 = (TextView) findViewById(R.id.topic3);
        tvTopic4 = (TextView) findViewById(R.id.topic4);
        tvTopic5 = (TextView) findViewById(R.id.topic5);
        tvTopic6 = (TextView) findViewById(R.id.topic6);
        tvBack = (TextView) findViewById(R.id.back);
        tvHome = (TextView) findViewById(R.id.home);
        tvNext = (TextView) findViewById(R.id.next);

        tvTopic1.setText("Trees");
        tvTopic2.setText("3d Shapes");

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

        if(System.currentTimeMillis() - StartTouchTime > 200) {
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


                if(isTouch) {

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

                    if(TouchMessage.equals(LastTouchMessage)) {

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

                        tts.speak(TOPIC_HELP, TextToSpeech.QUEUE_FLUSH, null);
                        Intent teacherIntent = new Intent(TopicActivity.this, TeacherActivity.class);
                        teacherIntent.putExtra("TopicNo", 0);
                        teacherIntent.putExtra("PageMax", 2);
                        TopicActivity.this.finish();
                        TopicActivity.this.startActivity(teacherIntent);

                    } else if (isInside(X, Y, tvTopic2)) {
//                        TapMessage = tvTopic2.getText().toString();
                        tts.speak(TOPIC_HELP, TextToSpeech.QUEUE_FLUSH, null);
                        Intent teacherIntent = new Intent(TopicActivity.this, TeacherActivity.class);
                        teacherIntent.putExtra("TopicNo", 1);
                        teacherIntent.putExtra("PageMax", 2);
                        TopicActivity.this.finish();
                        TopicActivity.this.startActivity(teacherIntent);
                    } else if (isInside(X, Y, tvTopic3)) {
                        TapMessage = tvTopic3.getText().toString();
                    } else if (isInside(X, Y, tvTopic4)) {
                        TapMessage = tvTopic4.getText().toString();
                    } else if (isInside(X, Y, tvTopic5)) {
                        TapMessage = tvTopic5.getText().toString();
                    } else if (isInside(X, Y, tvTopic6)) {
                        TapMessage = tvTopic6.getText().toString();
                    } else if (isInside(X, Y, tvBack)) {

                        tts.speak("There is nothing to go back to.", TextToSpeech.QUEUE_FLUSH, null);


                    } else if (isInside(X, Y, tvHome)) {

                        tts.speak(HOME_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        Intent homeIntent = new Intent(TopicActivity.this, HomeActivity.class);
                        TopicActivity.this.finish();
                        TopicActivity.this.startActivity(homeIntent);

                    } else if (isInside(X, Y, tvNext)) {

                        tts.speak("There are no more topics to show.", TextToSpeech.QUEUE_FLUSH, null);

                    }

                    if(!TapMessage.equals("")) {
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

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isInside(float X, float Y, TextView tv) {
        int tvCords[] = new int[2];
        tv.getLocationInWindow(tvCords);
        if (X > tvCords[0] && X < tvCords[0]+ tv.getWidth()) {
            if (Y > tvCords[1] && Y < tvCords[1] + tv.getHeight()) {
//                Log.d(TAG, "isInside: " + tv.getText());
                return true;
            }
        }
        return false;
    }
}
