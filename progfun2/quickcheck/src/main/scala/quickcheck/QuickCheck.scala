package quickcheck

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    x <- arbitrary[A]
    h <- Gen.oneOf(Gen.const(empty), genHeap)
  } yield insert(x, h)


  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("Deleting the minimal value in a Heap should result in an empty Heap\"") = forAll { (h: A) =>
    isEmpty(deleteMin(insert(h, empty)))
  }

  property("The smallest of 2 elements should be the smallest in a previously empty Heap") = forAll { (n1: A, n2: A) =>
    val heap = insert(n1, insert(n2, empty))
    findMin(heap) == ord.min(n1, n2)
  }

  property("toList should give sorted result") = forAll { (h: H) =>
    val list = toList(h)
    list == list.sorted
  }

  property("The minimal value of a melded Heap should be the min of the min of both heaps") = forAll { (h1: H, h2: H) =>
    findMin(meld(h1, h2)) == ord.min(findMin(h1), findMin(h2))
  }

  property("associative meld") = forAll { (h: H, i: H, j: H) =>
    val a = meld(meld(h, i), j)
    val b = meld(h, meld(i, j))
    toList(a) == toList(b)
  }

  def toList(h: H): List[A] = if (isEmpty(h)) Nil else findMin(h) :: toList(deleteMin(h))

}
