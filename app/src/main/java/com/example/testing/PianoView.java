package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PianoView extends View {

    private Paint whitePaint;
    private Paint blackPaint;
    private Paint borderPaint;
    private Paint textPaint;
    private Paint textOnBlackPaint;
    private Paint dividerPaint;

    private final int numWhiteKeys = 21;
    private final int[] blackKeyPattern = {1, 1, 0, 1, 1, 1, 0};
    private final String[] NOTE_NAMES = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    public PianoView(Context context) {
        super(context);
        init();
    }

    public PianoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);

        textOnBlackPaint = new Paint();
        textOnBlackPaint.setColor(Color.WHITE);
        textOnBlackPaint.setTextSize(26);
        textOnBlackPaint.setAntiAlias(true);

        dividerPaint = new Paint();
        dividerPaint.setColor(Color.LTGRAY);
        dividerPaint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float keyHeight = (float) getHeight() / numWhiteKeys;
        float keyWidth = getWidth();

        int pitchIndex = 0;
        int whiteKeyCount = 0;

        // Малюємо білі клавіші з підписами
        for (int i = 0; i < numWhiteKeys; i++) {
            float top = i * keyHeight;

            while (isBlackKey(pitchIndex)) {
                pitchIndex++;
            }

            String noteName = NOTE_NAMES[pitchIndex % 12] + (pitchIndex / 12);
            canvas.drawRect(0, top, keyWidth, top + keyHeight, whitePaint);
            canvas.drawRect(0, top, keyWidth, top + keyHeight, borderPaint);

            canvas.drawText(noteName, 20, top + keyHeight * 0.6f, textPaint);

            whiteKeyCount++;
            pitchIndex++;

            if (whiteKeyCount % 7 == 0 && whiteKeyCount < numWhiteKeys) {
                canvas.drawLine(0, top + keyHeight, keyWidth, top + keyHeight, dividerPaint);
            }
        }

        // Малюємо чорні клавіші з підписами
        int octaveCount = 3;
        int whiteKeyIndex = 0;
        int blackNoteCounter = 0;

        for (int octave = 0; octave < octaveCount; octave++) {
            for (int i = 0; i < blackKeyPattern.length; i++) {
                if (blackKeyPattern[i] == 1) {
                    int blackKeyIndex = whiteKeyIndex + i;
                    if (blackKeyIndex >= numWhiteKeys - 1) break;

                    float top = (blackKeyIndex + 1) * keyHeight - keyHeight * 0.3f;
                    float blackKeyHeight = keyHeight * 0.6f;
                    float blackKeyWidth = keyWidth * 0.6f;
                    float left = (keyWidth - blackKeyWidth) / 2;

                    canvas.drawRect(left, top, left + blackKeyWidth, top + blackKeyHeight, blackPaint);

                    // Підпис чорної клавіші
                    int currentPitch = getBlackPitchIndex(blackNoteCounter);
                    String noteName = NOTE_NAMES[currentPitch % 12] + (currentPitch / 12);
                    canvas.drawText(noteName, left + 10, top + blackKeyHeight / 2 + 8, textOnBlackPaint);

                    blackNoteCounter++;
                }
            }
            whiteKeyIndex += 7;
        }
    }

    private boolean isBlackKey(int pitchIndex) {
        int note = pitchIndex % 12;
        return note == 1 || note == 3 || note == 6 || note == 8 || note == 10;
    }

    private int getBlackPitchIndex(int blackNoteCounter) {
        int count = 0;
        for (int i = 0; i < 36; i++) {
            if (isBlackKey(i)) {
                if (count == blackNoteCounter) {
                    return i;
                }
                count++;
            }
        }
        return 1; // fallback (C#)
    }
}
