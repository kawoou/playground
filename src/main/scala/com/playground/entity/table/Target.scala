package com.playground.entity.table

import slick.jdbc.PostgresProfile.api._

case class Target(
  name: String,
  birthDay: String,
  id: Option[Long] = Option.empty
)

class TargetTable(tag: Tag) extends Table[Target](tag, "target") {
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  val name = column[String]("name")
  val birthDay = column[String]("birth")

  def * = (name, birthDay, id.?) <> ((Target.apply _).tupled, Target.unapply)
}