package com.wx.demo.view.widgets.contentscrollable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContentUtils {
    static final int ACTION_SAME = 0;
    static final int ACTION_INSERT = 1;
    static final int ACTION_DELETE = 2;

    static final char EMPTY_CHAR = (char) 0;

    public static String provideNumberList() {
        return "0123456789";
    }

    public static String provideAlphabeticalList() {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }

    public static int[] computeColumnActions(char[] source, char[] target,
            Set<Character> supportedCharacters) {
        int sourceIndex = 0;
        int targetIndex = 0;

        List<Integer> columnActions = new ArrayList<>();
        while (true) {
            final boolean reachedEndOfSource = sourceIndex == source.length;
            final boolean reachedEndOfTarget = targetIndex == target.length;
            if (reachedEndOfSource && reachedEndOfTarget) {
                break;
            } else if (reachedEndOfSource) {
                fillWithActions(columnActions, target.length - targetIndex, ACTION_INSERT);
                break;
            } else if (reachedEndOfTarget) {
                fillWithActions(columnActions, source.length - sourceIndex, ACTION_DELETE);
                break;
            }

            final boolean containsSourceChar = supportedCharacters.contains(source[sourceIndex]);
            final boolean containsTargetChar = supportedCharacters.contains(target[targetIndex]);

            if (containsSourceChar && containsTargetChar) {
                final int sourceEndIndex =
                        findNextUnsupportedChar(source, sourceIndex + 1, supportedCharacters);
                final int targetEndIndex =
                        findNextUnsupportedChar(target, targetIndex + 1, supportedCharacters);

                appendColumnActionsForSegment(
                        columnActions,
                        source,
                        target,
                        sourceIndex,
                        sourceEndIndex,
                        targetIndex,
                        targetEndIndex
                );
                sourceIndex = sourceEndIndex;
                targetIndex = targetEndIndex;
            } else if (containsSourceChar) {
                columnActions.add(ACTION_INSERT);
                targetIndex++;
            } else if (containsTargetChar) {
                columnActions.add(ACTION_DELETE);
                sourceIndex++;
            } else {
                columnActions.add(ACTION_SAME);
                sourceIndex++;
                targetIndex++;
            }
        }

        final int[] result = new int[columnActions.size()];
        for (int i = 0; i < columnActions.size(); i++) {
            result[i] = columnActions.get(i);
        }
        return result;
    }

    private static int findNextUnsupportedChar(char[] chars, int startIndex,
            Set<Character> supportedCharacters) {
        for (int i = startIndex; i < chars.length; i++) {
            if (!supportedCharacters.contains(chars[i])) {
                return i;
            }
        }
        return chars.length;
    }

    private static void fillWithActions(List<Integer> actions, int num, int action) {
        for (int i = 0; i < num; i++) {
            actions.add(action);
        }
    }

    private static void appendColumnActionsForSegment(
            List<Integer> columnActions,
            char[] source,
            char[] target,
            int sourceStart,
            int sourceEnd,
            int targetStart,
            int targetEnd
    ) {
        final int sourceLength = sourceEnd - sourceStart;
        final int targetLength = targetEnd - targetStart;
        final int resultLength = Math.max(sourceLength, targetLength);

        if (sourceLength == targetLength) {
            fillWithActions(columnActions, resultLength, ACTION_SAME);
            return;
        }

        final int numRows = sourceLength + 1;
        final int numCols = targetLength + 1;

        final int[][] matrix = new int[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j < numCols; j++) {
            matrix[0][j] = j;
        }

        int cost;
        for (int row = 1; row < numRows; row++) {
            for (int col = 1; col < numCols; col++) {
                cost = source[row - 1 + sourceStart] == target[col - 1 + targetStart] ? 0 : 1;

                matrix[row][col] = min(
                        matrix[row-1][col] + 1,
                        matrix[row][col-1] + 1,
                        matrix[row-1][col-1] + cost);
            }
        }

        final List<Integer> resultList = new ArrayList<>(resultLength * 2);
        int row = numRows - 1;
        int col = numCols - 1;
        while (row > 0 || col > 0) {
            if (row == 0) {
                resultList.add(ACTION_INSERT);
                col--;
            } else if (col == 0) {
                resultList.add(ACTION_DELETE);
                row--;
            } else {
                final int insert = matrix[row][col-1];
                final int delete = matrix[row-1][col];
                final int replace = matrix[row-1][col-1];

                if (insert < delete && insert < replace) {
                    resultList.add(ACTION_INSERT);
                    col--;
                } else if (delete < replace) {
                    resultList.add(ACTION_DELETE);
                    row--;
                } else {
                    resultList.add(ACTION_SAME);
                    row--;
                    col--;
                }
            }
        }

        final int resultSize = resultList.size();
        for (int i = resultSize - 1; i >= 0; i--) {
            columnActions.add(resultList.get(i));
        }
    }

    private static int min(int first, int second, int third) {
        return Math.min(first, Math.min(second, third));
    }
}
