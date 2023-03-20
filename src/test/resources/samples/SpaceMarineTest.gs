package de.friday.metrics.lines

uses org.junit.jupiter.api.Test
uses org.assertj.core.api.Assertions

class SpaceMarineTest {

  @Test
  function shouldPurgeItInFlame() {
     Assertions.assertThat(
        \-> new SpaceMarine().purgeItInFlame(0)
     ).isEmpty()
  }
}
