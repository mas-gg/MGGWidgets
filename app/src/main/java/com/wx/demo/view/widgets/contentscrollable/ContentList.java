package com.wx.demo.view.widgets.contentscrollable;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ContentList {
    private final int numOriginalCharacters;
    private final char[] characterList;
    private final Map<Character, Integer> characterIndicesMap;

    ContentList(String contentList) {
        if (contentList.contains(Character.toString(ContentUtils.EMPTY_CHAR))) {
//            CDException.handleException("You cannot include EMPTY_CHAR in the character list.");
            contentList = ContentUtils.provideNumberList();
        }

        final char[] charsArray = contentList.toCharArray();
        final int length = charsArray.length;
        this.numOriginalCharacters = length;

        characterIndicesMap = new HashMap<>(length);
        for (int i = 0; i < length; i++) {
            characterIndicesMap.put(charsArray[i], i);
        }

        this.characterList = new char[length * 2 + 1];
        this.characterList[0] = ContentUtils.EMPTY_CHAR;
        for (int i = 0; i < length; i++) {
            this.characterList[1 + i] = charsArray[i];
            this.characterList[1 + length + i] = charsArray[i];
        }
    }

    CharacterIndices getCharacterIndices(char start, char end, ContentScrollableTextView.ScrollDirection direction) {
        int startIndex = getIndexOfChar(start);
        int endIndex = getIndexOfChar(end);

        if (startIndex < 0 || endIndex < 0) {
            return null;
        }

        switch (direction) {
            case DOWN:
                if (end == ContentUtils.EMPTY_CHAR) {
                    endIndex = characterList.length;
                } else if (endIndex < startIndex) {
                    endIndex += numOriginalCharacters;
                }

                break;
            case UP:
                if (startIndex < endIndex) {
                    startIndex += numOriginalCharacters;
                }

                break;
            case ANY:
                if (start != ContentUtils.EMPTY_CHAR && end != ContentUtils.EMPTY_CHAR) {
                    if (endIndex < startIndex) {
                        final int nonWrapDistance = startIndex - endIndex;
                        final int wrapDistance = numOriginalCharacters - startIndex + endIndex;
                        if (wrapDistance < nonWrapDistance) {
                            endIndex += numOriginalCharacters;
                        }
                    } else if (startIndex < endIndex) {
                        final int nonWrapDistance = endIndex - startIndex;
                        final int wrapDistance = numOriginalCharacters - endIndex + startIndex;
                        if (wrapDistance < nonWrapDistance) {
                            startIndex += numOriginalCharacters;
                        }
                    }
                }
                break;
        }

        return new CharacterIndices(startIndex, endIndex);
    }

    Set<Character> getSupportedCharacters() {
        return characterIndicesMap.keySet();
    }

    char[] getCharacterList() {
        return characterList;
    }

    @SuppressWarnings("ConstantConditions")
    private int getIndexOfChar(char c) {
        if (c == ContentUtils.EMPTY_CHAR) {
            return 0;
        } else if (characterIndicesMap.containsKey(c)) {
            return characterIndicesMap.get(c) + 1;
        } else {
            return -1;
        }
    }

    static class CharacterIndices {
        final int startIndex;
        final int endIndex;

        public CharacterIndices(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }
}
