package tf.bug.scalagb

import spire.math.{UByte, ULong, UShort}

case class GameBoy(
  pc: UShort = UShort(0),
  sp: UShort = UShort(0),
  registers: GameBoyRegisters = GameBoyRegisters(),
  reserves: GameBoyRegisters = GameBoyRegisters(),
  i: UByte = UByte(0),
  r: UByte = UByte(0),
  m: UByte = UByte(0),
  clockM: ULong = ULong(0),
  halt: Boolean = false,
  stop: Boolean = false,
  error: Boolean = false,
  unknownOpcodes: Vector[(UShort, UByte)] = Vector.empty
) {
  def withA(value: UByte): GameBoy = copy(registers = registers.copy(a = value))
  def withB(value: UByte): GameBoy = copy(registers = registers.copy(b = value))
  def withC(value: UByte): GameBoy = copy(registers = registers.copy(c = value))
  def withD(value: UByte): GameBoy = copy(registers = registers.copy(d = value))
  def withE(value: UByte): GameBoy = copy(registers = registers.copy(e = value))
  def withH(value: UByte): GameBoy = copy(registers = registers.copy(h = value))
  def withL(value: UByte): GameBoy = copy(registers = registers.copy(l = value))
  def withF(value: UByte): GameBoy = copy(registers = registers.copy(f = value))
  def reserve: GameBoy = copy(reserves = registers)
  def restore: GameBoy = copy(registers = reserves)

  def read(address: UShort): UByte = ???
  def write(address: UShort, value: UByte): GameBoy = ???
}

case class GameBoyRegisters(
  a: UByte = UByte(0),
  b: UByte = UByte(0),
  c: UByte = UByte(0),
  d: UByte = UByte(0),
  e: UByte = UByte(0),
  h: UByte = UByte(0),
  l: UByte = UByte(0),
  f: UByte = UByte(0),
)
