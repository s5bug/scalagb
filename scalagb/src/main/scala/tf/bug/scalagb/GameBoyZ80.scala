package tf.bug.scalagb

import cats._
import cats.data.StateT
import cats.implicits._
import cats.tagless._
import spire.math.{UByte, ULong, UShort}

@finalAlg
@autoInvariantK
trait GameBoyZ80[F[_]] {

  def liftByte(value: UByte): F[UByte]
  def liftWord(value: UShort): F[UShort]

  def readByte(address: UShort): F[UByte]
  def readWord(address: UShort): F[UShort]
  def writeByte(address: UShort, value: F[UByte]): F[Unit]
  def writeWord(address: UShort, value: F[UShort]): F[Unit]

  def getA: F[UByte]
  def setA(value: F[UByte]): F[Unit]
  def getB: F[UByte]
  def setB(value: F[UByte]): F[Unit]
  def getC: F[UByte]
  def setC(value: F[UByte]): F[Unit]
  def getD: F[UByte]
  def setD(value: F[UByte]): F[Unit]
  def getE: F[UByte]
  def setE(value: F[UByte]): F[Unit]
  def getH: F[UByte]
  def setH(value: F[UByte]): F[Unit]
  def getL: F[UByte]
  def setL(value: F[UByte]): F[Unit]
  def getF: F[UByte]
  def setF(value: F[UByte]): F[Unit]

  def getPC: F[UShort]
  def setPC(value: F[UShort]): F[Unit]
  def getSP: F[UShort]
  def setSP(value: F[UShort]): F[Unit]

  def getI: F[UByte]
  def setI(value: F[UByte]): F[Unit]
  def getR: F[UByte]
  def setR(value: F[UByte]): F[Unit]

  def getM: F[UByte]
  def setM(value: F[UByte]): F[Unit]

  def reserve: F[Unit]
  def restore: F[Unit]

  def reset: F[Unit]

  def exec: F[Unit]

  def unknownOpcode(op: F[UByte]): F[Unit]

  def incrementClock(value: F[UByte]): F[Unit]

  def continue: F[Boolean]

