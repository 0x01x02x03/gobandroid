package org.ligi.gobandroid_hd.ui.tsumego;

import com.google.common.base.Optional;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NextTsumegoDeterminator {

    /**
     * try to find next tsumego based on filename
     * searching the last number and incrementing it
     *
     * @param FileName
     * @return the filename found
     */
    public static String calcNextTsumego(String FileName) {
        final Optional<String> old_index = getLastNumberInStringOrNull(FileName);

        if (!old_index.isPresent()) {
            return null;
        }

        final int index = Integer.parseInt(old_index.get());

        String new_index = "";
        // add the leading zeroes
        for (int i = 0; i < old_index.get().length() - ((index + 1) / 10 + 1); i++) {
            new_index += "0";
        }

        new_index += "" + (index + 1);

        final String guessedFileNameString = replaceLast(FileName, old_index.get(), new_index);

        // check if it exists
        if (!new File(guessedFileNameString).exists()) {
            return null;
        }

        return guessedFileNameString;

    }

    private static Optional<String> getLastNumberInStringOrNull(final String fileName) {
        final Pattern p = Pattern.compile("\\d+");
        final Matcher m = p.matcher(fileName);

        Optional<String> old_index = Optional.absent();

        while (m.find()) {
            old_index = Optional.of(m.group());
        }

        return old_index;
    }

    private static String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) {
            return string;
        }
        final String tail = string.substring(lastIndex).replaceFirst(from, to);
        return string.substring(0, lastIndex) + tail;
    }

}
