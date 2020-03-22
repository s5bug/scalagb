package tf.bug.scalagb

import cats._
import cats.implicits._
import spire.math.{UByte, UShort}

abstract class MonadGameBoyZ80[M[_]](implicit monad: Monad[M]) extends GameBoyZ80[M] {

  override def liftByte(value: UByte): M[UByte] = monad.pure(value)

  override def liftWord(value: UShort): M[UShort] = monad.pure(value)

  override def reset: M[Unit] = for {
    _ <- setA(liftByte(UByte(0)))
    _ <- setB(liftByte(UByte(0)))
    _ <- setC(liftByte(UByte(0)))
    _ <- setD(liftByte(UByte(0)))
    _ <- setE(liftByte(UByte(0)))
    _ <- setH(liftByte(UByte(0)))
    _ <- setL(liftByte(UByte(0)))
    _ <- setF(liftByte(UByte(0)))
    _ <- setSP(liftWord(UShort(0)))
    _ <- setPC(liftWord(UShort(0)))
    _ <- setI(liftByte(UByte(0)))
    _ <- setR(liftByte(UByte(0)))
    _ <- setM(liftByte(UByte(0)))
  } yield ()

  override def exec: M[Unit] = for {
    op <- readByteAndIncrementPC
    _ <- instructionVector(op.toInt).getOrElse(unknownOpcode(liftByte(op)))
    _ <- incrementClock(getM)
  } yield ()

  def getHL: M[UShort] = for {
    h <- getH
    l <- getL
  } yield uBytesToUShort(h, l)

  def getAndIncrementPC: M[UShort] = for {
    pc <- getPC
    _ <- setPC(liftWord(pc + UShort(1)))
  } yield pc

  def readByteAndIncrementPC: M[UByte] = for {
    getAndIncrement <- getAndIncrementPC
    read <- readByte(getAndIncrement)
  } yield read

  def decrementAndGetSP(amount: UShort = UShort(1)): M[UShort] = for {
    sp <- getSP
    decSP = sp - amount
    _ <- setSP(liftWord(decSP))
  } yield decSP

  override def iNOP: M[Unit] = setM(liftByte(UByte(1)))

  override def iLDBCnn: M[Unit] = for {
    _ <- setC(readByteAndIncrementPC)
    _ <- setB(readByteAndIncrementPC)
    _ <- setM(liftByte(UByte(3)))
  } yield ()

  override def iLDBCmA: M[Unit] = for {
    b <- getB
    c <- getC
    _ <- writeByte(uBytesToUShort(b, c), getA)
    _ <- setM(liftByte(UByte(2)))
  } yield ()

