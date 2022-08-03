package com.wx.demo.view.widgets.contentscrollable;

import android.graphics.Canvas;
import android.graphics.Paint;

@SuppressWarnings("unused")
class ContentColumn {
    private ContentList[] characterLists;
    private final ContentDrawMetrics metrics;

    private char currentChar = ContentUtils.EMPTY_CHAR;
    private char targetChar = ContentUtils.EMPTY_CHAR;

    private char[] currentCharacterList;
    private int startIndex;
    private int endIndex;

    private int bottomCharIndex;
    private float bottomDelta;
    private float charHeight;

    private float sourceWidth, currentWidth, targetWidth, minimumRequiredWidth;

    private float currentBottomDelta;
    private float previousBottomDelta;
    private int directionAdjustment;

    ContentColumn(ContentList[] characterLists, ContentDrawMetrics metrics) {
        this.characterLists = characterLists;
        this.metrics = metrics;
    }

    void setCharacterLists(ContentList[] characterLists) {
        this.characterLists = characterLists;
    }

    void setTargetChar(char targetChar) {
        this.targetChar = targetChar;
        this.sourceWidth = this.currentWidth;
        this.targetWidth = metrics.getCharWidth(targetChar);
        this.minimumRequiredWidth = Math.max(this.sourceWidth, this.targetWidth);

        setCharacterIndices();

        final boolean scrollDown = endIndex >= startIndex;
        directionAdjustment = scrollDown ? 1 : -1;

        previousBottomDelta = currentBottomDelta;
        currentBottomDelta = 0f;
    }

    char getCurrentChar() {
        return currentChar;
    }

    char getTargetChar() {
        return targetChar;
    }

    float getCurrentWidth() {
        checkForDrawMetricsChanges();
        return currentWidth;
    }

    float getMinimumRequiredWidth() {
        checkForDrawMetricsChanges();
        return minimumRequiredWidth;
    }

    private void setCharacterIndices() {
        currentCharacterList = null;

        for (ContentList characterList : characterLists) {
            final ContentList.CharacterIndices indices =
                    characterList.getCharacterIndices(currentChar, targetChar, metrics.getScrollDirection());
            if (indices != null) {
                this.currentCharacterList = characterList.getCharacterList();
                this.startIndex = indices.startIndex;
                this.endIndex = indices.endIndex;
            }
        }

        if (currentCharacterList == null) {
            if (currentChar == targetChar) {
                currentCharacterList = new char[] {currentChar};
                startIndex = endIndex = 0;
            } else {
                currentCharacterList = new char[] {currentChar, targetChar};
                startIndex = 0;
                endIndex = 1;
            }
        }
    }

    void onAnimationEnd() {
        checkForDrawMetricsChanges();
        minimumRequiredWidth = currentWidth;
    }

    private void checkForDrawMetricsChanges() {
        final float currentTargetWidth = metrics.getCharWidth(targetChar);
        if (currentWidth == targetWidth && targetWidth != currentTargetWidth) {
            this.minimumRequiredWidth = this.currentWidth = this.targetWidth = currentTargetWidth;
        }
    }

    void setAnimationProgress(float animationProgress) {
        if (animationProgress == 1f) {
            this.currentChar = this.targetChar;
            currentBottomDelta = 0f;
            previousBottomDelta = 0f;
        }

        final float charHeight = metrics.getCharHeight();

        final float totalHeight = charHeight * Math.abs(endIndex - startIndex);

        final float currentBase = animationProgress * totalHeight;

        final float bottomCharPosition = currentBase / charHeight;

        final float bottomCharOffsetPercentage = bottomCharPosition - (int) bottomCharPosition;

        final float additionalDelta = previousBottomDelta * (1f - animationProgress);

        bottomDelta = bottomCharOffsetPercentage * charHeight * directionAdjustment
                + additionalDelta;

        bottomCharIndex = startIndex + ((int) bottomCharPosition * directionAdjustment);

        this.charHeight = charHeight;
        this.currentWidth = sourceWidth + (targetWidth - sourceWidth) * animationProgress;
    }

    void draw(Canvas canvas, Paint textPaint) {
        if (drawText(canvas, textPaint, currentCharacterList, bottomCharIndex, bottomDelta)) {

            if (bottomCharIndex >= 0) {
                currentChar = currentCharacterList[bottomCharIndex];
            }

            currentBottomDelta = bottomDelta;
        }

        drawText(canvas, textPaint, currentCharacterList, bottomCharIndex + 1,
                bottomDelta - charHeight);
        drawText(canvas, textPaint, currentCharacterList, bottomCharIndex - 1,
                bottomDelta + charHeight);
    }

    private boolean drawText(Canvas canvas, Paint textPaint, char[] characterList,
            int index, float verticalOffset) {
        if (index >= 0 && index < characterList.length) {
            canvas.drawText(characterList, index, 1, 0f, verticalOffset, textPaint);
            return true;
        }
        return false;
    }
}
