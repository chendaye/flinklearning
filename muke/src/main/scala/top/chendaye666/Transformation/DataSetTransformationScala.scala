package top.chendaye666.Transformation

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

import scala.collection.mutable.ListBuffer


object DataSetTransformationScala {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
//    mapFunction(env)
//    filterFunction(env)
    mapPartitionFunction(env)
  }

  //todo: map 算子
  def mapFunction(env:ExecutionEnvironment): Unit ={
    val data = env.fromCollection(List(1,2,3,4,5,6,7,8,9,10))
    data.map(x => x+1).print()
    data.map(_+1).print()
  }
  //todo:mapPartition 算子
  def mapPartitionFunction(env:ExecutionEnvironment): Unit ={
    val student = new ListBuffer[String]
    for(i <- 1 to 100){
      student.append("student:"+i)
    }

    val data = env.fromCollection(student).setParallelism(4) //获取dataset 并且设置并行度

    //4个分区
    data.mapPartition(x => {
      println("连接一次")
      x
    }).print()

  }


  //todo: filter 算子
  def filterFunction(env: ExecutionEnvironment): Unit ={
    val data = env.fromCollection(List(1,2,3,4,5,6,7,8,9,10))
    data.filter(_>5).print()
  }
}
