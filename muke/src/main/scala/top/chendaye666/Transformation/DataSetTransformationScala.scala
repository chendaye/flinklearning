package top.chendaye666.Transformation

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

import scala.collection.mutable.ListBuffer


object DataSetTransformationScala {


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

  def firstnFunction(env: ExecutionEnvironment): Unit ={
    val data = ListBuffer[(Int, String)]()
    data.append((1, "hadoop"))
    data.append((1, "spark"))
    data.append((1, "storm"))
    data.append((2, "hive"))
    data.append((2, "scoop"))
    data.append((3, "hbase"))
    data.append((4, "redis"))
    data.append((4, "mysql"))

    val source = env.fromCollection(data)

//    source.first(3).print()
      source.groupBy(0).sortGroup(0, Order.ASCENDING).first(2).print()
  }

  //todo: flatMap
  def flatMapFunction(env: ExecutionEnvironment): Unit = {
    val data = ListBuffer[String]()
    data.append("hadoop,spark")
    data.append("hive,spark")
    data.append("hive,flink")
    data.append("hadoop,flink")
    val source = env.fromCollection(data)

//    source.map(_.split(",")).print()
    //todo: 首先拆分元素， 将每一个元素 包装成 元组 (_, 1) 再按第一个字段 分组， 再对第二个字段求和
    source.flatMap(_.split(",")).map((_, 1)).groupBy(0).sum(1).print()
  }

  //todo: distinct
  def distinctFunction(env: ExecutionEnvironment): Unit = {
    val data = ListBuffer[String]()
    data.append("hadoop,spark")
    data.append("hive,spark")
    data.append("hive,flink")
    data.append("hadoop,flink")
    val source = env.fromCollection(data)
    source.flatMap(_.split(",")).distinct().print()
  }

  //todo: join
  def joinFunction(env: ExecutionEnvironment): Unit = {
    val person = ListBuffer[(Int, String)]()
    person.append((1, "老王"))
    person.append((2, "老张"))
    person.append((3, "老李"))
    person.append((4, "老陈"))

    val city = ListBuffer[(Int, String)]()
    city.append((1, "北京"))
    city.append((2, "湖北"))
    city.append((3, "浙江"))
    city.append((5, "上海"))

    val source1 = env.fromCollection(person)
    val source2 = env.fromCollection(city)
    source1.join(source2).where(0).equalTo(0).apply((left, right) => {
      (left._1, left._2, right._2)
    }).print()
  }

  //todo: leftOuterJoin
  def leftJoinFunction(env: ExecutionEnvironment): Unit = {
    val person = ListBuffer[(Int, String)]()
    person.append((1, "老王"))
    person.append((2, "老张"))
    person.append((3, "老李"))
    person.append((4, "老陈"))

    val city = ListBuffer[(Int, String)]()
    city.append((1, "北京"))
    city.append((2, "湖北"))
    city.append((3, "浙江"))
    city.append((5, "上海"))

    val source1 = env.fromCollection(person)
    val source2 = env.fromCollection(city)
    source1.leftOuterJoin(source2).where(0).equalTo(0).apply((left, right) => {
      if(right == null){
        (left._1, left._2, "null")
      }else{
        (left._1, left._2, right._2)
      }

    }).print()
  }

  //todo: cross
  def crossFunction(env: ExecutionEnvironment): Unit = {
    val data1 = List("北京", "武汉")
    val data2 = List(3, 1, 0)

    val source1 = env.fromCollection(data1)
    val source2 = env.fromCollection(data2)

    source1.cross(source2).print()
  }


  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //    mapFunction(env)
    //    filterFunction(env)
    //    mapPartitionFunction(env)
//    firstnFunction(env)
//    flatMapFunction(env)
//    distinctFunction(env)
//    joinFunction(env)
//    leftJoinFunction(env)
    crossFunction(env)
  }
}
