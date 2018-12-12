package me.theeninja.nativearrays.jmh;

import me.theeninja.nativearrays.core.IntArray;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(final String[] args) throws RunnerException {
        IntArray intArray = IntArray.fromJavaArray(new int[] {
                1, 2, 3
        });

        /* final Options opt = new OptionsBuilder()
            .include(IntArrayBenchmark.class.getSimpleName())
            .forks(1)
            .build();

        final Runner benchmarkRunner = new Runner(opt);
        benchmarkRunner.run(); */
    }
}
