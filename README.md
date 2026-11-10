Лаборатори №9: Код хамралт — JaCoCo
Оюутны нэр: З.ЖаргалмааОюутны код: B232270005Хичээл: F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест (2026)

Орчин
java -version ба mvn -version командын гаралт:

openjdk version "17.0.21.0.3" 2026-10-24
OpenJDK Runtime Environment (build 17.0.21.0.3+1-1-24.04.1-Ubuntu)
OpenJDK 64-Bit Server VM (build 17.0.21.0.3+1-1-24.04.1-Ubuntu, mixed mode, sharing)
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 17.0.21.0.3, vendor: Ubuntu, runtime: /usr/lib/jvm/java-17-openjdk-amd64
Default locale: en, platform encoding: UTF-8
OS name: "linux", version: "6.6.87.2-microsoft-standard-wsl2", arch: "amd64", family: "unix"

Репогийн бүтэц
pom.xml
src/main/java/mn/edu/must/sqat/ScholarshipCalculator.java
src/test/java/mn/edu/must/sqat/ScholarshipCalculatorTest.java
cfg.png
results/
  mvn-verify-A.txt   jacoco-A.csv
  mvn-verify-B.txt   jacoco-B.csv
  mvn-test-D-fail.txt
  mvn-verify-final.txt   jacoco-final.csv

Ажиллуулах: mvn verify (тайлан: target/site/jacoco/index.html).

Хэсэг A: Happy path ба эхний хэмжилт
Зөвхөн хэвийн урсгалын 5 тест бичсэн (letterGradeA_95, letterGradeB_85, gpaTwoScores_95_85, scholarship100_highGpaEnoughCredits, scholarship70_goodGpa13Credits). results/mvn-verify-A.txt дахь Tests run: 5, Failures: 0.
results/jacoco-A.csv-ээс гарсан хамралт:
- Branch: 20 / 41 (48.8%)
- Line: 15 / 25 (60.0%)

Ажиглалт. Шар ромбо бүхий мөрүүд нь нөхцөлийн зөвхөн нэг тал нь (голдуу "хэвийн" тал нь) хамрагдсан салаанууд юм. Жишээ нь letterGrade-ийн score < 0 || score > 100 шалгалтад зөвхөн false тал, scholarship-ийн gpa >= 3.8 && credits >= 15, gpa >= 3.4 && credits >= 12 нөхцөлүүдэд зөвхөн true тал хамрагдсан. Улаан (огт хүрээгүй) мөрүүд нь голдуу алдааны замууд, өөрөөр хэлбэл IllegalArgumentException шидэх мөрүүд (letterGrade, gpa, scholarship гурван функцийн), мөн letterGrade-ийн C/D/F буцаах мөр, gpa-ийн switch-ийн C/D/default тал, scholarship-ийн 50 ба 0 буцаах мөрүүд байлаа. Happy path тест нь эдгээр "аль нэг нь буруу болсон" замуудыг огт дайрдаггүй нь харагдсан.

Хэсэг B: CFG ба Branch 100%
scholarship() функцийн CFG-г гараар зурсан: CFG
Нөхцөл бүрийг тусдаа зангилаа болгосон (N1 gpa < 0, N2 gpa > 4, N3 credits < 0, N4 throw, N5 hasViolation, N6 return 0, N7 gpa >= 3.8, N8 credits >= 15, N9 return 100, N10 gpa >= 3.4, N11 credits >= 12, N12 return 70, N13 gpa >= 3.0, N14 return 50, N15 return 0). Ирмэг бүрийг дайрах тестүүд:
- N1 –T→ N4 : scholarshipGpaNegativeThrows
- N1 –F→ N2, N2 –T→ N4 : scholarshipGpaAboveFourThrows
- N2 –F→ N3, N3 –T→ N4 : scholarshipCreditsNegativeThrows
- N3 –F→ N5 : scholarship100_highGpaEnoughCredits
- N5 –T→ N6 : scholarshipViolationIsZero
- N7 –T→ N8, N8 –T→ N9 : scholarship100_highGpaEnoughCredits
- N7 –F→ N10, N10 –T→ N11, N11 –T→ N12 : scholarship70_goodGpa13Credits
- N8 –F→ N10 : scholarship50_highGpaLowCredits
- N11 –T→ N13, N13 –T→ N14 : scholarship50_highGpaLowCredits
- N10 –F→ N13 : scholarship50_midGpa
- N13 –F→ N15 : scholarshipZero_lowGpa