  final val instructionVector: Vector[Option[F[Unit]]] = Vector(
    iNOP.some,
    iLDBCnn.some,
    iLDBCmA.some,
    iINCBC.some,
    iINCB.some,
    iDECB.some,
    iLDBn.some,
    iRLCA.some,
    iLDnnmSP.some,
    iADDHLBC.some,
    iLDABCm.some,
    iDECBC.some,
    iINCC.some,
    iDECC.some,
    iLDCn.some,
    iRRCA.some,
    iSTOP.some,
    iLDDEnn.some,
    iLDDEmA.some,
    iINCDE.some,
    iINCD.some,
    iDECD.some,
    iLDDn.some,
    iRLA.some,
    iJRn.some,
    iADDHLDE.some,
    iLDADEm.some,
    iDECDE.some,
    iINCE.some,
    iDECE.some,
    iLDEn.some,
    iRRA.some,
    iJRNZn.some,
    iLDHLnn.some,
    iLDIHLmA.some,
    iINCHL.some,
    iINCH.some,
    iDECH.some,
    iLDHn.some,
    iDAA.some,
    iJRZn.some,
    iADDHLHL.some,
    iLDIAHLm.some,
    iDECHL.some,
    iINCL.some,
    iDECL.some,
    iLDLn.some,
    iCPL.some,
    iJRNCn.some,
    iLDSPnn.some,
    iLDDHLmA.some,
    iINCSP.some,
    iINCHLm.some,
    iDECHLm.some,
    iLDHLmn.some,
    iSCF.some,
    iJRCn.some,
    iADDHLSP.some,
    iLDDAHLm.some,
    iDECSP.some,
    iINCA.some,
    iDECA.some,
    iLDAn.some,
    iCCF.some,
    iLDBB.some,
    iLDBC.some,
    iLDBD.some,
    iLDBE.some,
    iLDBH.some,
    iLDBL.some,
    iLDBHLm.some,
    iLDBA.some,
    iLDCB.some,
    iLDCC.some,
    iLDCD.some,
    iLDCE.some,
    iLDCH.some,
    iLDCL.some,
    iLDCHLm.some,
    iLDCA.some,
    iLDDB.some,
    iLDDC.some,
    iLDDD.some,
    iLDDE.some,
    iLDDH.some,
    iLDDL.some,
    iLDDHLm.some,
    iLDDA.some,
    iLDEB.some,
    iLDEC.some,
    iLDED.some,
    iLDEE.some,
    iLDEH.some,
    iLDEL.some,
    iLDEHLm.some,
    iLDEA.some,
    iLDHB.some,
    iLDHC.some,
    iLDHD.some,
    iLDHE.some,
    iLDHH.some,
    iLDHL.some,
    iLDHHLm.some,
    iLDHA.some,
    iLDLB.some,
    iLDLC.some,
    iLDLD.some,
    iLDLE.some,
    iLDLH.some,
    iLDLL.some,
    iLDLHLm.some,
    iLDLA.some,
    iLDHLmB.some,
    iLDHLmC.some,
    iLDHLmD.some,
    iLDHLmE.some,
    iLDHLmH.some,
    iLDHLmL.some,
    iHALT.some,
    iLDHLmA.some,
    iLDAB.some,
    iLDAC.some,
    iLDAD.some,
    iLDAE.some,
    iLDAH.some,
    iLDAL.some,
    iLDAHLm.some,
    iLDAA.some,
    iADDAB.some,
    iADDAC.some,
    iADDAD.some,
    iADDAE.some,
    iADDAH.some,
    iADDAL.some,
    iADDAHLm.some,
    iADDAA.some,
    iADCAB.some,
    iADCAC.some,
    iADCAD.some,
    iADCAE.some,
    iADCAH.some,
    iADCAL.some,
    iADCAHLm.some,
    iADCAA.some,
    iSUBAB.some,
    iSUBAC.some,
    iSUBAD.some,
    iSUBAE.some,
    iSUBAH.some,
    iSUBAL.some,
    iSUBAHLm.some,
    iSUBAA.some,
    iSBCAB.some,
    iSBCAC.some,
    iSBCAD.some,
    iSBCAE.some,
    iSBCAH.some,
    iSBCAL.some,
    iSBCAHLm.some,
    iSBCAA.some,
    iANDB.some,
    iANDC.some,
    iANDD.some,
    iANDE.some,
    iANDH.some,
    iANDL.some,
    iANDHLm.some,
    iANDA.some,
    iXORB.some,
    iXORC.some,
    iXORD.some,
    iXORE.some,
    iXORH.some,
    iXORL.some,
    iXORHLm.some,
    iXORA.some,
    iORB.some,
    iORC.some,
    iORD.some,
    iORE.some,
    iORH.some,
    iORL.some,
    iORHLm.some,
    iORA.some,
    iCMPB.some,
    iCMPC.some,
    iCMPD.some,
    iCMPE.some,
    iCMPH.some,
    iCMPL.some,
    iCMPHLm.some,
    iCMPA.some,
    iRETNZ.some,
    iPOPBC.some,
    iJPNZnn.some,
    iJPnn.some,
    iCALLNZnn.some,
    iPUSHBC.some,
    iADDAn.some,
    iRST0.some,
    iRETZ.some,
    iRET.some,
    iJPZnn.some,
    iExtensions.some,
    iCALLZnn.some,
    iCALLnn.some,
    iADCAn.some,
    iRST8.some,
    iRETNC.some,
    iPOPDE.some,
    iJPNCnn.some,
    None,
    iCALLNCnn.some,
    iPUSHDE.some,
    iSUBAn.some,
    iRST10.some,
    iRETC.some,
    iRETI.some,
    iJPCnn.some,
    None,
    iCALLCnn.some,
    None,
    iSBCAn.some,
    iRST18.some,
    iLDHnmA.some,
    iPOPHL.some,
    iLDHCmA.some,
    None,
    None,
    iPUSHHL.some,
    iANDn.some,
    iRST20.some,
    iADDSPd.some,
    iJPHLm.some,
    iLDnnmA.some,
    None,
    None,
    None,
    iXORn.some,
    iRST28.some,
    iLDHAnm.some,
    iPOPAF.some,
    None,
    iDI.some,
    None,
    iPUSHAF.some,
    iORn.some,
    iRST30.some,
    iLDHLSPd.some,
    iLDSPHL.some,
    iLDAnnm.some,
    iEI.some,
    None,
    None,
    iCPn.some,
    iRST38.some
  )
  final val extensionVector: Vector[F[Unit]] = Vector(
    eRLCB,
    eRLCC,
    eRLCD,
    eRLCE,
    eRLCH,
    eRLCL,
    eRLCHLm,
    eRLCA,
    eRRCB,
    eRRCC,
    eRRCD,
    eRRCE,
    eRRCH,
    eRRCL,
    eRRCHLm,
    eRRCA,
    eRLB,
    eRLC,
    eRLD,
    eRLE,
    eRLH,
    eRLL,
    eRLHLm,
    eRLA,
    eRRB,
    eRRC,
    eRRD,
    eRRE,
    eRRH,
    eRRL,
    eRRHLm,
    eRRA,
    eSLAB,
    eSLAC,
    eSLAD,
    eSLAE,
    eSLAH,
    eSLAL,
    eSLAHLm,
    eSLAA,
    eSRAB,
    eSRAC,
    eSRAD,
    eSRAE,
    eSRAH,
    eSRAL,
    eSRAHLm,
    eSRAA,
    eSWAPB,
    eSWAPC,
    eSWAPD,
    eSWAPE,
    eSWAPH,
    eSWAPL,
    eSWAPHLm,
    eSWAPA,
    eSRLB,
    eSRLC,
    eSRLD,
    eSRLE,
    eSRLH,
    eSRLL,
    eSRLHLm,
    eSRLA,
    eBIT0B,
    eBIT0C,
    eBIT0D,
    eBIT0E,
    eBIT0H,
    eBIT0L,
    eBIT0HLm,
    eBIT0A,
    eBIT1B,
    eBIT1C,
    eBIT1D,
    eBIT1E,
    eBIT1H,
    eBIT1L,
    eBIT1HLm,
    eBIT1A,
    eBIT2B,
    eBIT2C,
    eBIT2D,
    eBIT2E,
    eBIT2H,
    eBIT2L,
    eBIT2HLm,
    eBIT2A,
    eBIT3B,
    eBIT3C,
    eBIT3D,
    eBIT3E,
    eBIT3H,
    eBIT3L,
    eBIT3HLm,
    eBIT3A,
    eBIT4B,
    eBIT4C,
    eBIT4D,
    eBIT4E,
    eBIT4H,
    eBIT4L,
    eBIT4HLm,
    eBIT4A,
    eBIT5B,
    eBIT5C,
    eBIT5D,
    eBIT5E,
    eBIT5H,
    eBIT5L,
    eBIT5HLm,
    eBIT5A,
    eBIT6B,
    eBIT6C,
    eBIT6D,
    eBIT6E,
    eBIT6H,
    eBIT6L,
    eBIT6HLm,
    eBIT6A,
    eBIT7B,
    eBIT7C,
    eBIT7D,
    eBIT7E,
    eBIT7H,
    eBIT7L,
    eBIT7HLm,
    eBIT7A,
    eRES0B,
    eRES0C,
    eRES0D,
    eRES0E,
    eRES0H,
    eRES0L,
    eRES0HLm,
    eRES0A,
    eRES1B,
    eRES1C,
    eRES1D,
    eRES1E,
    eRES1H,
    eRES1L,
    eRES1HLm,
    eRES1A,
    eRES2B,
    eRES2C,
    eRES2D,
    eRES2E,
    eRES2H,
    eRES2L,
    eRES2HLm,
    eRES2A,
    eRES3B,
    eRES3C,
    eRES3D,
    eRES3E,
    eRES3H,
    eRES3L,
    eRES3HLm,
    eRES3A,
    eRES4B,
    eRES4C,
    eRES4D,
    eRES4E,
    eRES4H,
    eRES4L,
    eRES4HLm,
    eRES4A,
    eRES5B,
    eRES5C,
    eRES5D,
    eRES5E,
    eRES5H,
    eRES5L,
    eRES5HLm,
    eRES5A,
    eRES6B,
    eRES6C,
    eRES6D,
    eRES6E,
    eRES6H,
    eRES6L,
    eRES6HLm,
    eRES6A,
    eRES7B,
    eRES7C,
    eRES7D,
    eRES7E,
    eRES7H,
    eRES7L,
    eRES7HLm,
    eRES7A,
    eSET0B,
    eSET0C,
    eSET0D,
    eSET0E,
    eSET0H,
    eSET0L,
    eSET0HLm,
    eSET0A,
    eSET1B,
    eSET1C,
    eSET1D,
    eSET1E,
    eSET1H,
    eSET1L,
    eSET1HLm,
    eSET1A,
    eSET2B,
    eSET2C,
    eSET2D,
    eSET2E,
    eSET2H,
    eSET2L,
    eSET2HLm,
    eSET2A,
    eSET3B,
    eSET3C,
    eSET3D,
    eSET3E,
    eSET3H,
    eSET3L,
    eSET3HLm,
    eSET3A,
    eSET4B,
    eSET4C,
    eSET4D,
    eSET4E,
    eSET4H,
    eSET4L,
    eSET4HLm,
    eSET4A,
    eSET5B,
    eSET5C,
    eSET5D,
    eSET5E,
    eSET5H,
    eSET5L,
    eSET5HLm,
    eSET5A,
    eSET6B,
    eSET6C,
    eSET6D,
    eSET6E,
    eSET6H,
    eSET6L,
    eSET6HLm,
    eSET6A,
    eSET7B,
    eSET7C,
    eSET7D,
    eSET7E,
    eSET7H,
    eSET7L,
    eSET7HLm,
    eSET7A,
  )

