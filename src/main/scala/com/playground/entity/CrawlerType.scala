package com.playground.entity

object CrawlerType extends Enumeration {
  sealed abstract class CrawlerInfo(name: String) {
    override def toString: String = name
  }

  case object Bloter extends CrawlerInfo("Bloter")


}
