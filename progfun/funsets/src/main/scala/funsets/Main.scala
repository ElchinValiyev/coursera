package funsets

object Main extends App {

  import FunSets._

  println(contains(singletonSet(1), 1))

  val x = union(union(singletonSet(-999), singletonSet(-997)), singletonSet(-995))
  printSet(x)
  printSet(map(x, _* -1))
}
