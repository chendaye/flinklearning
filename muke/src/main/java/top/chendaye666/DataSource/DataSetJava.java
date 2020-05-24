package top.chendaye666.DataSource;

import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DataSetJava {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        fromCollection(env);
//        fromTextFile(env);
    }

    /**
     * 从集合创建 dataSet
     * @param env
     * @throws Exception
     */
    private static void fromCollection(ExecutionEnvironment env) throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<10; i++)
            list.add(i);
        env.fromCollection(list).print();
    }

    private static void fromTextFile(ExecutionEnvironment env) throws Exception {
        String path = "muke/data/wc.txt";

        env.readTextFile(path).print();
    }
}
