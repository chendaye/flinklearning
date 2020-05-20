package top.chendaye666.quickstart

import org.apache.flink.api.scala.ExecutionEnvironment

/**
 * 使用scala 开发 flink 的批处理 wc
 */
object ScalaBatchWCJob {
  def main(args: Array[String]): Unit = {
    val path = "muke/data/wc.txt"
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.readTextFile(path)
    text.print()
  }
}
