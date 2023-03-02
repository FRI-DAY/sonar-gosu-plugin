package checks.LoggerCheck

uses org.slf4j.LoggerFactory

class ok {

  public var field : int
  public static var notMemberAccessField : int = 21
  public static var notMethodCallField : int = "abcde".length
  public static var notLoggerFactory : int = "abcde".length()
  static var notGetMethod = LoggerFactory.getNotLogger(ok)
  var withoutArguments = LoggerFactory.getLogger()
  static final var LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

  function sampleFunc() {
  }

}
