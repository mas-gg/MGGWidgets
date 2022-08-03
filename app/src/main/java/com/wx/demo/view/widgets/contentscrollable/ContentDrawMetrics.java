package com.wx.demo.view.widgets.contentscrollable;

import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

class ContentDrawMetrics {
    private final Paint textPaint;

    private final Map<Character, Float> charWidths = new HashMap<>(256);
    private float charHeight, charBaseline;

    private ContentScrollableTextView.ScrollDirection scrollDirection = ContentScrollableTextView.ScrollDirection.ANY;

    ContentDrawMetrics(Paint textPaint) {
        this.textPaint = textPaint;
        invalidate();
    }

    void invalidate() {
        charWidths.clear();
        final Paint.FontMetrics fm = textPaint.getFontMetrics();
        charHeight = fm.bottom - fm.top;
        charBaseline = -fm.top;
    }

    float getCharWidth(char character) {
        if (character == ContentUtils.EMPTY_CHAR) {
            return 0;
        }

        final Float value = charWidths.get(character);
        if (value != null) {
            return value;
        } else {
            final float width = textPaint.measureText(Character.toString(character));
            charWidths.put(character, width);
            return width;
        }
    }

    float getCharHeight() {
        return charHeight;
    }

    float getCharBaseline() {
        return charBaseline;
    }

    ContentScrollableTextView.ScrollDirection getScrollDirection() {
        return scrollDirection;
    }

    void setScrollDirection(ContentScrollableTextView.ScrollDirection scrollDirection) {
        this.scrollDirection = scrollDirection;
    }
}