  // 00-0F
  def iNOP: F[Unit]
  def iLDBCnn: F[Unit]
  def iLDBCmA: F[Unit]
  def iINCBC: F[Unit]
  def iINCB: F[Unit]
  def iDECB: F[Unit]
  def iLDBn: F[Unit]
  def iRLCA: F[Unit]
  def iLDnnmSP: F[Unit]
  def iADDHLBC: F[Unit]
  def iLDABCm: F[Unit]
  def iDECBC: F[Unit]
  def iINCC: F[Unit]
  def iDECC: F[Unit]
  def iLDCn: F[Unit]
  def iRRCA: F[Unit]
  // 10-1F
  def iSTOP: F[Unit]
  def iLDDEnn: F[Unit]
  def iLDDEmA: F[Unit]
  def iINCDE: F[Unit]
  def iINCD: F[Unit]
  def iDECD: F[Unit]
  def iLDDn: F[Unit]
  def iRLA: F[Unit]
  def iJRn: F[Unit]
  def iADDHLDE: F[Unit]
  def iLDADEm: F[Unit]
  def iDECDE: F[Unit]
  def iINCE: F[Unit]
  def iDECE: F[Unit]
  def iLDEn: F[Unit]
  def iRRA: F[Unit]
  // 20-2F
  def iJRNZn: F[Unit]
  def iLDHLnn: F[Unit]
  def iLDIHLmA: F[Unit]
  def iINCHL: F[Unit]
  def iINCH: F[Unit]
  def iDECH: F[Unit]
  def iLDHn: F[Unit]
  def iDAA: F[Unit]
  def iJRZn: F[Unit]
  def iADDHLHL: F[Unit]
  def iLDIAHLm: F[Unit]
  def iDECHL: F[Unit]
  def iINCL: F[Unit]
  def iDECL: F[Unit]
  def iLDLn: F[Unit]
  def iCPL: F[Unit]
  // 30-3F
  def iJRNCn: F[Unit]
  def iLDSPnn: F[Unit]
  def iLDDHLmA: F[Unit]
  def iINCSP: F[Unit]
  def iINCHLm: F[Unit]
  def iDECHLm: F[Unit]
  def iLDHLmn: F[Unit]
  def iSCF: F[Unit]
  def iJRCn: F[Unit]
  def iADDHLSP: F[Unit]
  def iLDDAHLm: F[Unit]
  def iDECSP: F[Unit]
  def iINCA: F[Unit]
  def iDECA: F[Unit]
  def iLDAn: F[Unit]
  def iCCF: F[Unit]
  // 40-4F
  def iLDBB: F[Unit]
  def iLDBC: F[Unit]
  def iLDBD: F[Unit]
  def iLDBE: F[Unit]
  def iLDBH: F[Unit]
  def iLDBL: F[Unit]
  def iLDBHLm: F[Unit]
  def iLDBA: F[Unit]
  def iLDCB: F[Unit]
  def iLDCC: F[Unit]
  def iLDCD: F[Unit]
  def iLDCE: F[Unit]
  def iLDCH: F[Unit]
  def iLDCL: F[Unit]
  def iLDCHLm: F[Unit]
  def iLDCA: F[Unit]
  // 50-5F
  def iLDDB: F[Unit]
  def iLDDC: F[Unit]
  def iLDDD: F[Unit]
  def iLDDE: F[Unit]
  def iLDDH: F[Unit]
  def iLDDL: F[Unit]
  def iLDDHLm: F[Unit]
  def iLDDA: F[Unit]
  def iLDEB: F[Unit]
  def iLDEC: F[Unit]
  def iLDED: F[Unit]
  def iLDEE: F[Unit]
  def iLDEH: F[Unit]
  def iLDEL: F[Unit]
  def iLDEHLm: F[Unit]
  def iLDEA: F[Unit]
  // 60-6F
  def iLDHB: F[Unit]
  def iLDHC: F[Unit]
  def iLDHD: F[Unit]
  def iLDHE: F[Unit]
  def iLDHH: F[Unit]
  def iLDHL: F[Unit]
  def iLDHHLm: F[Unit]
  def iLDHA: F[Unit]
  def iLDLB: F[Unit]
  def iLDLC: F[Unit]
  def iLDLD: F[Unit]
  def iLDLE: F[Unit]
  def iLDLH: F[Unit]
  def iLDLL: F[Unit]
  def iLDLHLm: F[Unit]
  def iLDLA: F[Unit]
  // 70-7F
  def iLDHLmB: F[Unit]
  def iLDHLmC: F[Unit]
  def iLDHLmD: F[Unit]
  def iLDHLmE: F[Unit]
  def iLDHLmH: F[Unit]
  def iLDHLmL: F[Unit]
  def iHALT: F[Unit]
  def iLDHLmA: F[Unit]
  def iLDAB: F[Unit]
  def iLDAC: F[Unit]
  def iLDAD: F[Unit]
  def iLDAE: F[Unit]
  def iLDAH: F[Unit]
  def iLDAL: F[Unit]
  def iLDAHLm: F[Unit]
  def iLDAA: F[Unit]
  // 80-8F
  def iADDAB: F[Unit]
  def iADDAC: F[Unit]
  def iADDAD: F[Unit]
  def iADDAE: F[Unit]
  def iADDAH: F[Unit]
  def iADDAL: F[Unit]
  def iADDAHLm: F[Unit]
  def iADDAA: F[Unit]
  def iADCAB: F[Unit]
  def iADCAC: F[Unit]
  def iADCAD: F[Unit]
  def iADCAE: F[Unit]
  def iADCAH: F[Unit]
  def iADCAL: F[Unit]
  def iADCAHLm: F[Unit]
  def iADCAA: F[Unit]
  // 90-9F
  def iSUBAB: F[Unit]
  def iSUBAC: F[Unit]
  def iSUBAD: F[Unit]
  def iSUBAE: F[Unit]
  def iSUBAH: F[Unit]
  def iSUBAL: F[Unit]
  def iSUBAHLm: F[Unit]
  def iSUBAA: F[Unit]
  def iSBCAB: F[Unit]
  def iSBCAC: F[Unit]
  def iSBCAD: F[Unit]
  def iSBCAE: F[Unit]
  def iSBCAH: F[Unit]
  def iSBCAL: F[Unit]
  def iSBCAHLm: F[Unit]
  def iSBCAA: F[Unit]
  // A0-AF
  def iANDB: F[Unit]
  def iANDC: F[Unit]
  def iANDD: F[Unit]
  def iANDE: F[Unit]
  def iANDH: F[Unit]
  def iANDL: F[Unit]
  def iANDHLm: F[Unit]
  def iANDA: F[Unit]
  def iXORB: F[Unit]
  def iXORC: F[Unit]
  def iXORD: F[Unit]
  def iXORE: F[Unit]
  def iXORH: F[Unit]
  def iXORL: F[Unit]
  def iXORHLm: F[Unit]
  def iXORA: F[Unit]
  // B0-BF
  def iORB: F[Unit]
  def iORC: F[Unit]
  def iORD: F[Unit]
  def iORE: F[Unit]
  def iORH: F[Unit]
  def iORL: F[Unit]
  def iORHLm: F[Unit]
  def iORA: F[Unit]
  def iCMPB: F[Unit]
  def iCMPC: F[Unit]
  def iCMPD: F[Unit]
  def iCMPE: F[Unit]
  def iCMPH: F[Unit]
  def iCMPL: F[Unit]
  def iCMPHLm: F[Unit]
  def iCMPA: F[Unit]
  // C0-CF
  def iRETNZ: F[Unit]
  def iPOPBC: F[Unit]
  def iJPNZnn: F[Unit]
  def iJPnn: F[Unit]
  def iCALLNZnn: F[Unit]
  def iPUSHBC: F[Unit]
  def iADDAn: F[Unit]
  def iRST0: F[Unit]
  def iRETZ: F[Unit]
  def iRET: F[Unit]
  def iJPZnn: F[Unit]
  def iExtensions: F[Unit]
  def iCALLZnn: F[Unit]
  def iCALLnn: F[Unit]
  def iADCAn: F[Unit]
  def iRST8: F[Unit]
  // D0-DF
  def iRETNC: F[Unit]
  def iPOPDE: F[Unit]
  def iJPNCnn: F[Unit]
  // Undefined
  def iCALLNCnn: F[Unit]
  def iPUSHDE: F[Unit]
  def iSUBAn: F[Unit]
  def iRST10: F[Unit]
  def iRETC: F[Unit]
  def iRETI: F[Unit]
  def iJPCnn: F[Unit]
  // Undefined
  def iCALLCnn: F[Unit]
  // Undefined
  def iSBCAn: F[Unit]
  def iRST18: F[Unit]
  // E0-EF
  def iLDHnmA: F[Unit]
  def iPOPHL: F[Unit]
  def iLDHCmA: F[Unit]
  // Undefined
  // Undefined
  def iPUSHHL: F[Unit]
  def iANDn: F[Unit]
  def iRST20: F[Unit]
  def iADDSPd: F[Unit]
  def iJPHLm: F[Unit]
  def iLDnnmA: F[Unit]
  // Undefined
  // Undefined
  // Undefined
  def iXORn: F[Unit]
  def iRST28: F[Unit]
  // F0-FF
  def iLDHAnm: F[Unit]
  def iPOPAF: F[Unit]
  // Undefined
  def iDI: F[Unit]
  // Undefined
  def iPUSHAF: F[Unit]
  def iORn: F[Unit]
  def iRST30: F[Unit]
  def iLDHLSPd: F[Unit]
  def iLDSPHL: F[Unit]
  def iLDAnnm: F[Unit]
  def iEI: F[Unit]
  // Undefined
  // Undefined
  def iCPn: F[Unit]
  def iRST38: F[Unit]

