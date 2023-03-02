package checks.DefaultModifiersRule

public class nok {

  private var _startDate : Date as StartDate

  public property get CurrentDate() : Date {
    return StartDate
  }

  public function getDate() {
      /* ... */
  }

  enum InnerEnum {
    YES,
    NO
  }

  interface InnerInterface {
    abstract public function getBigInt() : int
  }

}
