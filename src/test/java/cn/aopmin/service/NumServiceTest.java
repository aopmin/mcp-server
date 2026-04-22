package cn.aopmin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("NumService 单元测试")
public class NumServiceTest {

    private final NumService numService = new NumService();

    @Nested
    @DisplayName("judgeIfOddJava 方法测试")
    class JudgeIfOddJavaTest {

        @Test
        @DisplayName("测试正偶数 - 输入 2，应返回 '2是双数'")
        void testPositiveEvenNumber() {
            String result = numService.judgeIfOddJava(2);
            assertEquals("2是双数", result);
        }

        @Test
        @DisplayName("测试正偶数 - 输入 4，应返回 '4是双数'")
        void testPositiveEvenNumber4() {
            String result = numService.judgeIfOddJava(4);
            assertEquals("4是双数", result);
        }

        @Test
        @DisplayName("测试正偶数 - 输入 100，应返回 '100是双数'")
        void testPositiveEvenNumber100() {
            String result = numService.judgeIfOddJava(100);
            assertEquals("100是双数", result);
        }

        @Test
        @DisplayName("测试正奇数 - 输入 1，应返回 '1不是双数'")
        void testPositiveOddNumber() {
            String result = numService.judgeIfOddJava(1);
            assertEquals("1不是双数", result);
        }

        @Test
        @DisplayName("测试正奇数 - 输入 3，应返回 '3不是双数'")
        void testPositiveOddNumber3() {
            String result = numService.judgeIfOddJava(3);
            assertEquals("3不是双数", result);
        }

        @Test
        @DisplayName("测试正奇数 - 输入 99，应返回 '99不是双数'")
        void testPositiveOddNumber99() {
            String result = numService.judgeIfOddJava(99);
            assertEquals("99不是双数", result);
        }

        @Test
        @DisplayName("测试负偶数 - 输入 -2，应返回 '-2是双数'")
        void testNegativeEvenNumber() {
            String result = numService.judgeIfOddJava(-2);
            assertEquals("-2是双数", result);
        }

        @Test
        @DisplayName("测试负偶数 - 输入 -4，应返回 '-4是双数'")
        void testNegativeEvenNumber4() {
            String result = numService.judgeIfOddJava(-4);
            assertEquals("-4是双数", result);
        }

        @Test
        @DisplayName("测试负奇数 - 输入 -1，应返回 '-1不是双数'")
        void testNegativeOddNumber() {
            String result = numService.judgeIfOddJava(-1);
            assertEquals("-1不是双数", result);
        }

        @Test
        @DisplayName("测试负奇数 - 输入 -3，应返回 '-3不是双数'")
        void testNegativeOddNumber3() {
            String result = numService.judgeIfOddJava(-3);
            assertEquals("-3不是双数", result);
        }

        @Test
        @DisplayName("测试边界值 - 输入 0（偶数），应返回 '0是双数'")
        void testZero() {
            String result = numService.judgeIfOddJava(0);
            assertEquals("0是双数", result);
        }

        @Test
        @DisplayName("测试边界值 - 输入 Integer.MAX_VALUE（奇数），应返回 '2147483647不是双数'")
        void testIntegerMaxValue() {
            String result = numService.judgeIfOddJava(Integer.MAX_VALUE);
            assertEquals("2147483647不是双数", result);
        }

        @Test
        @DisplayName("测试边界值 - 输入 Integer.MIN_VALUE（偶数），应返回 '-2147483648是双数'")
        void testIntegerMinValue() {
            String result = numService.judgeIfOddJava(Integer.MIN_VALUE);
            assertEquals("-2147483648是双数", result);
        }
    }
}