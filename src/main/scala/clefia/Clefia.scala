package clefia

import clefia.Numeric._

import scala.collection.GenSeq

/**
  * Created by gastonsantoalla on 31/10/16.
  */
object Clefia {

  def process[T](blocks: GenSeq[Numeric128], key: T, f: (Numeric128, Keys, Int) => Numeric128 ): GenSeq[Numeric128] = {
    val (keys, rounds) = key match  {
      case (k0: Long, k1: Long, k2: Long, k3: Long) => (KeyScheduling.scheduleKeys((k0, k1, k2, k3)), 18)
      case (k0: Long, k1: Long, k2: Long, k3: Long, k4: Long, k5: Long) => (KeyScheduling.scheduleKeys((k0, k1, k2, k3, k4, k5)), 22)
      case (k0: Long, k1: Long, k2: Long, k3: Long, k4: Long, k5: Long, k6: Long, k7: Long) => (KeyScheduling.scheduleKeys((k0, k1, k2, k3, k4, k5, k6, k7)), 26)
      case _ => throw new InvalidKeyException("Invalid Key")
    }

    blocks.map(f(_, keys, rounds))
  }

  def encrypt[T](blocks: GenSeq[Numeric128], key: T): GenSeq[Numeric128] = process(blocks, key, DataProcessing.enc)
  def decrypt[T](blocks: GenSeq[Numeric128], key: T): GenSeq[Numeric128] = process(blocks, key, DataProcessing.dec)

  def encryptBlock[T](block: Numeric128, key: T): Numeric128 = encrypt(List(block), key).head
  def decryptBlock[T](block: Numeric128, key: T): Numeric128 = decrypt(List(block), key).head

  def encryptText[T](plaintText: String, key: T): String = ???
  def decryptText[T](cipherText: String, key: T): String = ???

  def encryptFile[T](originPath: String, destinationPath: String, key: T) = ???
  def decryptFile[T](originPath: String, destinationPath: String, key: T) = ???


}

class InvalidKeyException(message: String ) extends RuntimeException
