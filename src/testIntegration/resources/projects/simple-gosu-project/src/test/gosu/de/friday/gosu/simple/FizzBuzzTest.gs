package de.friday.gosu.simple

uses org.junit.Test
uses org.assertj.core.api.Assertions

class FizzBuzzTest {

  @Test
  function fizzBuzz() {
    Assertions.assertThatCode(
        \-> new FizzBuzz().run()
    ).doesNotThrowAnyException()
  }

  @Test
  function isFizz() {
    Assertions.assertThat(new FizzBuzz().isFizz(5)).isTrue()
  }

  @Test
  function isBuzz() {
    Assertions.assertThat(new FizzBuzz().isBuzz(3)).isTrue()
  }

  @Test
  function isFizzBuzz() {
    Assertions.assertThat(new FizzBuzz().isFizzBuzz(15)).isTrue()
  }
}
