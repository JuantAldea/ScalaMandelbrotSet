/*
 * Juan Antonio Aldea Armenteros (2013)
 */

package algorithm

import scala.annotation.tailrec

class Window(
  val pixelsHeight: Int,
  val pixelsWidth: Int,
  val xStart: Double,
  val yStart: Double,
  val xLen: Double,
  val yLen: Double)

object MandelbrotSet {
  def iterationToColor(iterations: Long, maxIterations: Long, maxColorLevel: Long): Int =
    ((iterations / maxIterations.toDouble) * maxColorLevel).floor.toInt

  def calculatePoint(cr: Double, ci: Double, maxIterations: Long): Long = {

    @tailrec
    def iterate(zr: Double, zi: Double, zrs: Double, zis: Double, currentIteration: Long): Long = {
      if (maxIterations == currentIteration) {
        maxIterations
      } else {
        if (zrs + zis < 4.0) {
          val zrsNext = zr * zr
          val zisNext = zi * zi
          val ziNext = 2.0 * zr * zi + ci
          val zrNext = zrsNext - zisNext + cr
          iterate(zrNext, ziNext, zrsNext, zisNext, currentIteration + 1)
        } else {
          currentIteration
        }
      }
    }

    def testPointInCardiodOr2ndBud(real: Double, imaginary: Double): Boolean = {
      val real2 = real - 0.25f;
      val ySquared = imaginary * imaginary
      val q = real2 * real2 + ySquared;
      (q * (q + real2)) < (0.25f * ySquared);
    }

    if (testPointInCardiodOr2ndBud(cr, ci)) {
      maxIterations
    } else {
      iterate(cr, ci, 0, 0, 0)
    }
  }

  def calculateRow(row: Int, maxIterations: Long, maxColorLevel: Int, window: Window): List[Int] = {
    val dx = window.xLen / window.pixelsWidth
    val dy = window.yLen / window.pixelsHeight
    val zi = window.yStart + row * dy;

    def rowIteration(pixel: Int): List[Long] = {
      if (pixel == window.pixelsWidth) {
        List[Long]()
      } else {
        calculatePoint(window.xStart + pixel * dx, zi, maxIterations) :: rowIteration(pixel + 1)
      }
    }
    rowIteration(0) map (iterations => iterationToColor(iterations, maxIterations, maxColorLevel: Int))
  }

  def calculateMandelbrot(maxIterations: Long, maxColorLevel: Int, window: Window): List[List[Int]] = {
    def calculateMandelbrotRow(row: Int): List[List[Int]] = {
      if (row == window.pixelsHeight) {
        List[List[Int]]()
      } else {
        List(calculateRow(row, maxIterations, maxColorLevel, window)) ::: calculateMandelbrotRow(row + 1)
      }
    }
    calculateMandelbrotRow(0)
  }
}
