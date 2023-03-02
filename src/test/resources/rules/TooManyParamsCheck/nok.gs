package checks.TooManyParamsCheck

class nok {

  function sampleFunc(a : int, b : String, c : int, d : int, e : String, f : int, g : String, h : int) {

  }

  construct(a : int, b : String, c : int, d : int, e : String, f : int, g : String, h : int) {

  }

  @SuppressWarnings("someWarning")
  function sampleFuncFour(a : int, b : String, c : int, d : int, e : String, f : int, g : String, h : int) {

  }

}
