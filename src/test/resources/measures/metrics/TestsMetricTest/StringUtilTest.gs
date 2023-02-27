package friday.suite.util

uses gw.testharness.RunLevel
uses friday.suite.TestBase

@RunLevel(NONE)
class StringUtilTest extends TestBase {

  var text = "LCM Lackiercentrum Magdeburg, Zweigniederlassung der LCW Lackiercentrum " +
      "Wernigerode GmbH"

  @org.junit.Test
  function testTrimToFullWords() {
    var expected = "LCM Lackiercentrum Magdeburg, Zweigniederlassung der LCW"
    assertEquals(expected, StringUtil.trimToFullWords(text, 60))
  }

  @org.junit.Test
  function testTrimToFullWordsBeginningOfString() {
    assertExceptionThrownWithMessage(\-> StringUtil.trimToFullWords(text, 0),
                                        IllegalArgumentException,
                                        "Max length should be greater than 0")
  }

  @org.junit.Test
  function testTrimToFullWordsMiddleOfFirstWord() {
    var expected = ""
    assertEquals(expected, StringUtil.trimToFullWords(text, 2))
  }

  @org.junit.Test
  function testTrimToFullWordsLimitToPositionOfSpaceAfterFirstWord() {
    var expected = "LCM"
    assertEquals(expected, StringUtil.trimToFullWords(text, 4))
  }

  @org.junit.Test
  function testTrimToFullWordsLimitToPositionOfSpaceAfterSecondWord() {
    var expected = "LCM Lackiercentrum"
    assertEquals(expected, StringUtil.trimToFullWords(text, 19))
  }

  @org.junit.Test
  function testTrimToFullWordsWithLengthGreaterThanText() {
    assertEquals(text, StringUtil.trimToFullWords(text, text.length() + 1))
  }

}
