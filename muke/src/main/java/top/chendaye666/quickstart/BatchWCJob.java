package top.chendaye666.quickstart;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWCJob {
    public static void main(String[] args) throws Exception {
        System.out.println(ClassLoader.getSystemResource("log4j.properties"));
        System.out.println(ClassLoader.getSystemResource("log4j.xml"));
        String path = "muke/data/wc.txt";

        //todo:step1 获取运行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        //todo:step2 读取数据
        DataSource<String> text = env.readTextFile(path);

        //todo: step3 ETL
        text.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] split = s.split(" ");
                for (String str: split){
                    if (str.length() > 0)
                        collector.collect(new Tuple2<String, Integer>(str, 1));
                }
            }
        }).groupBy(0).sum(1).print();
    }
}
