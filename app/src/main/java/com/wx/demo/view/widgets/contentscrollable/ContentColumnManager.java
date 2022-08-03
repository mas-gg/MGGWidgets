package com.wx.demo.view.widgets.contentscrollable;

import android.graphics.Canvas;
import android.graphics.Paint;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("ForLoopReplaceableByForEach")
class ContentColumnManager {
    final ArrayList<ContentColumn> contentColumns = new ArrayList<>();
    private final ContentDrawMetrics metrics;

    private ContentList[] characterLists;
    private Set<Character> supportedCharacters;

    ContentColumnManager(ContentDrawMetrics metrics) {
        this.metrics = metrics;
    }

    void setContentList(String... contentList) {
        this.characterLists = new ContentList[contentList.length];
        for (int i = 0; i < contentList.length; i++) {
            this.characterLists[i] = new ContentList(contentList[i]);
        }

        this.supportedCharacters = new HashSet<>();
        for (int i = 0; i < contentList.length; i++) {
            this.supportedCharacters.addAll(this.characterLists[i].getSupportedCharacters());
        }

        for (ContentColumn contentColumn : contentColumns) {
            contentColumn.setCharacterLists(this.characterLists);
        }
    }

    ContentList[] getCharacterLists() {
        return characterLists;
    }

    void setText(char[] text) {
        if (characterLists == null) {
//            CDException.handleException("Need to call #setCharacterLists first.");
            return;
        }

        for (int i = 0; i < contentColumns.size(); ) {
            final ContentColumn contentColumn = contentColumns.get(i);
            if (contentColumn.getCurrentWidth() > 0) {
                i++;
            } else {
                contentColumns.remove(i);
            }
        }

        final int[] actions = ContentUtils.computeColumnActions(
                getCurrentText(), text, supportedCharacters
        );
        int columnIndex = 0;
        int textIndex = 0;
        for (int i = 0; i < actions.length; i++) {
            switch (actions[i]) {
                case ContentUtils.ACTION_INSERT:
                    contentColumns.add(columnIndex,
                            new ContentColumn(characterLists, metrics));
                case ContentUtils.ACTION_SAME:
                    contentColumns.get(columnIndex).setTargetChar(text[textIndex]);
                    columnIndex++;
                    textIndex++;
                    break;
                case ContentUtils.ACTION_DELETE:
                    contentColumns.get(columnIndex).setTargetChar(ContentUtils.EMPTY_CHAR);
                    columnIndex++;
                    break;
                default:
//                    CDException.handleException("Unknown action: " + actions[i]);
            }
        }
    }

    void onAnimationEnd() {
        for (int i = 0, size = contentColumns.size(); i < size; i++) {
            final ContentColumn column = contentColumns.get(i);
            column.onAnimationEnd();
        }
    }

    void setAnimationProgress(float animationProgress) {
        for (int i = 0, size = contentColumns.size(); i < size; i++) {
            final ContentColumn column = contentColumns.get(i);
            column.setAnimationProgress(animationProgress);
        }
    }

    float getMinimumRequiredWidth() {
        float width = 0f;
        for (int i = 0, size = contentColumns.size(); i < size; i++) {
            width += contentColumns.get(i).getMinimumRequiredWidth();
        }
        return width;
    }

    float getCurrentWidth() {
        float width = 0f;
        for (int i = 0, size = contentColumns.size(); i < size; i++) {
            width += contentColumns.get(i).getCurrentWidth();
        }
        return width;
    }

    char[] getCurrentText() {
        final int size = contentColumns.size();
        final char[] currentText = new char[size];
        for (int i = 0; i < size; i++) {
            currentText[i] = contentColumns.get(i).getCurrentChar();
        }
        return currentText;
    }

    void draw(Canvas canvas, Paint textPaint) {
        for (int i = 0, size = contentColumns.size(); i < size; i++) {
            final ContentColumn column = contentColumns.get(i);
            column.draw(canvas, textPaint);
            canvas.translate(column.getCurrentWidth(), 0f);
        }
    }
}
