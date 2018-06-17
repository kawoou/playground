package com.playground.entity.table

import slick.jdbc.PostgresProfile.api._

case class Mail(
  title: String,
  content: String,
  sender: String,
  isSent: Boolean = false,
  id: Option[Long] = Option.empty
)

class Mails(tag: Tag) extends Table[Mail](tag, "mail") {
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  val title = column[String]("title")
  val content = column[String]("content")
  val sender = column[String]("sender")
  val isSent = column[Boolean]("sent")

  def * = (title, content, sender, isSent, id.?) <> ((Mail.apply _).tupled, Mail.unapply)
}
