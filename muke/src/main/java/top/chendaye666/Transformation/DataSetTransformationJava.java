package top.chendaye666.Transformation;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class DataSetTransformationJava {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        mapFunction(env);
//        filterFunction(env);
        mapPartitionFunction(env);
    }

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
}
