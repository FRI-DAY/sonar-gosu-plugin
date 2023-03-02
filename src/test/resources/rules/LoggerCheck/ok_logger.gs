package checks.LoggerCheck

uses org.slf4j.LoggerFactory

class ok_logger {

  private static final var LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())
  private static final var LOG = LoggerFactory.getLogger(ok_logger.class)

  function sampleFunc() {
  }

}