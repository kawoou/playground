package com.playground.entity.table

import com.playground.ApplicationComponentImpl

case class Mail(
  title: String,
  content: String,
  id: Option[Long] = Option.empty
)

trait MailTableComponent extends ApplicationComponentImpl {
  import application.session.profile.api._

  class Mails(tag: Tag) extends Table[Mail](tag, "mail") {
    val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    val title = column[String]("title")
    val content = column[String]("content")

    def * = (title, content, id.?) <> ((Mail.apply _).tupled, Mail.unapply)
  }
  lazy val Mails = TableQuery[Mails]
}
