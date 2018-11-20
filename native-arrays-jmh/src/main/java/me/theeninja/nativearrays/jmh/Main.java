package me.theeninja.nativearrays.jmh;

import me.theeninja.nativearrays.core.IntArray;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(final String[] args) throws RunnerException {
        final Options opt = new OptionsBuilder()
            .include(IntArrayBenchmark.class.getSimpleName())
            .forks(1)
            .build();

        final Runner benchmarkRunner = new Runner(opt);
        benchmarkRunner.run();

        /* System.out.println("Starting Int Array creation");
        IntArray intArray = IntArray.fromJavaArray(1, 1, 2, 3);
        System.out.println("Starting Int Array count");
        System.out.println(intArray.count(1)); */
    }
}
