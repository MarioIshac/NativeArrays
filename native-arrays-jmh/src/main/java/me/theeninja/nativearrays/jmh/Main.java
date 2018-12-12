package me.theeninja.nativearrays.jmh;

import me.theeninja.nativearrays.core.IntArray;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(final String[] args) throws RunnerException {
        IntArray intArray = IntArray.fromJavaArray(3, 6, 7);

        System.out.println("Printing array");
        intArray.forEachIndexValuePair((index, value) -> {
            System.out.println(index);
            System.out.println(value);
        });

        System.out.println("Printing array directly");
        System.out.println(intArray);

        /* final Options opt = new OptionsBuilder()
            .include(IntArrayBenchmark.class.getSimpleName())
            .forks(1)
            .build();

        final Runner benchmarkRunner = new Runner(opt);
        benchmarkRunner.run(); */
    }
}