  // 00-0F
  def eRLCB: F[Unit]
  def eRLCC: F[Unit]
  def eRLCD: F[Unit]
  def eRLCE: F[Unit]
  def eRLCH: F[Unit]
  def eRLCL: F[Unit]
  def eRLCHLm: F[Unit]
  def eRLCA: F[Unit]
  def eRRCB: F[Unit]
  def eRRCC: F[Unit]
  def eRRCD: F[Unit]
  def eRRCE: F[Unit]
  def eRRCH: F[Unit]
  def eRRCL: F[Unit]
  def eRRCHLm: F[Unit]
  def eRRCA: F[Unit]
  // 10-1F
  def eRLB: F[Unit]
  def eRLC: F[Unit]
  def eRLD: F[Unit]
  def eRLE: F[Unit]
  def eRLH: F[Unit]
  def eRLL: F[Unit]
  def eRLHLm: F[Unit]
  def eRLA: F[Unit]
  def eRRB: F[Unit]
  def eRRC: F[Unit]
  def eRRD: F[Unit]
  def eRRE: F[Unit]
  def eRRH: F[Unit]
  def eRRL: F[Unit]
  def eRRHLm: F[Unit]
  def eRRA: F[Unit]
  // 20-2F
  def eSLAB: F[Unit]
  def eSLAC: F[Unit]
  def eSLAD: F[Unit]
  def eSLAE: F[Unit]
  def eSLAH: F[Unit]
  def eSLAL: F[Unit]
  def eSLAHLm: F[Unit]
  def eSLAA: F[Unit]
  def eSRAB: F[Unit]
  def eSRAC: F[Unit]
  def eSRAD: F[Unit]
  def eSRAE: F[Unit]
  def eSRAH: F[Unit]
  def eSRAL: F[Unit]
  def eSRAHLm: F[Unit]
  def eSRAA: F[Unit]
  // 30-3F
  def eSWAPB: F[Unit]
  def eSWAPC: F[Unit]
  def eSWAPD: F[Unit]
  def eSWAPE: F[Unit]
  def eSWAPH: F[Unit]
  def eSWAPL: F[Unit]
  def eSWAPHLm: F[Unit]
  def eSWAPA: F[Unit]
  def eSRLB: F[Unit]
  def eSRLC: F[Unit]
  def eSRLD: F[Unit]
  def eSRLE: F[Unit]
  def eSRLH: F[Unit]
  def eSRLL: F[Unit]
  def eSRLHLm: F[Unit]
  def eSRLA: F[Unit]
  // 40-4F
  def eBIT0B: F[Unit]
  def eBIT0C: F[Unit]
  def eBIT0D: F[Unit]
  def eBIT0E: F[Unit]
  def eBIT0H: F[Unit]
  def eBIT0L: F[Unit]
  def eBIT0HLm: F[Unit]
  def eBIT0A: F[Unit]
  def eBIT1B: F[Unit]
  def eBIT1C: F[Unit]
  def eBIT1D: F[Unit]
  def eBIT1E: F[Unit]
  def eBIT1H: F[Unit]
  def eBIT1L: F[Unit]
  def eBIT1HLm: F[Unit]
  def eBIT1A: F[Unit]
  // 50-5F
  def eBIT2B: F[Unit]
  def eBIT2C: F[Unit]
  def eBIT2D: F[Unit]
  def eBIT2E: F[Unit]
  def eBIT2H: F[Unit]
  def eBIT2L: F[Unit]
  def eBIT2HLm: F[Unit]
  def eBIT2A: F[Unit]
  def eBIT3B: F[Unit]
  def eBIT3C: F[Unit]
  def eBIT3D: F[Unit]
  def eBIT3E: F[Unit]
  def eBIT3H: F[Unit]
  def eBIT3L: F[Unit]
  def eBIT3HLm: F[Unit]
  def eBIT3A: F[Unit]
  // 60-6F
  def eBIT4B: F[Unit]
  def eBIT4C: F[Unit]
  def eBIT4D: F[Unit]
  def eBIT4E: F[Unit]
  def eBIT4H: F[Unit]
  def eBIT4L: F[Unit]
  def eBIT4HLm: F[Unit]
  def eBIT4A: F[Unit]
  def eBIT5B: F[Unit]
  def eBIT5C: F[Unit]
  def eBIT5D: F[Unit]
  def eBIT5E: F[Unit]
  def eBIT5H: F[Unit]
  def eBIT5L: F[Unit]
  def eBIT5HLm: F[Unit]
  def eBIT5A: F[Unit]
  // 70-7F
  def eBIT6B: F[Unit]
  def eBIT6C: F[Unit]
  def eBIT6D: F[Unit]
  def eBIT6E: F[Unit]
  def eBIT6H: F[Unit]
  def eBIT6L: F[Unit]
  def eBIT6HLm: F[Unit]
  def eBIT6A: F[Unit]
  def eBIT7B: F[Unit]
  def eBIT7C: F[Unit]
  def eBIT7D: F[Unit]
  def eBIT7E: F[Unit]
  def eBIT7H: F[Unit]
  def eBIT7L: F[Unit]
  def eBIT7HLm: F[Unit]
  def eBIT7A: F[Unit]
  // 80-8F
  def eRES0B: F[Unit]
  def eRES0C: F[Unit]
  def eRES0D: F[Unit]
  def eRES0E: F[Unit]
  def eRES0H: F[Unit]
  def eRES0L: F[Unit]
  def eRES0HLm: F[Unit]
  def eRES0A: F[Unit]
  def eRES1B: F[Unit]
  def eRES1C: F[Unit]
  def eRES1D: F[Unit]
  def eRES1E: F[Unit]
  def eRES1H: F[Unit]
  def eRES1L: F[Unit]
  def eRES1HLm: F[Unit]
  def eRES1A: F[Unit]
  // 90-9F
  def eRES2B: F[Unit]
  def eRES2C: F[Unit]
  def eRES2D: F[Unit]
  def eRES2E: F[Unit]
  def eRES2H: F[Unit]
  def eRES2L: F[Unit]
  def eRES2HLm: F[Unit]
  def eRES2A: F[Unit]
  def eRES3B: F[Unit]
  def eRES3C: F[Unit]
  def eRES3D: F[Unit]
  def eRES3E: F[Unit]
  def eRES3H: F[Unit]
  def eRES3L: F[Unit]
  def eRES3HLm: F[Unit]
  def eRES3A: F[Unit]
  // A0-AF
  def eRES4B: F[Unit]
  def eRES4C: F[Unit]
  def eRES4D: F[Unit]
  def eRES4E: F[Unit]
  def eRES4H: F[Unit]
  def eRES4L: F[Unit]
  def eRES4HLm: F[Unit]
  def eRES4A: F[Unit]
  def eRES5B: F[Unit]
  def eRES5C: F[Unit]
  def eRES5D: F[Unit]
  def eRES5E: F[Unit]
  def eRES5H: F[Unit]
  def eRES5L: F[Unit]
  def eRES5HLm: F[Unit]
  def eRES5A: F[Unit]
  // B0-BF
  def eRES6B: F[Unit]
  def eRES6C: F[Unit]
  def eRES6D: F[Unit]
  def eRES6E: F[Unit]
  def eRES6H: F[Unit]
  def eRES6L: F[Unit]
  def eRES6HLm: F[Unit]
  def eRES6A: F[Unit]
  def eRES7B: F[Unit]
  def eRES7C: F[Unit]
  def eRES7D: F[Unit]
  def eRES7E: F[Unit]
  def eRES7H: F[Unit]
  def eRES7L: F[Unit]
  def eRES7HLm: F[Unit]
  def eRES7A: F[Unit]
  // C0-CF
  def eSET0B: F[Unit]
  def eSET0C: F[Unit]
  def eSET0D: F[Unit]
  def eSET0E: F[Unit]
  def eSET0H: F[Unit]
  def eSET0L: F[Unit]
  def eSET0HLm: F[Unit]
  def eSET0A: F[Unit]
  def eSET1B: F[Unit]
  def eSET1C: F[Unit]
  def eSET1D: F[Unit]
  def eSET1E: F[Unit]
  def eSET1H: F[Unit]
  def eSET1L: F[Unit]
  def eSET1HLm: F[Unit]
  def eSET1A: F[Unit]
  // D0-DF
  def eSET2B: F[Unit]
  def eSET2C: F[Unit]
  def eSET2D: F[Unit]
  def eSET2E: F[Unit]
  def eSET2H: F[Unit]
  def eSET2L: F[Unit]
  def eSET2HLm: F[Unit]
  def eSET2A: F[Unit]
  def eSET3B: F[Unit]
  def eSET3C: F[Unit]
  def eSET3D: F[Unit]
  def eSET3E: F[Unit]
  def eSET3H: F[Unit]
  def eSET3L: F[Unit]
  def eSET3HLm: F[Unit]
  def eSET3A: F[Unit]
  // E0-EF
  def eSET4B: F[Unit]
  def eSET4C: F[Unit]
  def eSET4D: F[Unit]
  def eSET4E: F[Unit]
  def eSET4H: F[Unit]
  def eSET4L: F[Unit]
  def eSET4HLm: F[Unit]
  def eSET4A: F[Unit]
  def eSET5B: F[Unit]
  def eSET5C: F[Unit]
  def eSET5D: F[Unit]
  def eSET5E: F[Unit]
  def eSET5H: F[Unit]
  def eSET5L: F[Unit]
  def eSET5HLm: F[Unit]
  def eSET5A: F[Unit]
  // F0-FF
  def eSET6B: F[Unit]
  def eSET6C: F[Unit]
  def eSET6D: F[Unit]
  def eSET6E: F[Unit]
  def eSET6H: F[Unit]
  def eSET6L: F[Unit]
  def eSET6HLm: F[Unit]
  def eSET6A: F[Unit]
  def eSET7B: F[Unit]
  def eSET7C: F[Unit]
  def eSET7D: F[Unit]
  def eSET7E: F[Unit]
  def eSET7H: F[Unit]
  def eSET7L: F[Unit]
  def eSET7HLm: F[Unit]
  def eSET7A: F[Unit]

}

