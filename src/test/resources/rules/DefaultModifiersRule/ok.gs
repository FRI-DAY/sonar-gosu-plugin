package checks.DefaultModifiersRule

class ok {

  var _startDate : Date as StartDate

  property get CurrentDate() : Date {
    return StartDate
  }

  function getDate() {
      /* ... */
  }

  public enum InnerEnum {
    YES,
    NO
  }

  public interface InnerInterface {
    function getBigInt() : int
  }

  public class nestedClass {

  }

}
