package mn.edu.must.sqat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScholarshipCalculatorTest {

    private ScholarshipCalculator calc;

    @BeforeEach
    void setUp() { calc = new ScholarshipCalculator(); }

    // ===== Хэсэг A: happy path =====
    @Test void letterGradeA_95()  { assertEquals("A", calc.letterGrade(95)); }
    @Test void letterGradeB_85()  { assertEquals("B", calc.letterGrade(85)); }
    @Test void gpaTwoScores_95_85() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), 0.0001);
    }
    @Test void scholarship100_highGpaEnoughCredits() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
    }
    @Test void scholarship70_goodGpa13Credits() {
        assertEquals(70, calc.scholarship(3.5, 13, false));
    }
}