Хамралтыг 100% хүргэхэд нийт 20 тест шаардагдсан (results/mvn-verify-B.txt: Tests run: 20, Failures: 0). results/jacoco-B.csv-д BRANCH_MISSED = 0, LINE_MISSED = 0 (Branch 41/41, Line 25/25). gpa()-ийн switch-ийн default салааг (F дүн) gpaMixed_C_D_F тест, || нөхцөл бүрийн үнэн утгыг тусдаа assertThrows тестүүд хамруулсан.

Хэсэг C: Давталтын заагийн тестүүд
gpa() дахь давталтад 0 / 1 / 2+ дүрмийг хэрэгжүүлсэн:
- 0 удаа (gpaEmptyArrayThrows, gpaNullThrows): хоосон массивт эсвэл null-д assertThrows(IllegalArgumentException)
- 1 удаа (gpaSingleScore_95): gpa([95]) == 4.0
- 2+ удаа (gpaManyScores_95_85_75_65): gpa([95, 85, 75, 65]) == 2.5

Эдгээр нь results/mvn-verify-B.txt дахь 20 тестэд нэмэгдэж нийт 22 тест болсон. Хамралтын тоо өөрчлөгдөөгүй (аль хэдийн 100%).
Яагаад хэрэгтэй вэ. Хамралт нь мөр, салаа ажилласан эсэхийг л хэмждэг тул давталт хэдэн удаа гүйснийг ялгадаггүй. Харин 0, 1, олон элементтэй үед хоосон оролт, голчийн хуваагч, нэг элементийн хариу зэрэг өөр өөр зан төлөв гардаг тул эдгээр тест хамралт өсгөхгүй ч алдаа барих чадварыг нэмдэг.

Хэсэг D: Хамралт 100%, гэхдээ алдаа амьд
Спецификаци: 60–69.99 оноо "D". Зааг утгын letterGradeBoundaries параметржсэн тестийг (0, 59.99, 60, 69.99, 70, 89.99, 90, 100 гэсэн 8 утга) нэмээд засваргүй кодоор mvn test ажиллуулахад score 60 -> D тохиолдол унав. results/mvn-test-D-fail.txt дахь алдааны мөр:
expected: <D> but was: <F>

Алдаа. letterGrade-д if (score > 60) return "D"; гэж бичсэн тул яг 60 оноо "F" болж байсан. Спецификаци 60-ыг D гэж заасан тул >= байх ёстой.
Засвар. Тусдаа commit-оор: Fix letterGrade: > 60 -> >= 60, spec says 60 is D. Засварын дараа бүх тест ногоон болсон (results/mvn-verify-final.txt: Tests run: 30, Failures: 0) ба results/jacoco-final.csv-д BRANCH_MISSED = 0, LINE_MISSED = 0.

Гол дүгнэлт. Branch хамралт 100% байхад энэ алдаа үл үзэгдэх байсан, учир нь score > 60 ба score >= 60 хоёр нь ижил хоёр салааг (true, false) үүсгэдэг. 65 оноо true салааг, 50 оноо false салааг хамруулахад хоёр хувилбар хоёулаа 100% хамралт өгдөг. Тэдгээрийн ялгаа нь зөвхөн яг 60 гэсэн ганц утгад л гардаг тул үүнийг зөвхөн зааг утгын тест илрүүлнэ. Хамралт нь тестийн кодын аль мөр, салааг гүйлгэснийг хэмждэг боловч гарсан үр дүн спецификацитай таарч байгаа эсэхийг хэмждэггүй. Мөн хэрэгжүүлээгүй шаардлага, буруу харьцуулах оператор зэргийг мэдэхгүй. Иймээс хамралт нь тестийн дутагдлыг харуулах хэрэгсэл болохоос кодыг зөв гэдгийн нотолгоо биш юм.

Үр дүнгийн хураангуй
- Үе шат A | Tests run: 5 | Branch: 20/41 (48.8%) | Line: 15/25 (60.0%) | jacoco-A.csv
- Үе шат B | Tests run: 20 | Branch: 41/41 (100%) | Line: 25/25 (100%) | jacoco-B.csv
- Үе шат C | Tests run: 22 | Branch: 41/41 (100%) | Line: 25/25 (100%) | —
- Үе шат D (засвар) | Tests run: 30 | Branch: 41/41 (100%) | Line: 25/25 (100%) | jacoco-final.csv
