package com.zytoune.dailygame.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameComparaturUtilTest {

    @Test
    void shouldReturnTrueWhenNamesAreIdentical() {
        assertTrue(GameComparaturUtil.compareGameNames("GameName", "GameName"));
    }

    @Test
    void shouldReturnTrueWhenNamesAreIdenticalButCaseDiffers() {
        assertTrue(GameComparaturUtil.compareGameNames("GameName", "gamename"));
    }

    @Test
    void shouldReturnTrueWhenNamesAreIdenticalButSpecialCharactersPresent() {
        assertTrue(GameComparaturUtil.compareGameNames("Game-Name", "GameName"));
    }

    @Test
    void shouldReturnTrueWhenNamesAreIdenticalButSpecialCharactersPresentWithSpaces() {
        assertTrue(GameComparaturUtil.compareGameNames("Game-Name  ", " GameName"));
    }

    @Test
    void shouldReturnTrueWhenNamesAreDifferentButContainEachOther() {
        assertTrue(GameComparaturUtil.compareGameNames("FIFA Soccer 08", "FIFA"));
    }

    @Test
    void shouldReturnTrueWhenNamesAreSameButWithAccent() {
        assertTrue(GameComparaturUtil.compareGameNames("Pokémon", "Pokemon"));
    }

    @Test
    void shouldReturnFalseWhenNamesAreDifferent() {
        assertFalse(GameComparaturUtil.compareGameNames("One", "Two"));
    }


}