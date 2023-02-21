package checks.LoggerCheck

uses org.slf4j.LoggerFactory

class ok_log {

  static final var LOGGER = LoggerFactory.getLogger(ok_log.Class)
  static final var LOG = LoggerFactory.getLogger(MethodHandles().lookup)

  function sampleFunc() {
  }

}
