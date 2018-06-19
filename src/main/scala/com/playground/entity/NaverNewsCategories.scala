package com.playground.entity

object NaverNewsCategories extends Enumeration {
    sealed abstract class Category(val key: String) {}

    case object Politics extends Category("100")
    case object Economy extends Category("101")
    case object Society extends Category("102")
    case object Life extends Category("103")
    case object World extends Category("104")
    case object IT extends Category("105")
    case object Entertainment extends Category("106")
}