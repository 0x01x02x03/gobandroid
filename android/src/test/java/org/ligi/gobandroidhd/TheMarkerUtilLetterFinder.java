package org.ligi.gobandroidhd;

import org.junit.Test;
import org.ligi.gobandroid_hd.logic.markers.util.MarkerUtil;

import static org.assertj.core.api.Assertions.assertThat;

public class TheMarkerUtilLetterFinder extends MarkerTestBase {

    @Test
    public void testFindFirst() {
        final String firstFreeLetter = MarkerUtil.findNextLetter(markerList("C", "B"));
        assertThat(firstFreeLetter).isEqualTo("A");
    }


    @Test
    public void testFindLast() {
        final String firstFreeLetter = MarkerUtil.findNextLetter(markerList("A", "B"));
        assertThat(firstFreeLetter).isEqualTo("C");
    }


    @Test
    public void testFindGap() {
        final String firstFreeLetter = MarkerUtil.findNextLetter(markerList("A", "C"));
        assertThat(firstFreeLetter).isEqualTo("B");
    }


    @Test
    public void testSurviveNumbers() {
        final String firstFreeLetter = MarkerUtil.findNextLetter(markerList("A", "1"));
        assertThat(firstFreeLetter).isEqualTo("B");
    }


    @Test
    public void testFindZ() {
        final String firstFreeLetter = MarkerUtil.findNextLetter(markerList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y"));
        assertThat(firstFreeLetter).isEqualTo("Z");
    }
}