  override def iINCBC: M[Unit] = for { // FIXME there may be some weird behavior here when setting B to itself
    b <- getB
    c <- getC
    asUShort = uBytesToUShort(b, c)
    increment = asUShort + UShort(1)
    (newB, newC) = uShortToUBytes(increment)
    _ <- setB(liftByte(newB))
    _ <- setC(liftByte(newC))
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iINCB: M[Unit] = for {
    b <- getB
    increment = b + UByte(1)
    _ <- setB(liftByte(increment))
    _ <- setF(liftByte(if(increment == UByte(0)) UByte(0x80) else UByte(0)))
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iDECB: M[Unit] = for {
    b <- getB
    decrement = b - UByte(1)
    _ <- setB(liftByte(decrement))
    _ <- setF(liftByte(if(decrement == UByte(255)) UByte(0x80) else UByte(0)))
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iLDBn: M[Unit] = for {
    _ <- setB(readByteAndIncrementPC)
    _ <- setM(liftByte(UByte(2)))
  } yield ()

  override def iRLCA: M[Unit] = ???

  override def iLDnnmSP: M[Unit] = ???

  override def iADDHLBC: M[Unit] = ???

  override def iLDABCm: M[Unit] = ???

  override def iDECBC: M[Unit] = ???

  override def iINCC: M[Unit] = ???

  override def iDECC: M[Unit] = ???

  override def iLDCn: M[Unit] = ???

  override def iRRCA: M[Unit] = ???

  override def iSTOP: M[Unit] = ???

  override def iLDDEnn: M[Unit] = ???

  override def iLDDEmA: M[Unit] = ???

  override def iINCDE: M[Unit] = ???

  override def iINCD: M[Unit] = ???

  override def iDECD: M[Unit] = ???

  override def iLDDn: M[Unit] = ???

  override def iRLA: M[Unit] = ???

  override def iJRn: M[Unit] = ???

  override def iADDHLDE: M[Unit] = ???

  override def iLDADEm: M[Unit] = ???

  override def iDECDE: M[Unit] = ???

  override def iINCE: M[Unit] = ???

  override def iDECE: M[Unit] = ???

  override def iLDEn: M[Unit] = ???

  override def iRRA: M[Unit] = ???

  override def iJRNZn: M[Unit] = ???

  override def iLDHLnn: M[Unit] = ???

  override def iLDIHLmA: M[Unit] = ???

  override def iINCHL: M[Unit] = ???

  override def iINCH: M[Unit] = ???

  override def iDECH: M[Unit] = ???

  override def iLDHn: M[Unit] = ???

  override def iDAA: M[Unit] = ???

  override def iJRZn: M[Unit] = ???

  override def iADDHLHL: M[Unit] = ???

  override def iLDIAHLm: M[Unit] = ???

  override def iDECHL: M[Unit] = ???

  override def iINCL: M[Unit] = ???

  override def iDECL: M[Unit] = ???

  override def iLDLn: M[Unit] = ???

  override def iCPL: M[Unit] = ???

  override def iJRNCn: M[Unit] = ???

  override def iLDSPnn: M[Unit] = ???

  override def iLDDHLmA: M[Unit] = ???

  override def iINCSP: M[Unit] = ???

  override def iINCHLm: M[Unit] = ???

  override def iDECHLm: M[Unit] = ???

  override def iLDHLmn: M[Unit] = ???

  override def iSCF: M[Unit] = ???

  override def iJRCn: M[Unit] = ???

  override def iADDHLSP: M[Unit] = ???

  override def iLDDAHLm: M[Unit] = ???

  override def iDECSP: M[Unit] = ???

  override def iINCA: M[Unit] = ???

  override def iDECA: M[Unit] = ???

  override def iLDAn: M[Unit] = ???

  override def iCCF: M[Unit] = ???

  override def iLDBB: M[Unit] = for {
    _ <- setB(getB)
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iLDBC: M[Unit] = for {
    _ <- setB(getC)
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iLDBD: M[Unit] = for {
    _ <- setB(getD)
    _ <- setM(liftByte(UByte(1)))
  } yield ()

  override def iLDBE: M[Unit] = ???

  override def iLDBH: M[Unit] = ???

  override def iLDBL: M[Unit] = ???

  override def iLDBHLm: M[Unit] = ???

  override def iLDBA: M[Unit] = ???

  override def iLDCB: M[Unit] = ???

  override def iLDCC: M[Unit] = ???

  override def iLDCD: M[Unit] = ???

  override def iLDCE: M[Unit] = ???

  override def iLDCH: M[Unit] = ???

  override def iLDCL: M[Unit] = ???

  override def iLDCHLm: M[Unit] = ???

  override def iLDCA: M[Unit] = ???

  override def iLDDB: M[Unit] = ???

  override def iLDDC: M[Unit] = ???

  override def iLDDD: M[Unit] = ???

  override def iLDDE: M[Unit] = ???

  override def iLDDH: M[Unit] = ???

  override def iLDDL: M[Unit] = ???

  override def iLDDHLm: M[Unit] = ???

  override def iLDDA: M[Unit] = ???

  override def iLDEB: M[Unit] = ???

  override def iLDEC: M[Unit] = ???

  override def iLDED: M[Unit] = ???

  override def iLDEE: M[Unit] = ???

  override def iLDEH: M[Unit] = ???

  override def iLDEL: M[Unit] = ???

  override def iLDEHLm: M[Unit] = ???

  override def iLDEA: M[Unit] = ???

  override def iLDHB: M[Unit] = ???

  override def iLDHC: M[Unit] = ???

  override def iLDHD: M[Unit] = ???

  override def iLDHE: M[Unit] = ???

  override def iLDHH: M[Unit] = ???

  override def iLDHL: M[Unit] = ???

  override def iLDHHLm: M[Unit] = ???

  override def iLDHA: M[Unit] = ???

  override def iLDLB: M[Unit] = ???

  override def iLDLC: M[Unit] = ???

  override def iLDLD: M[Unit] = ???

  override def iLDLE: M[Unit] = ???

  override def iLDLH: M[Unit] = ???

  override def iLDLL: M[Unit] = ???

  override def iLDLHLm: M[Unit] = ???

  override def iLDLA: M[Unit] = ???

  override def iLDHLmB: M[Unit] = ???

  override def iLDHLmC: M[Unit] = ???

  override def iLDHLmD: M[Unit] = ???

  override def iLDHLmE: M[Unit] = ???

  override def iLDHLmH: M[Unit] = ???

  override def iLDHLmL: M[Unit] = ???

  override def iHALT: M[Unit] = ???

  override def iLDHLmA: M[Unit] = ???

  override def iLDAB: M[Unit] = ???

  override def iLDAC: M[Unit] = ???

  override def iLDAD: M[Unit] = ???

  override def iLDAE: M[Unit] = ???

  override def iLDAH: M[Unit] = ???

  override def iLDAL: M[Unit] = ???

  override def iLDAHLm: M[Unit] = ???

  override def iLDAA: M[Unit] = ???

  override def iADDAB: M[Unit] = ???

  override def iADDAC: M[Unit] = ???

  override def iADDAD: M[Unit] = ???

  override def iADDAE: M[Unit] = ???

  override def iADDAH: M[Unit] = ???

  override def iADDAL: M[Unit] = ???

  override def iADDAHLm: M[Unit] = ???

  override def iADDAA: M[Unit] = ???

  override def iADCAB: M[Unit] = ???

  override def iADCAC: M[Unit] = ???

  override def iADCAD: M[Unit] = ???

  override def iADCAE: M[Unit] = ???

  override def iADCAH: M[Unit] = ???

  override def iADCAL: M[Unit] = ???

  override def iADCAHLm: M[Unit] = ???

  override def iADCAA: M[Unit] = ???

  override def iSUBAB: M[Unit] = ???

  override def iSUBAC: M[Unit] = ???

  override def iSUBAD: M[Unit] = ???

  override def iSUBAE: M[Unit] = ???

  override def iSUBAH: M[Unit] = ???

  override def iSUBAL: M[Unit] = ???

  override def iSUBAHLm: M[Unit] = ???

  override def iSUBAA: M[Unit] = ???

  override def iSBCAB: M[Unit] = ???

  override def iSBCAC: M[Unit] = ???

  override def iSBCAD: M[Unit] = ???

  override def iSBCAE: M[Unit] = ???

  override def iSBCAH: M[Unit] = ???

  override def iSBCAL: M[Unit] = ???

  override def iSBCAHLm: M[Unit] = ???

  override def iSBCAA: M[Unit] = ???

  override def iANDB: M[Unit] = ???

  override def iANDC: M[Unit] = ???

  override def iANDD: M[Unit] = ???

  override def iANDE: M[Unit] = ???

  override def iANDH: M[Unit] = ???

  override def iANDL: M[Unit] = ???

  override def iANDHLm: M[Unit] = ???

  override def iANDA: M[Unit] = ???

  override def iXORB: M[Unit] = ???

  override def iXORC: M[Unit] = ???

  override def iXORD: M[Unit] = ???

  override def iXORE: M[Unit] = ???

  override def iXORH: M[Unit] = ???

  override def iXORL: M[Unit] = ???

  override def iXORHLm: M[Unit] = ???

  override def iXORA: M[Unit] = ???

  override def iORB: M[Unit] = ???

  override def iORC: M[Unit] = ???

  override def iORD: M[Unit] = ???

  override def iORE: M[Unit] = ???

  override def iORH: M[Unit] = ???

  override def iORL: M[Unit] = ???

  override def iORHLm: M[Unit] = ???

  override def iORA: M[Unit] = ???

  override def iCMPB: M[Unit] = ???

  override def iCMPC: M[Unit] = ???

  override def iCMPD: M[Unit] = ???

  override def iCMPE: M[Unit] = ???

  override def iCMPH: M[Unit] = ???

  override def iCMPL: M[Unit] = ???

  override def iCMPHLm: M[Unit] = ???

  override def iCMPA: M[Unit] = ???

  override def iRETNZ: M[Unit] = ???

  override def iPOPBC: M[Unit] = ???

  override def iJPNZnn: M[Unit] = ???

  override def iJPnn: M[Unit] = for {
    pc <- getPC
    _ <- setPC(readWord(pc))
    _ <- setM(liftByte(UByte(3)))
  } yield ()

  override def iCALLNZnn: M[Unit] = ???

  override def iPUSHBC: M[Unit] = ???

  override def iADDAn: M[Unit] = ???

  override def iRST0: M[Unit] = ???

  override def iRETZ: M[Unit] = ???

  override def iRET: M[Unit] = ???

  override def iJPZnn: M[Unit] = ???

  override def iExtensions: M[Unit] = for {
    op <- readByteAndIncrementPC
    _ <- extensionVector(op.toInt)
  } yield ()

  override def iCALLZnn: M[Unit] = ???

  override def iCALLnn: M[Unit] = ???

  override def iADCAn: M[Unit] = ???

  override def iRST8: M[Unit] = ???

  override def iRETNC: M[Unit] = ???

  override def iPOPDE: M[Unit] = ???

  override def iJPNCnn: M[Unit] = ???

  override def iCALLNCnn: M[Unit] = ???

  override def iPUSHDE: M[Unit] = ???

  override def iSUBAn: M[Unit] = ???

  override def iRST10: M[Unit] = ???

  override def iRETC: M[Unit] = ???

  override def iRETI: M[Unit] = ???

  override def iJPCnn: M[Unit] = ???

  override def iCALLCnn: M[Unit] = ???

  override def iSBCAn: M[Unit] = ???

  override def iRST18: M[Unit] = ???

  override def iLDHnmA: M[Unit] = ???

  override def iPOPHL: M[Unit] = ???

  override def iLDHCmA: M[Unit] = ???

  override def iPUSHHL: M[Unit] = ???

  override def iANDn: M[Unit] = ???

  override def iRST20: M[Unit] = ???

  override def iADDSPd: M[Unit] = ???

  override def iJPHLm: M[Unit] = ???

  override def iLDnnmA: M[Unit] = ???

  override def iXORn: M[Unit] = ???

  override def iRST28: M[Unit] = ???

  override def iLDHAnm: M[Unit] = ???

  override def iPOPAF: M[Unit] = ???

  override def iDI: M[Unit] = ???

  override def iPUSHAF: M[Unit] = for {
    aadr <- decrementAndGetSP()
    _ <- writeByte(aadr, getA)
    fadr <- decrementAndGetSP()
    _ <- writeByte(fadr, getF)
    _ <- setM(liftByte(UByte(3)))
  } yield ()

  override def iORn: M[Unit] = ???

  override def iRST30: M[Unit] = ???

  override def iLDHLSPd: M[Unit] = ???

  override def iLDSPHL: M[Unit] = ???

  override def iLDAnnm: M[Unit] = ???

  override def iEI: M[Unit] = ???

  override def iCPn: M[Unit] = ???

  override def iRST38: M[Unit] = for {
    _ <- reserve
    sp <- decrementAndGetSP(UShort(2))
    _ <- writeWord(sp, getPC)
    _ <- setPC(liftWord(UShort(0x38)))
    _ <- setM(liftByte(UByte(3)))
  } yield()

  override def eRLCB: M[Unit] = ???

  override def eRLCC: M[Unit] = ???

  override def eRLCD: M[Unit] = ???

  override def eRLCE: M[Unit] = ???

  override def eRLCH: M[Unit] = ???

  override def eRLCL: M[Unit] = ???

  override def eRLCHLm: M[Unit] = ???

  override def eRLCA: M[Unit] = ???

  override def eRRCB: M[Unit] = ???

  override def eRRCC: M[Unit] = ???

  override def eRRCD: M[Unit] = ???

  override def eRRCE: M[Unit] = ???

  override def eRRCH: M[Unit] = ???

  override def eRRCL: M[Unit] = ???

  override def eRRCHLm: M[Unit] = ???

  override def eRRCA: M[Unit] = ???

  override def eRLB: M[Unit] = ???

  override def eRLC: M[Unit] = ???

  override def eRLD: M[Unit] = ???

  override def eRLE: M[Unit] = ???

  override def eRLH: M[Unit] = ???

  override def eRLL: M[Unit] = ???

  override def eRLHLm: M[Unit] = ???

  override def eRLA: M[Unit] = ???

  override def eRRB: M[Unit] = ???

  override def eRRC: M[Unit] = ???

  override def eRRD: M[Unit] = ???

  override def eRRE: M[Unit] = ???

  override def eRRH: M[Unit] = ???

  override def eRRL: M[Unit] = ???

  override def eRRHLm: M[Unit] = ???

  override def eRRA: M[Unit] = ???

  override def eSLAB: M[Unit] = ???

  override def eSLAC: M[Unit] = ???

  override def eSLAD: M[Unit] = ???

  override def eSLAE: M[Unit] = ???

  override def eSLAH: M[Unit] = ???

  override def eSLAL: M[Unit] = ???

  override def eSLAHLm: M[Unit] = ???

  override def eSLAA: M[Unit] = ???

  override def eSRAB: M[Unit] = ???

  override def eSRAC: M[Unit] = ???

  override def eSRAD: M[Unit] = ???

  override def eSRAE: M[Unit] = ???

  override def eSRAH: M[Unit] = ???

  override def eSRAL: M[Unit] = ???

  override def eSRAHLm: M[Unit] = ???

  override def eSRAA: M[Unit] = ???

  override def eSWAPB: M[Unit] = ???

  override def eSWAPC: M[Unit] = ???

  override def eSWAPD: M[Unit] = ???

  override def eSWAPE: M[Unit] = ???

  override def eSWAPH: M[Unit] = ???

  override def eSWAPL: M[Unit] = ???

  override def eSWAPHLm: M[Unit] = ???

  override def eSWAPA: M[Unit] = ???

  override def eSRLB: M[Unit] = ???

  override def eSRLC: M[Unit] = ???

  override def eSRLD: M[Unit] = ???

  override def eSRLE: M[Unit] = ???

  override def eSRLH: M[Unit] = ???

  override def eSRLL: M[Unit] = ???

  override def eSRLHLm: M[Unit] = ???

  override def eSRLA: M[Unit] = ???

  override def eBIT0B: M[Unit] = ???

  override def eBIT0C: M[Unit] = ???

  override def eBIT0D: M[Unit] = ???

  override def eBIT0E: M[Unit] = ???

  override def eBIT0H: M[Unit] = ???

  override def eBIT0L: M[Unit] = ???

  override def eBIT0HLm: M[Unit] = ???

  override def eBIT0A: M[Unit] = ???

  override def eBIT1B: M[Unit] = ???

  override def eBIT1C: M[Unit] = ???

  override def eBIT1D: M[Unit] = ???

  override def eBIT1E: M[Unit] = ???

  override def eBIT1H: M[Unit] = ???

  override def eBIT1L: M[Unit] = ???

  override def eBIT1HLm: M[Unit] = ???

  override def eBIT1A: M[Unit] = ???

  override def eBIT2B: M[Unit] = ???

  override def eBIT2C: M[Unit] = ???

  override def eBIT2D: M[Unit] = ???

  override def eBIT2E: M[Unit] = ???

  override def eBIT2H: M[Unit] = ???

  override def eBIT2L: M[Unit] = ???

  override def eBIT2HLm: M[Unit] = ???

  override def eBIT2A: M[Unit] = ???

  override def eBIT3B: M[Unit] = ???

  override def eBIT3C: M[Unit] = ???

  override def eBIT3D: M[Unit] = ???

  override def eBIT3E: M[Unit] = ???

  override def eBIT3H: M[Unit] = ???

  override def eBIT3L: M[Unit] = ???

  override def eBIT3HLm: M[Unit] = ???

  override def eBIT3A: M[Unit] = ???

  override def eBIT4B: M[Unit] = ???

  override def eBIT4C: M[Unit] = ???

  override def eBIT4D: M[Unit] = ???

  override def eBIT4E: M[Unit] = ???

  override def eBIT4H: M[Unit] = ???

  override def eBIT4L: M[Unit] = ???

  override def eBIT4HLm: M[Unit] = ???

  override def eBIT4A: M[Unit] = ???

  override def eBIT5B: M[Unit] = ???

  override def eBIT5C: M[Unit] = ???

  override def eBIT5D: M[Unit] = ???

  override def eBIT5E: M[Unit] = ???

  override def eBIT5H: M[Unit] = ???

  override def eBIT5L: M[Unit] = ???

  override def eBIT5HLm: M[Unit] = ???

  override def eBIT5A: M[Unit] = ???

  override def eBIT6B: M[Unit] = ???

  override def eBIT6C: M[Unit] = ???

  override def eBIT6D: M[Unit] = ???

  override def eBIT6E: M[Unit] = ???

  override def eBIT6H: M[Unit] = ???

  override def eBIT6L: M[Unit] = ???

  override def eBIT6HLm: M[Unit] = ???

  override def eBIT6A: M[Unit] = ???

  override def eBIT7B: M[Unit] = ???

  override def eBIT7C: M[Unit] = ???

  override def eBIT7D: M[Unit] = ???

  override def eBIT7E: M[Unit] = ???

  override def eBIT7H: M[Unit] = ???

  override def eBIT7L: M[Unit] = ???

  override def eBIT7HLm: M[Unit] = ???

  override def eBIT7A: M[Unit] = ???

  override def eRES0B: M[Unit] = ???

  override def eRES0C: M[Unit] = ???

  override def eRES0D: M[Unit] = ???

  override def eRES0E: M[Unit] = ???

  override def eRES0H: M[Unit] = ???

  override def eRES0L: M[Unit] = ???

  override def eRES0HLm: M[Unit] = ???

  override def eRES0A: M[Unit] = ???

  override def eRES1B: M[Unit] = ???

  override def eRES1C: M[Unit] = ???

  override def eRES1D: M[Unit] = ???

  override def eRES1E: M[Unit] = ???

  override def eRES1H: M[Unit] = ???

  override def eRES1L: M[Unit] = ???

  override def eRES1HLm: M[Unit] = ???

  override def eRES1A: M[Unit] = ???

  override def eRES2B: M[Unit] = ???

  override def eRES2C: M[Unit] = ???

  override def eRES2D: M[Unit] = ???

  override def eRES2E: M[Unit] = ???

  override def eRES2H: M[Unit] = ???

  override def eRES2L: M[Unit] = ???

  override def eRES2HLm: M[Unit] = ???

  override def eRES2A: M[Unit] = ???

  override def eRES3B: M[Unit] = ???

  override def eRES3C: M[Unit] = ???

  override def eRES3D: M[Unit] = ???

  override def eRES3E: M[Unit] = ???

  override def eRES3H: M[Unit] = ???

  override def eRES3L: M[Unit] = ???

  override def eRES3HLm: M[Unit] = ???

  override def eRES3A: M[Unit] = ???

  override def eRES4B: M[Unit] = ???

  override def eRES4C: M[Unit] = ???

  override def eRES4D: M[Unit] = ???

  override def eRES4E: M[Unit] = ???

  override def eRES4H: M[Unit] = ???

  override def eRES4L: M[Unit] = ???

  override def eRES4HLm: M[Unit] = ???

  override def eRES4A: M[Unit] = ???

  override def eRES5B: M[Unit] = ???

  override def eRES5C: M[Unit] = ???

  override def eRES5D: M[Unit] = ???

  override def eRES5E: M[Unit] = ???

  override def eRES5H: M[Unit] = ???

  override def eRES5L: M[Unit] = ???

  override def eRES5HLm: M[Unit] = ???

  override def eRES5A: M[Unit] = ???

  override def eRES6B: M[Unit] = ???

  override def eRES6C: M[Unit] = ???

  override def eRES6D: M[Unit] = ???

  override def eRES6E: M[Unit] = ???

  override def eRES6H: M[Unit] = ???

  override def eRES6L: M[Unit] = ???

  override def eRES6HLm: M[Unit] = ???

  override def eRES6A: M[Unit] = ???

  override def eRES7B: M[Unit] = ???

  override def eRES7C: M[Unit] = ???

  override def eRES7D: M[Unit] = ???

  override def eRES7E: M[Unit] = ???

  override def eRES7H: M[Unit] = ???

  override def eRES7L: M[Unit] = ???

  override def eRES7HLm: M[Unit] = ???

  override def eRES7A: M[Unit] = ???

  override def eSET0B: M[Unit] = ???

  override def eSET0C: M[Unit] = ???

  override def eSET0D: M[Unit] = ???

  override def eSET0E: M[Unit] = ???

  override def eSET0H: M[Unit] = ???

  override def eSET0L: M[Unit] = ???

  override def eSET0HLm: M[Unit] = ???

  override def eSET0A: M[Unit] = ???

  override def eSET1B: M[Unit] = ???

  override def eSET1C: M[Unit] = ???

  override def eSET1D: M[Unit] = ???

  override def eSET1E: M[Unit] = ???

  override def eSET1H: M[Unit] = ???

  override def eSET1L: M[Unit] = ???

  override def eSET1HLm: M[Unit] = ???

  override def eSET1A: M[Unit] = ???

  override def eSET2B: M[Unit] = ???

  override def eSET2C: M[Unit] = ???

  override def eSET2D: M[Unit] = ???

  override def eSET2E: M[Unit] = ???

  override def eSET2H: M[Unit] = ???

  override def eSET2L: M[Unit] = ???

  override def eSET2HLm: M[Unit] = ???

  override def eSET2A: M[Unit] = ???

  override def eSET3B: M[Unit] = ???

  override def eSET3C: M[Unit] = ???

  override def eSET3D: M[Unit] = ???

  override def eSET3E: M[Unit] = ???

  override def eSET3H: M[Unit] = ???

  override def eSET3L: M[Unit] = ???

  override def eSET3HLm: M[Unit] = ???

  override def eSET3A: M[Unit] = ???

  override def eSET4B: M[Unit] = ???

  override def eSET4C: M[Unit] = ???

  override def eSET4D: M[Unit] = ???

  override def eSET4E: M[Unit] = ???

  override def eSET4H: M[Unit] = ???

  override def eSET4L: M[Unit] = ???

  override def eSET4HLm: M[Unit] = ???

  override def eSET4A: M[Unit] = ???

  override def eSET5B: M[Unit] = ???

  override def eSET5C: M[Unit] = ???

  override def eSET5D: M[Unit] = ???

  override def eSET5E: M[Unit] = ???

  override def eSET5H: M[Unit] = ???

  override def eSET5L: M[Unit] = ???

  override def eSET5HLm: M[Unit] = ???

  override def eSET5A: M[Unit] = ???

  override def eSET6B: M[Unit] = ???

  override def eSET6C: M[Unit] = ???

  override def eSET6D: M[Unit] = ???

  override def eSET6E: M[Unit] = ???

  override def eSET6H: M[Unit] = ???

  override def eSET6L: M[Unit] = ???

  override def eSET6HLm: M[Unit] = ???

  override def eSET6A: M[Unit] = ???

  override def eSET7B: M[Unit] = ???

  override def eSET7C: M[Unit] = ???

  override def eSET7D: M[Unit] = ???

  override def eSET7E: M[Unit] = ???

  override def eSET7H: M[Unit] = ???

  override def eSET7L: M[Unit] = ???

  override def eSET7HLm: M[Unit] = ???

  override def eSET7A: M[Unit] = ???
}
