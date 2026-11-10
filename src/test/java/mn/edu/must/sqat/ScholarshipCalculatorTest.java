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
// ===== Хэсэг B: 100% branch =====    
    // letterGrade    
    @Test void letterGradeNegativeThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));    
    }    
    @Test void letterGradeOver100Throws() {        
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));    
    }    
    @Test void letterGradeC_75() { assertEquals("C", calc.letterGrade(75)); }    
    @Test void letterGradeD_65() { assertEquals("D", calc.letterGrade(65)); }    
    @Test void letterGradeF_50() { assertEquals("F", calc.letterGrade(50)); }    
    
    // gpa    
    @Test void gpaNullThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));    
    }    
    @Test void gpaEmptyArrayThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));    
    }    
    @Test void gpaMixed_C_D_F() {          // switch: C, D, default(F)        
        assertEquals(1.0, calc.gpa(new double[]{75, 65, 50}), 0.0001);    
    }    
    
    // scholarship    
    @Test void scholarshipGpaNegativeThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.1, 15, false));    
    }    
    @Test void scholarshipGpaAboveFourThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 15, false));    
    }    
    @Test void scholarshipCreditsNegativeThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.5, -1, false));    
    }    
    @Test void scholarshipViolationIsZero() {        
        assertEquals(0, calc.scholarship(3.9, 16, true));    
    }    
    @Test void scholarship50_highGpaLowCredits() {   // 3.9, 10 → голч хангасан ч кредит дутуу        
        assertEquals(50, calc.scholarship(3.9, 10, false));    
    }    
    @Test void scholarship50_midGpa() {              // 3.2 → 3.4-өөс бага        
        assertEquals(50, calc.scholarship(3.2, 16, false));    
    }    
    @Test void scholarshipZero_lowGpa() {        
        assertEquals(0, calc.scholarship(2.5, 12, false));    
    }
// ===== Хэсэг C: давталтын зааг (0 / 1 / 2+) =====
    // 0 удаа давтагдах (хоосон эсвэл null утга шалгах)
    @Test void gpaNullThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));    
    }    
    @Test void gpaEmptyArrayThrows() {        
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));    
    }
    
    // 1 удаа давтагдах
    @Test void gpaSingleScore_95() {        
        assertEquals(4.0, calc.gpa(new double[]{95}), 0.0001);    
    }
    
    // 2 болон түүнээс дээш удаа давтагдах (2+)
    @Test void gpaManyScores_95_85_75_65() {        
        assertEquals(2.5, calc.gpa(new double[]{95, 85, 75, 65}), 0.0001);    
    }
}
