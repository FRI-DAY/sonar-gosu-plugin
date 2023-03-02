package checks.DefaultModifiersRule

final public class finalClass {

  final var _startDate : Date as StartDate

  construct(startDate : Date) {
    _startDate = startDate
  }

  final property get CurrentDate() : Date {
    return StartDate
  }

  final function getDate() {
      /* ... */
  }

  private function getField() {
  }

  public enum InnerEnum {
    YES,
    NO
  }

  public interface InnerInterface {
    function getBigInt() : int
  }

  public final class nestedClass {

  }
}
