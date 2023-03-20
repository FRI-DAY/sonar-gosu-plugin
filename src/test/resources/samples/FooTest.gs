package samples

uses org.junit.jupiter.api.Test
uses org.assertj.core.api.Assertions

class FooTest {

  @Test
  function shouldDoSomething() {
     Assertions.assertThat(
        \-> new Foo().doSomething('world')
     ).isEqualTo("Hello, got the argument 'world'")
  }
}
