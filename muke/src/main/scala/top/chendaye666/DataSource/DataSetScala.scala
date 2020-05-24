package top.chendaye666.DataSource

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.configuration.Configuration

object DataSetScala {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
//    fromCollection(env)
//    fromTextFile(env)
//    fromCSVFile(env)
    fromRecursiveFiles(env)
  }


  /**
   * 从集合创建 collection
   * @param env
   */
  def fromCollection(env: ExecutionEnvironment): Unit ={
    import org.apache.flink.api.scala._
    val data = 1 to 10;
    env.fromCollection(data).print()
  }

  /**
   * 从文件创建 DataSet
   * @param env
   */
  def fromTextFile(env: ExecutionEnvironment): Unit ={
    val path = "muke/data/wc.txt"
//    val path = "muke/data/" // 也可以读取一个文件夹
    env.readTextFile(path).print()
  }

  def fromCSVFile(env:ExecutionEnvironment): Unit ={
    import org.apache.flink.api.scala._
    val path = "muke/data/people.csv"
    env.readCsvFile[(String, Int)](path, ignoreFirstLine = true, includedFields = Array(0,1)).print()

//    case class MyCaseClass(name:String, age:Int)
//    env.readCsvFile[MyCaseClass](path, ignoreFirstLine = true, includedFields = Array(0,1)).print()

    env.readCsvFile[Person](path, ignoreFirstLine = true, pojoFields = Array("name", "age", "work")).print()
  }

  /**
   * 递归的读取文件夹下面的文件
   * @param env
   */
  def fromRecursiveFiles(env: ExecutionEnvironment): Unit ={
    import org.apache.flink.api.scala._
    val conf = new Configuration()
    conf.setBoolean("recursive.file.enumeration", true)
    val path = "muke/data/tmp/"
    env.readTextFile(path).withParameters(conf).print()
  }
}
