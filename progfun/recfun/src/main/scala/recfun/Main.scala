package recfun

import scala.annotation.tailrec

object
Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c, r - 1) + pascal(c - 1, r - 1)
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def stack(chars: List[Char], n: Int): Boolean = {
      if (chars.isEmpty) n == 0
      else if (n < 0) false
      else
        chars.head match {
          case x if x == '(' => stack(chars.tail, n + 1)
          case x if x == ')' => stack(chars.tail, n - 1)
          case _ => stack(chars.tail, n)
        }
    }

    stack(chars, 0)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    money match {
      case x if x == 0 => 1 // If n is 0 then there is 1 solution (do not include any coin)
      case x if x < 0 => 0 // If n is less than 0 then no solution exists
      case x if coins.isEmpty && x > 0 => 0
      case _ => countChange(money, coins.tail) + countChange(money - coins.head, coins)
    }
  }
}
