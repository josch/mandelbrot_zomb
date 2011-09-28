package deepZoom.calculator;

/**
 * @author Zom-B
 * @since 1.0
 * @date Apr 19, 2009
 */
public interface Calculator {

    public void prepareCalculation();

    public void calculate(int cpu);

    public void calculationCompleted();
}
