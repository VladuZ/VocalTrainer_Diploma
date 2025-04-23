package com.example.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoteAnimator extends View {

    private List<Note> notes;
    private Handler handler;
    private Runnable runnable;

    public static final int REFRESH_RATE = 16;
    public static final int NOTE_SPEED = 5;

    private static final int NUM_PITCHES = 36;
    private List<Key> pianoKeys = new ArrayList<>();

    private int currentPitchIndex = -1;
    private int sungPitch = -1;

    private static final int[] NOTE_COLORS = {
            Color.RED, Color.MAGENTA, Color.rgb(255,165,0), Color.rgb(255,105,180),
            Color.YELLOW, Color.GREEN, Color.rgb(0,128,128), Color.BLUE,
            Color.CYAN, Color.rgb(128,0,128), Color.rgb(255,20,147), Color.LTGRAY
    };

    public NoteAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        notes = new ArrayList<>();
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                updateNotes();
                invalidate();
                handler.postDelayed(this, REFRESH_RATE);
            }
        };

        handler.post(runnable);
    }

    private boolean isBlackKey(int pitchIndex) {
        int[] blackNotes = {1, 3, 6, 8, 10};
        for (int i : blackNotes) if (i == pitchIndex % 12) return true;
        return false;
    }

    private void initPianoKeys() {
        pianoKeys.clear();
        int height = getHeight();
        int whiteKeyCount = 0;

        for (int i = 0; i < NUM_PITCHES; i++) {
            if (!isBlackKey(i)) whiteKeyCount++;
        }

        float whiteKeyHeight = (float) height / whiteKeyCount;
        float y = 0;

        for (int i = 0; i < NUM_PITCHES; i++) {
            if (isBlackKey(i)) {
                float blackHeight = whiteKeyHeight * 0.6f;
                float blackY = y - blackHeight / 2;
                pianoKeys.add(new Key(blackY, blackHeight, true));
            } else {
                pianoKeys.add(new Key(y, whiteKeyHeight, false));
                y += whiteKeyHeight;
            }
        }
    }

    public void addNote(int pitchIndex, int lengthPx) {
        if (pianoKeys.isEmpty()) initPianoKeys();
        if (pitchIndex < 0 || pitchIndex >= pianoKeys.size()) return;

        Key key = pianoKeys.get(pitchIndex);
        int y = (int) key.y;
        int height = (int) key.height;
        int width = key.isBlack ? (int) (getWidth() * 0.6f) : getWidth();

        int color = NOTE_COLORS[pitchIndex % 12];
        notes.add(new Note(getWidth(), y, width, height, pitchIndex, color));
    }

    private void updateNotes() {
        Iterator<Note> iterator = notes.iterator();
        currentPitchIndex = -1;

        while (iterator.hasNext()) {
            Note note = iterator.next();
            note.x -= NOTE_SPEED;
            if (note.x + note.width < 0) {
                iterator.remove();
            } else if (currentPitchIndex == -1 && note.x <= getWidth() * 0.9) {
                currentPitchIndex = note.pitchIndex;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Note note : notes) {
            Paint paint = new Paint();
            paint.setColor(note.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(note.x, note.y, note.x + note.width, note.y + note.height, paint);
        }

        // 🎤 Візуалізація співаної ноти
        if (sungPitch != -1 && sungPitch < pianoKeys.size()) {
            Key key = pianoKeys.get(sungPitch);
            int y = (int) key.y;
            int height = (int) key.height;
            Paint sungPaint = new Paint();
            sungPaint.setColor(Color.argb(150, 0, 255, 0)); // прозоро-зелений
            sungPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, y, getWidth(), y + height, sungPaint);
        }
    }

    public int getCurrentPitchIndex() {
        return currentPitchIndex;
    }

    public void setSungPitch(int pitchIndex) {
        this.sungPitch = pitchIndex;
    }

    private static class Note {
        int x, y, width, height, pitchIndex, color;

        Note(int x, int y, int width, int height, int pitchIndex, int color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.pitchIndex = pitchIndex;
            this.color = color;
        }
    }

    private static class Key {
        float y, height;
        boolean isBlack;

        Key(float y, float height, boolean isBlack) {
            this.y = y;
            this.height = height;
            this.isBlack = isBlack;
        }
    }
}
