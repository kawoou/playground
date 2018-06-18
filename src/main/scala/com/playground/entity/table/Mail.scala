package com.playground.entity.table

import java.sql.Timestamp

import com.playground.common.Passwordable
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._

case class Mail(
  title: String,
  content: String,
  sender: String,
  password: String,
  salt: String,
  sentAt: Option[Timestamp] = Option.empty,
  createdAt: Timestamp = new Timestamp(DateTime.now().getMillis),
  id: Option[Long] = Option.empty
) extends Passwordable {
  def isSent: Boolean = sentAt.isDefined
}

class Mails(tag: Tag) extends Table[Mail](tag, "mail") {
  val id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  val title = column[String]("title")
  val content = column[String]("content")
  val sender = column[String]("sender")
  val password = column[String]("password")
  val salt = column[String]("salt")
  val sentAt = column[Option[Timestamp]]("sent_at")
  val createdAt = column[Timestamp]("created_at")

  def * = (title, content, sender, password, salt, sentAt, createdAt, id.?) <> ((Mail.apply _).tupled, Mail.unapply)
}
