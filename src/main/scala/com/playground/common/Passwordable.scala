package com.playground.common

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

import scala.util.Random

trait Passwordable {
  val password: String
  val salt: String

  def testPassword(plainPassword: String): Boolean =
    password.equals(Passwordable.encryptPassword(plainPassword, salt))
}
object Passwordable {
  // Private
  private val saltSize = 128
  private val hexArray = "0123456789ABCDEF".toCharArray

  private def toHex(bytes: Array[Byte]): String = {
    val hexChars = new Array[Char](bytes.length * 2)
    for (j <- bytes.indices) {
      val v = bytes(j) & 0xFF
      hexChars(j * 2) = hexArray(v >>> 4)
      hexChars(j * 2 + 1) = hexArray(v & 0x0F)
    }
    new String(hexChars)
  }

  // Public
  def makeSalt: String =
    Random.alphanumeric take saltSize mkString ""

  def encryptPassword(password: String, salt: String): String = {
    val keySpec = new PBEKeySpec(password.toCharArray, salt.getBytes, 10000, 128)
    val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val bytes = secretKeyFactory.generateSecret(keySpec).getEncoded
    toHex(bytes)
  }
}