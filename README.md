# RBCS 7312 — Event Programming (Weeks 1–10)

**Student:** Silas Mungai | **Unit:** Event Programming | **Institution:** Riara University

Learning implementations based on the weekly practical guides in the September–December 2026 course folder. These examples show event sources, listeners, the Swing Event Dispatch Thread, Observer pattern, JavaFX events, binding, background work, and a JavaFX–MySQL capstone.

## Project layout
- \`week01\`: event-driven systems table, reflections, calculators, handler repairs, prototype.
- \`week02\`: custom Observer weather station, legacy Observer, refactored publishing, PropertyChangeListener.
- \`week03\`: JFrame/component/layout exploration, EDT, student portal mockup.
- \`week04\`: event listeners, live search, pizza totals, counter, registration, drawing.
- \`week05\`: JList, JTable, JTree, menus, toolbar, text editor, file manager.
- \`javafx-exercises\`: Week 6–9 JavaFX projects (Maven, JavaFX 21, JDK 21).
- \`university-system\`: Week 10 capstone with SQL schema, JDBC, FXML and background tasks.

## Swing and console examples
JDK 17+; for a file with a main method:
\`\`\`sh
javac week01/ProceduralCalculator.java
java -cp week01 ProceduralCalculator
javac week04/CounterApp.java
java -cp week04 CounterApp
\`\`\`
Run GUI applications on a computer with a desktop environment. The JavaFX projects require JDK 21 and Maven; see their READMEs.

## Verification
Core Swing sources are intended to compile with the stock JDK. JavaFX and the MySQL capstone require external Maven dependencies. MySQL credentials are supplied **only** via environment variables; no passwords are committed. Review, run and adapt the activities to your own observations before academic submission.

**Implementation note:** Video observation, physical event-card simulation, and NetBeans GUI drag-and-drop actions cannot be performed solely by code; accompanying model answers and runnable equivalents are provided instead.
