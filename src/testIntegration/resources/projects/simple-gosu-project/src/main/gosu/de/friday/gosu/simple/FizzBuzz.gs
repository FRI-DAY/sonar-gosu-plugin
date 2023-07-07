package de.friday.gosu.simple

uses java.lang.Boolean
uses java.lang.Integer

uses java.util.*

class FizzBuzz implements Fizz, Buzz {

  function run() {
    for (number in 0..100) {
      if(isFizzBuzz(number)) "FizzBuzz".print()
      else if(isFizz(number)) "Fizz".print()
      else if(isBuzz(number)) "Buzz".print()
      else print(number)
    }
  }

  function isFizzBuzz(number: Integer): boolean {
    return isFizz(number) and isBuzz(number)
  }

  override function isFizz(number: Integer): Boolean {
    return (number % 5) == 0;
  }

  override function isBuzz(number: Integer): Boolean {
    return (number % 3) == 0;
  }
}
