package com.playground.entity

object CrawlerType extends Enumeration {
  sealed abstract class Info(val name: String) {}

  case object Bloter extends Info("Bloter")
  case object NaverNews extends Info("Naver News")
}
