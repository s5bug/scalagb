package tf.bug.scalagb

import cats._
import cats.data.State
import cats.implicits._

object Main {

  def main(args: Array[String]): Unit = {
    val p = program[State[GameBoy, *]]
    val endingState = p.runS(GameBoy())
    println(endingState.value.unknownOpcodes)
  }

  def program[F[_]: Monad](implicit gameBoyZ80: GameBoyZ80[F]): F[Unit] = {
    gameBoyZ80.exec.whileM_(gameBoyZ80.continue)
  }

}
