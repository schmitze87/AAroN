package aaron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemorySizeCalculatorTest {

    @Test
    void testCalculation() {
        System.out.println(MemorySizeCalculator.calculateMemory(80437, 577635, 10832796, 13.349474503166126, 1.5517858696868356));
        System.out.println(MemorySizeCalculator.calculateMemory(46926, 85027, 1713131, 16.284486708838962, 1.5384435067979372));
        System.out.println(MemorySizeCalculator.calculateMemory(804370, 5776350, 108327960, 13.349474503166126, 1.5517858696868356));
    }

}