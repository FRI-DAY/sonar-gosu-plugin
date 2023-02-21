package checks.ReturnEmptyCollectionCheck

uses java.util.function.Consumer
uses java.util.function.Predicate
uses java.util.function.UnaryOperator
uses java.util.stream.Stream

class nok {

  function returnSimpleArray() : String[] {
    return null
  }

  function returnQualifiedNameArray() : java.lang.String[] {
    return null
  }

  function returnList() : List {
    return null
  }

  function returnArrayList() : ArrayList {
    return null
  }

  function returnGenericList() : List<String> {
    return null
  }

  function nestedLists() : List<List<String>> {
    return null
  }

  function returnNullVariable() : PolicyLocationInfo[] {
    var sizedResultsArray : PolicyLocationInfo[] = null
    // Insure that we bounds restrict the result size
    var numberOfRecords = Math.max(0, Math.min(count, results.length-start))
    if (0 != numberOfRecords ) {
      sizedResultsArray = new PolicyLocationInfo[numberOfRecords]
      for (i in start..(start+sizedResultsArray.length-1)) {
        sizedResultsArray[i-start] = results[i]
      }
      // If we run out of results, must be returning the last little bit.
      //  remove the cache entry
      if (sizedResultsArray.length < count) {
        resultsCache.asMap().remove(refCon);
      }
    }
    return sizedResultsArray
  }

  function returnNewList() : List {
    var nullList : List<String> = null
    var newList = new List<String>(){

      override function size() : int {
        return 0
      }

      override property get Empty() : boolean {
        return false
      }

      override function contains(o : Object) : boolean {
        return false
      }

      override function iterator() : Iterator<String> {
        return null
      }

      override function forEach(action : Consumer<Object>) {

      }

      override function toArray() : Object[] {
        return new Object[0]
      }

      override function toArray<T>(a : T[]) : T[] {
        return null
      }

      override function add(s : String) : boolean {
        return false
      }

      override function remove(o : Object) : boolean {
        return false
      }

      override function removeIf(filter : Predicate<Object>) : boolean {
        return false
      }

      override function replaceAll(operator : UnaryOperator<String>) {

      }

      override function sort(c : Comparator<Object>) {

      }

      override function clear() {

      }

      override function get(index : int) : String {
        return null
      }

      override function set(index : int, element : String) : String {
        return null
      }

      override function add(index : int, element : String) {

      }

      override function remove(index : int) : String {
        return null
      }

      override function indexOf(o : Object) : int {
        return 0
      }

      override function lastIndexOf(o : Object) : int {
        return 0
      }

      override function listIterator() : ListIterator<String> {
        return null
      }

      override function listIterator(index : int) : ListIterator<String> {
        return null
      }

      override function subList(fromIndex : int, toIndex : int) : List<String> {
        return nullList
      }

      override function spliterator() : Spliterator<String> {
        return null
      }

      override function stream() : Stream<String> {
        return null
      }

      override function parallelStream() : Stream<String> {
        return null
      }

      override function retainAll(c : Collection<Object>) : boolean {
        return false
      }

      override function removeAll(c : Collection<Object>) : boolean {
        return false
      }

      override function addAll(index : int, c : Collection<String>) : boolean {
        return false
      }

      override function addAll(c : Collection<String>) : boolean {
        return false
      }

      override function containsAll(c : Collection<Object>) : boolean {
        return false
      }
    }

    return null
  }
}
