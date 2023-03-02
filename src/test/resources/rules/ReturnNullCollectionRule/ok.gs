package checks.ReturnEmptyCollectionCheck

uses java.util.function.Consumer
uses java.util.function.Predicate
uses java.util.function.UnaryOperator
uses java.util.stream.Stream

class ok {

  override property get EntityType() : Type {
    return null
  }

  function returnSimpleArray() : String[] {
    var stringArr : String[]
    return stringArr
  }

  function returnQualifiedNameArray() : java.lang.String[] {
    return {"abcde", "efgh"}
  }

  function returnList() : List {
    return new ArrayList<Integer>()
  }

  function returnArrayList() : ArrayList {
    var list = new ArrayList<String>()
    return list
  }

  function returnGenericList() : List<String> {
    return Collections.emptyList()
  }

  function returnString() : String {
    var someString : String = null
    return someString
  }

  function noReturnedValue() {
    return
  }

  function returnVoid() : void {
    return
  }

  function returnNewList() : List {

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
        return {}
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
        return Collections.emptyList()
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

    return newList
  }

}
