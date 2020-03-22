package tf.bug

import spire.math.{UByte, UShort}

package object scalagb {

  def uBytesToUShort(h: UByte, l: UByte): UShort = UShort(h.toInt << 8) + UShort(l.toInt)

  def uShortToUBytes(v: UShort): (UByte, UByte) = (UByte(v.toInt >> 8), UByte(v.toInt & 0xFF))

}