object GameBoyZ80 {

  implicit def gameBoyStateTGameBoyZ80[M[_]](implicit monad: Monad[M]): GameBoyZ80[StateT[M, GameBoy, *]] =
    new MonadGameBoyZ80[StateT[M, GameBoy, *]] {

      override def readByte(address: UShort): StateT[M, GameBoy, UByte] =
        StateT.inspect(_.read(address))

      override def readWord(address: UShort): StateT[M, GameBoy, UShort] = for {
        low <- StateT.inspect[M, GameBoy, UByte](_.read(address))
        high <- StateT.inspect[M, GameBoy, UByte](_.read(address + UShort(1)))
      } yield uBytesToUShort(high, low)

      override def writeByte(address: UShort, value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(byte => StateT.modify(_.write(address, byte)))

      override def writeWord(address: UShort, value: StateT[M, GameBoy, UShort]): StateT[M, GameBoy, Unit] =
        for {
          word <- value
          (high, low) = uShortToUBytes(word)
          _ <- StateT.modify[M, GameBoy](_.write(address, low))
          _ <- StateT.modify[M, GameBoy](_.write(address + UShort(1), high))
        } yield ()

      override def unknownOpcode(op: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        op.flatMap { op =>
          getPC.flatMap { pc =>
            val previousPC = pc - UShort(1)
            StateT.modify { gb =>
              val setErrorFlag = gb.copy(error = true)
              setErrorFlag.copy(unknownOpcodes = setErrorFlag.unknownOpcodes :+ ((previousPC, op)))
            }
          }
        }


      override def incrementClock(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(addition => StateT.modify(gb => gb.copy(clockM = gb.clockM + ULong(addition.toLong))))

      override def continue: StateT[M, GameBoy, Boolean] =
        StateT.inspect(gb => !gb.error && !gb.stop && !gb.halt)

      override def getA: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.a)

      override def setA(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newA => StateT.modify(_.withA(newA)))

      override def getB: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.b)

      override def setB(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newB => StateT.modify(_.withB(newB)))

      override def getC: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.c)

      override def setC(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newC => StateT.modify(_.withC(newC)))

      override def getD: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.d)

