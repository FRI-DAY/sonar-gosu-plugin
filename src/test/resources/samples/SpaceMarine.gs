package de.friday.metrics.lines

class SpaceMarine {

  private static final var PURGE_IT_IN_FLAME = "Purge it in gosu flame...\n";

  function purgeItInFlame( depthToPurge: int ): String {
    return (depthToPurge == 0 ? "" : purgeItInFlame( depthToPurge - 1 )) + "  ".repeat(depthToPurge) + PURGE_IT_IN_FLAME;
  }

    //Space, the final frontier.
    /*
    *
    */
}
