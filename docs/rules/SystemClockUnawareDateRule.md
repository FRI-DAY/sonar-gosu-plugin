Date-Creation not aware of Guidewire system clock

Usage of java.time.LocalDate.now() or new Date() ignore the Guidewire system clock setting. In result, it does not take time travel or cluster time synchronization into account.

On changing the execution order of unit tests, this potentially leads to failing tests. Within a GW cluster, it might also lead to unintented behaviour as cluster node times might slightly differ.