package org.example;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.example.NumberUtils.add;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NumberUtilsTest {
    /**
     *
     * Step 1: understand the requirement, input type and output type
     *        Requirement: Add two list of integer, index by index, and returns another list
     *
     * Step 2 (raw):  Perform partition and boundary analysis on input and output
     *        Each input: left | right
     *        Combination of input:
     *        Output:
     *  Step 3: Derive potential test cases
     *
     */


    // null input tests
    public static Stream<Arguments> nullInputData() {
        return Stream.of(
                // test1: case for null left input
                Arguments.of(null, List.of(0), null),
                Arguments.of(null, Arrays.asList(1, 2), null),
                // test2: case for null right input
                Arguments.of(List.of(0), null, null),
                Arguments.of(Arrays.asList(1, 2), null, null)
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("nullInputData")
    void nullInput(List<Integer> left, List<Integer> right, List<Integer> output) {
        // add() should return null
        assertThat(add(left, right))
                .as("expected output to be null")
                .isEqualTo(output);
    }


    // empty input test
    public static Stream<Arguments> emptyInputData() {
        return Stream.of(
                // test3: case for empty left input
                Arguments.of(List.of(), List.of(0), List.of(0)),
                Arguments.of(List.of(), Arrays.asList(1, 2), Arrays.asList(1, 2)),
                // test4: case for empty right input
                Arguments.of(List.of(0), List.of(), List.of(0)),
                Arguments.of(Arrays.asList(1, 2), List.of(), Arrays.asList(1, 2))
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("emptyInputData")
    void emptyInput(List<Integer> left, List<Integer> right, List<Integer> output) {
        // add() should return list of 0 + right if left empty or right + 0 if right empty
        assertThat(add(left, right))
                .as("expected output to be 0 + nonempty input")
                .isEqualTo(output);
    }


    // no carryover
    public static Stream<Arguments> noCarryoverData() {
        return Stream.of(
                // test5: case for when left and right are single digits
                Arguments.of(List.of(0), List.of(1), List.of(1)),
                Arguments.of(List.of(1), List.of(2), List.of(3)),
                // test7: case for when left is multiple digits and right is single digit
                Arguments.of(Arrays.asList(1, 2), List.of(0), Arrays.asList(1, 2)),
                Arguments.of(Arrays.asList(1, 2), List.of(3), Arrays.asList(1, 5)),
                // test10: case for when left and right are multiple digits
                Arguments.of(Arrays.asList(0, 1), Arrays.asList(2, 3), Arrays.asList(2, 4)),
                Arguments.of(Arrays.asList(1, 2), Arrays.asList(1, 3), Arrays.asList(2, 5))
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("noCarryoverData")
    void noCarryover(List<Integer> left, List<Integer> right, List<Integer> output) {
        // add() should return sum without needing to handle digit carryover
        assertThat(add(left, right))
                .as("expected output to be left + right without carryover")
                .isEqualTo(output);
    }


    // carryover cases
    public static Stream<Arguments> carryoverData() {
        return Stream.of(
                // test6: case for when left and right are single digits
                Arguments.of(List.of(2), List.of(9), Arrays.asList(1, 1)),
                Arguments.of(List.of(9), List.of(9), Arrays.asList(1, 8)),
                // test8: case for when left is multiple digits and right is single digit
                Arguments.of(Arrays.asList(0, 1), List.of(9), Arrays.asList(1, 0)),
                Arguments.of(Arrays.asList(1, 2), List.of(9), Arrays.asList(2, 1)),
                // test11: case for when left and right are multiple digits
                Arguments.of(Arrays.asList(0, 1), Arrays.asList(1, 9), Arrays.asList(2, 0)),
                Arguments.of(Arrays.asList(1, 2), Arrays.asList(3, 9), Arrays.asList(5, 1)),
                Arguments.of(Arrays.asList(1, 2), Arrays.asList(9, 9), Arrays.asList(1, 1, 1))
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("carryoverData")
    void carryover(List<Integer> left, List<Integer> right, List<Integer> output) {
        // add() should return sum and handle digit carryover
        assertThat(add(left, right))
                .as("expected output to be left + right with carryover")
                .isEqualTo(output);
    }


    // leading zero cases
    public static Stream<Arguments> leadingZeroData() {
        return Stream.of(
                // test9: case for when left is multiple digits and right is single digit
                Arguments.of(Arrays.asList(0, 1), List.of(2), List.of(3)),
                Arguments.of(Arrays.asList(0, 1, 2), List.of(3), Arrays.asList(1, 5)),
                // test12: case for when left and right are multiple digits
                Arguments.of(Arrays.asList(1, 2), Arrays.asList(0, 3), Arrays.asList(1, 5)),
                Arguments.of(Arrays.asList(0, 1, 2), Arrays.asList(0, 3), Arrays.asList(1, 5)),
                Arguments.of(Arrays.asList(0, 1, 2), Arrays.asList(1, 3), Arrays.asList(2, 5))
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("leadingZeroData")
    void leadingZero(List<Integer> left, List<Integer> right, List<Integer> output) {
        // add() should return sum and handle leading zero
        assertThat(add(left, right))
                .as("expected output to be left + right without leading zero")
                .isEqualTo(output);
    }


    // invalid digit cases
    public static Stream<Arguments> invalidDigitData() {
        return Stream.of(
                // test13: case for when either left or right has a digit less than 0
                Arguments.of(List.of(0), List.of(-1)),
                Arguments.of(List.of(-1), List.of(0)),
                Arguments.of(Arrays.asList(0, -1, -2), Arrays.asList(1, 2, 3)),
                Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(0, -1, -2)),
                // test14: case for when either left or right has a digit greater than 9
                Arguments.of(List.of(0), List.of(10)),
                Arguments.of(List.of(10), List.of(0)),
                Arguments.of(Arrays.asList(0, 10, 11), Arrays.asList(1, 2, 3)),
                Arguments.of(Arrays.asList(1, 2, 3), Arrays.asList(0, 10, 11))
        );
    }

    @ParameterizedTest
    @Tag("Specification-based")
    @MethodSource("invalidDigitData")
    void invalidDigit(List<Integer> left, List<Integer> right) {
        // add() should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> add(left, right),
                "expected method to throw IllegalArgumentException");
    }


    // exhaustive invalid digit cases
    @Test
    @Tag("Coverage")
    void exhaustiveDigit() {
        // add() should pass without exception
        // coverage test0: 0 <= leftDigit <= 9 && 0 <= rightDigit <= 9
        assertThat(add(List.of(5), List.of(4))).isEqualTo(List.of(9));
        // add() should now throw IllegalArgumentException
        // coverage test1: 0 <= leftDigit <= 9 && rightDigit > 9
        assertThrows(IllegalArgumentException.class, () -> add(List.of(5), List.of(10)));
        // coverage test2: 0 <= leftDigit <= 9 && 0 > rightDigit
        assertThrows(IllegalArgumentException.class, () -> add(List.of(5), List.of(-2)));
        // coverage test4: leftDigit > 9 && 0 <= rightDigit <= 9
        assertThrows(IllegalArgumentException.class, () -> add(List.of(10), List.of(4)));
        // coverage test8: 0 > leftDigit && 0 <= rightDigit <= 9
        assertThrows(IllegalArgumentException.class, () -> add(List.of(-2), List.of(4)));
    }


}