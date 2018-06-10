package com.playground.`type`

object Result {
  def success[A](x: A): Result[A] = Success(x)
  def failed[A](): Result[A] = Failed
}

sealed abstract class Result[+A] {
  def isSuccess: Boolean
  def isError: Boolean = !isSuccess
}
final case class Success[+A](value: A) extends Result[A] {
  def isSuccess: Boolean = true
}
case object Failed extends Result[Nothing] {
  def isSuccess: Boolean = false
}