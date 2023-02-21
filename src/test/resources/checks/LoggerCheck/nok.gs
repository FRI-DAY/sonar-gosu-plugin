package checks.LoggerCheck

uses org.slf4j.LoggerFactory

class nok {

  final var LOGGER = LoggerFactory.getLogger(log4jLogger)
  static var LOG = LoggerFactory.getLogger(log4jLogger)
  private static final var logger = LoggerFactory.getLogger(log4jLogger)
  private static final var _log = LoggerFactory.getLogger(ok)
  private static final var _logger = LoggerFactory.getLogger(ok.class)
  private static final var _loggerz = LoggerFactory.getLogger(ok.Class)

  function sampleFunc() {
  }
}
