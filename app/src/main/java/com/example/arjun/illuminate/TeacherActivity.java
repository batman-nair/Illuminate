package com.example.arjun.illuminate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.arjun.illuminate.HomeActivity.tts;
import static com.example.arjun.illuminate.TopicActivity.convertPixelsToDp;

public class TeacherActivity extends Activity {

    private static final String BACK_MESSAGE = "Going back to topics screen.";
    private Vibrator v;
    private int pageNo, pageMax;
    private boolean isTouch;
    private long StartTouchTime, EndTouchTime;
    float initialX, initialY;
    String TouchMessage = "", LastTouchMessage = "", TapMessage = "";
    String[][] PageTexts;
    private int[][] PageImages;
    int TopicNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        pageNo = 0;

        pageMax = getIntent().getIntExtra("PageMax", -1);

        PageImages = new int[3][3];
        PageTexts = new String[3][3];

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        TopicNo = getIntent().getIntExtra("TopicNo", -1);

        PageImages[0][0] = R.drawable.binary_tree;
        PageImages[0][1] = 0;
        PageImages[0][2] = R.drawable.nemeth_code;

        PageImages[1][0] = R.drawable.tetrahedron;
        PageImages[1][1] = R.drawable.square_prism;
        PageImages[1][2] = R.drawable.triangular_pyramid;




        PageTexts[0][0] = "Like linked lists, trees are made up of nodes. " +
                "A common kind of tree is a binary tree, in which each node contains a reference to two other nodes. " +
                "These references are referred to as the left and right subtrees. " +
                "A sample state diagram can be felt on the screen";

        PageTexts[0][1] = "Tree Traversal refers to the process of visiting (checking and/or updating) each node in a tree data structure, exactly once. " +
                "Such traversals are classified by the order in which the nodes are visited. ";

        PageTexts[0][2] = "A binary expression tree is a specific kind of a binary tree used to represent expressions. " +
                "Two common types of expressions that a binary expression tree can represent are algebraic and boolean. " +
                "These trees can represent expressions that contain both unary and binary operators. " +
                "A sample expression 1 + 5 multiplied by 3 cant be felt on screen. ";

        PageTexts[1][0] = "This is a tetrahedron. ";
        PageTexts[1][1] = "This is a square prism. ";
        PageTexts[1][2] = "This is a triangular pyramid. ";


        setPage();

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();


        if (System.currentTimeMillis() - StartTouchTime > 200) {
            isTouch = true;
        }

        ImageView imPage = (ImageView) findViewById(R.id.PageImage);

        float X = event.getX();
        float Y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                initialX = event.getX();
                initialY = event.getY();
                LastTouchMessage = "";
                TouchMessage = "";
                TapMessage = "";

                isTouch = false;

                StartTouchTime = System.currentTimeMillis();

                VibrateonObject((int) X, (int) Y, imPage);

                break;
            case MotionEvent.ACTION_MOVE:

                if (isTouch) {

                    TextView tvNext = (TextView) findViewById(R.id.Pagenext);
                    TextView tvBack = (TextView) findViewById(R.id.Pageback);
                    TextView tvSpeak = (TextView) findViewById(R.id.Pagespeak);

                    if (isInside(X, Y, tvNext)) {
                        TouchMessage = tvNext.getText().toString();
                    } else if (isInside(X, Y, tvBack)) {
                        TouchMessage = tvBack.getText().toString();
                    } else if (isInside(X, Y, tvSpeak)) {
                        TouchMessage = tvSpeak.getText().toString();
                    }

                    if (TouchMessage.equals(LastTouchMessage)) {

                    } else {
                        LastTouchMessage = TouchMessage;
                        tts.speak(TouchMessage, TextToSpeech.QUEUE_FLUSH, null);
                    }

                }

                VibrateonObject((int) X, (int) Y, imPage);

                break;

            case MotionEvent.ACTION_UP:

                float finalX = event.getX();
                float finalY = event.getY();

                EndTouchTime = System.currentTimeMillis();

                float distTravelledinPixel = (float) Math.sqrt(Math.pow((finalX - initialX), 2) + Math.pow((finalY - initialY), 2));
                float distTravelledinDPI = convertPixelsToDp(distTravelledinPixel, getApplicationContext());

                if (EndTouchTime - StartTouchTime < 200 && distTravelledinDPI < 50) {
                    TextView tvNext = (TextView) findViewById(R.id.Pagenext);
                    TextView tvBack = (TextView) findViewById(R.id.Pageback);
                    TextView tvSpeak = (TextView) findViewById(R.id.Pagespeak);

                    if (isInside(X, Y, tvNext)) {

                        nextPage();

                    } else if (isInside(X, Y, tvBack)) {

                        tts.speak(BACK_MESSAGE, TextToSpeech.QUEUE_FLUSH, null);
                        Intent topicIntent = new Intent(TeacherActivity.this, TopicActivity.class);
                        TeacherActivity.this.finish();
                        TeacherActivity.this.startActivity(topicIntent);

                    }

                    if (!TapMessage.equals("")) {
                        tts.speak("You tapped " + TapMessage, TextToSpeech.QUEUE_FLUSH, null);
                    }

                    if (isInside(X, Y, tvSpeak)) {
                        tts.speak(PageTexts[TopicNo][pageNo], TextToSpeech.QUEUE_FLUSH, null);
                    }
                }


                v.cancel();
        }
        return super.onTouchEvent(event);
    }

    public boolean isInside(float X, float Y, View tv) {
        int tvCords[] = new int[2];
        tv.getLocationInWindow(tvCords);
        if (tv.getVisibility() == View.GONE) {
            return false;
        }
        if (X > tvCords[0] && X < tvCords[0] + tv.getWidth()) {
            if (Y > tvCords[1] && Y < tvCords[1] + tv.getHeight()) {
//                Log.d(TAG, "isInside: " + tv.getText());
                return true;
            }
        }
        return false;
    }

    public void VibrateonObject(int X, int Y, ImageView iv) {
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap = iv.getDrawingCache();

        int imCords[] = new int[2];
        iv.getLocationInWindow(imCords);


        if (isInside(X, Y, iv)) {
            int PixelColor = bitmap.getPixel((int) X - imCords[0], (int) Y - imCords[1]);
            if (Color.red(PixelColor) < 220) {

                long[] pattern = {0, 100};
                v.vibrate(pattern, 0);

            } else {
                v.cancel();
            }
        }
    }

    public void setPage() {

        ImageView iv = (ImageView) findViewById(R.id.PageImage);
        iv.setImageResource(PageImages[TopicNo][pageNo]);
        tts.speak(PageTexts[TopicNo][pageNo], TextToSpeech.QUEUE_FLUSH, null);

    }

    public void nextPage() {
        if(pageNo < pageMax) {

            pageNo++;
            setPage();

        }
        else {

            tts.speak("No more pages", TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}