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
//    text.print()
    //todo: 引入隐式转换
    import org.apache.flink.api.scala._
    text.flatMap(_.toLowerCase.split(" "))
      .filter(_.nonEmpty)
      .map((_,1))
      .groupBy(0)
      .sum(1).print()
  }
}
