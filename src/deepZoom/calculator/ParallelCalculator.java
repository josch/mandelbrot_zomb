package deepZoom.calculator;

import java.util.Arrays;

/**
 * @author Zom-B
 * @since 1.0
 * @date Apr 19, 2009
 */
public class ParallelCalculator implements Runnable {

    private static int NUM_CPUS;
    private static boolean[] WAIT;
    private static boolean[] GO;

    public static void createCalculators(Calculator calculator, int numCPUs) {
        ParallelCalculator.NUM_CPUS = numCPUs;

        ParallelCalculator.WAIT = new boolean[numCPUs];
        ParallelCalculator.GO = new boolean[numCPUs];

        for (int i = 0; i < numCPUs; i++) {
            new ParallelCalculator(calculator, i);
        }
    }
    private int cpu;
    private Calculator calculator;

    private ParallelCalculator(Calculator calculator, int cpu) {
        this.calculator = calculator;
        this.cpu = cpu;

        System.out.println("Starting core " + cpu);

        prepare();
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            calculator.calculate(cpu);
            syncWithOthers();
        }
    }

    private void prepare() {
        if (cpu == 0) {
            Arrays.fill(ParallelCalculator.WAIT, true);
            Arrays.fill(ParallelCalculator.GO, true);

            calculator.prepareCalculation();
        }
    }

    private void syncWithOthers() {
        ParallelCalculator.WAIT[cpu] = false;

        if (cpu == 0) {
            while (true) {
                boolean done = true;
                for (int i = ParallelCalculator.NUM_CPUS - 1; i >= 0; i--) {
                    if (ParallelCalculator.WAIT[i]) {
                        done = false;
                        break;
                    }
                }
                if (done) {
                    break;
                }

                Thread.yield();
            }

            // All threads done.

            calculator.calculationCompleted();
            prepare();

            Arrays.fill(ParallelCalculator.WAIT, true);
            Arrays.fill(ParallelCalculator.GO, true);
        } else {
            while (!ParallelCalculator.GO[cpu]) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.yield();
                }
            }
        }

        ParallelCalculator.GO[cpu] = false;
    }
}