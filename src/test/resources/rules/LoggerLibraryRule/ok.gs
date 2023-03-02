package checks.LoggerLibraryRule

uses org.slf4j.Logger
uses org.slf4j.LoggerFactory
uses gw.api.system.PCLoggerCategory
uses org.apache.commons.lang3.StringUtils

class ok {

  static final var LOGGER = LoggerFactory.getLogger(ok)

  function sampleFunc() {
  }

}