      override def setD(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newD => StateT.modify(_.withD(newD)))

      override def getE: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.e)

      override def setE(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newE => StateT.modify(_.withE(newE)))

      override def getH: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.h)

      override def setH(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newH => StateT.modify(_.withH(newH)))

      override def getL: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.l)

      override def setL(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newL => StateT.modify(_.withL(newL)))

      override def getF: StateT[M, GameBoy, UByte] = StateT.inspect(_.registers.f)

      override def setF(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newF => StateT.modify(_.withF(newF)))

      override def getPC: StateT[M, GameBoy, UShort] = StateT.inspect(_.pc)

      override def setPC(value: StateT[M, GameBoy, UShort]): StateT[M, GameBoy, Unit] =
        value.flatMap(newPC => StateT.modify(_.copy(pc = newPC)))

      override def getSP: StateT[M, GameBoy, UShort] = StateT.inspect(_.sp)

      override def setSP(value: StateT[M, GameBoy, UShort]): StateT[M, GameBoy, Unit] =
        value.flatMap(newSP => StateT.modify(_.copy(sp = newSP)))

      override def getI: StateT[M, GameBoy, UByte] = StateT.inspect(_.i)

      override def setI(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newI => StateT.modify(_.copy(i = newI)))

      override def getR: StateT[M, GameBoy, UByte] = StateT.inspect(_.r)

      override def setR(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newR => StateT.modify(_.copy(r = newR)))

      override def getM: StateT[M, GameBoy, UByte] = StateT.inspect(_.m)

      override def setM(value: StateT[M, GameBoy, UByte]): StateT[M, GameBoy, Unit] =
        value.flatMap(newM => StateT.modify(_.copy(m = newM)))

      override def reserve: StateT[M, GameBoy, Unit] = StateT.modify(_.reserve)

      override def restore: StateT[M, GameBoy, Unit] = StateT.modify(_.reserve)

    }

}
