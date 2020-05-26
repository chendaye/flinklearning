package top.chendaye666.Transformation;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class DataSetTransformationJava {


    //todo: map
    public static void mapFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<=10; i++)
            list.add(i);
        DataSource<Integer> data = env.fromCollection(list);

        data.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer integer) throws Exception {
                return integer+1;
            }
        }).print();
    }

    //todo: MapPartition
    public static void mapPartitionFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<=100; i++)
            list.add(i);
        DataSource<Integer> data = env.fromCollection(list).setParallelism(4);

        data.mapPartition(new MapPartitionFunction<Integer, Integer>() {
            @Override
            public void mapPartition(Iterable<Integer> iterable, Collector<Integer> collector) throws Exception {
                System.out.println("连接一次");
            }
        }).print();
    }

    //todo:filter
    public static void filterFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<=10; i++)
            list.add(i);
        DataSource<Integer> data = env.fromCollection(list);

        data.map(new MapFunction<Integer, Integer>() {
            @Override
            public Integer map(Integer integer) throws Exception {
                return integer+1;
            }
        }).filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer integer) throws Exception {
                return integer>5;
            }
        }).print();
    }

    //todo: first
    public static void firstnFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Tuple2<Integer, String>> data = new ArrayList<>();
        data.add(new Tuple2<>(1, "hadoop"));
        data.add(new Tuple2<>(1, "spark"));
        data.add(new Tuple2<>(1, "storm"));
        data.add(new Tuple2<>(2, "hive"));
        data.add(new Tuple2<>(2, "scoop"));
        data.add(new Tuple2<>(3, "hbase"));
        data.add(new Tuple2<>(4, "redis"));
        data.add(new Tuple2<>(4, "mysql"));

        DataSource<Tuple2<Integer, String>> source = env.fromCollection(data);
        source.first(3).print();
        source.groupBy(0).first(2).print();
        source.groupBy(0).sortGroup(1, Order.ASCENDING).first(1).print();
    }

    //todo: flatMap
    public static void flatMapFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<String> data = new ArrayList<>();
        data.add("hadoop,spark");
        data.add("hive,spark");
        data.add("hive,flink");
        data.add("hadoop,flink");
        DataSource<String> source = env.fromCollection(data);

        source.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String input, Collector<String> collector) throws Exception {
                String[] split = input.split(",");
                for (String s:split){
                    collector.collect(s);
                }
            }
        }).map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        }).groupBy(0)
                .sum(1)
                .print();
    }

    //todo: distinct
    public static void distinctFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<String> data = new ArrayList<>();
        data.add("hadoop,spark");
        data.add("hive,spark");
        data.add("hive,flink");
        data.add("hadoop,flink");
        DataSource<String> source = env.fromCollection(data);

        source.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String input, Collector<String> collector) throws Exception {
                String[] split = input.split(",");
                for (String s:split){
                    collector.collect(s);
                }
            }
        }).distinct().print();
    }

    //todo: join
    public static void joinFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Tuple2<Integer, String>> person = new ArrayList<>();
        person.add(new Tuple2<>(1, "小红"));
        person.add(new Tuple2<>(2, "小明"));
        person.add(new Tuple2<>(3, "小王"));
        person.add(new Tuple2<>(4, "小李"));

        ArrayList<Tuple2<Integer, String>> city = new ArrayList<>();
        city.add(new Tuple2<>(1, "上海"));
        city.add(new Tuple2<>(2, "北京"));
        city.add(new Tuple2<>(3, "武汉"));
        city.add(new Tuple2<>(5, "南京"));

        DataSource<Tuple2<Integer, String>> source1 = env.fromCollection(person);
        DataSource<Tuple2<Integer, String>> source2 = env.fromCollection(city);

        source1.join(source2).where(0).equalTo(0)
                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
                    @Override
                    public Tuple3<Integer, String, String> join(Tuple2<Integer, String> left, Tuple2<Integer, String> right) throws Exception {
                        return new Tuple3<Integer, String, String>(left.f0, left.f1, right.f1);
                    }
                }).print();

    }

    //todo: outJoin
    public static void outJoinFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<Tuple2<Integer, String>> person = new ArrayList<>();
        person.add(new Tuple2<>(1, "小红"));
        person.add(new Tuple2<>(2, "小明"));
        person.add(new Tuple2<>(3, "小王"));
        person.add(new Tuple2<>(4, "小李"));

        ArrayList<Tuple2<Integer, String>> city = new ArrayList<>();
        city.add(new Tuple2<>(1, "上海"));
        city.add(new Tuple2<>(2, "北京"));
        city.add(new Tuple2<>(3, "武汉"));
        city.add(new Tuple2<>(5, "南京"));

        DataSource<Tuple2<Integer, String>> source1 = env.fromCollection(person);
        DataSource<Tuple2<Integer, String>> source2 = env.fromCollection(city);

        source1.leftOuterJoin(source2).where(0).equalTo(0)
                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Tuple3<Integer, String, String>>() {
                    @Override
                    public Tuple3<Integer, String, String> join(Tuple2<Integer, String> left, Tuple2<Integer, String> right) throws Exception {

                        if (right != null)
                            return new Tuple3<Integer, String, String>(left.f0, left.f1, right.f1);
                        else
                            return new Tuple3<Integer, String, String>(left.f0, left.f1,"null");
                    }
                }).print();

    }

    //todo: cross
    public static void crossFunction(ExecutionEnvironment env) throws Exception {
        ArrayList<String> person = new ArrayList<>();
        person.add("北京");
        person.add("上海");
        person.add("深圳");

        ArrayList<String> city = new ArrayList<>();
        city.add("1");
        city.add("2");
        city.add("3");

        DataSource<String> source1 = env.fromCollection(person);
        DataSource<String> source2 = env.fromCollection(city);

        source1.cross(source2).print();
    }

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        mapFunction(env);
//        filterFunction(env);
//        mapPartitionFunction(env);
//        firstnFunction(env);
//        flatMapFunction(env);
//        distinctFunction(env);
//        joinFunction(env);
//        outJoinFunction(env);
        crossFunction(env);
    }

}
