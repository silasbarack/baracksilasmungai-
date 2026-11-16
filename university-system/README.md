# Week 10 — University Management System (EDA Capstone)

Based on the Week 10 final project guide. Implements MySQL schema, JDBC database utility, JavaFX observable student model, background Task for reads and writes, FXML views, a controller, and a 5-second ScheduledExecutorService heartbeat posted to the JavaFX application thread.

## Requirements
- JDK 21, Maven 3.9+, MySQL 8+, GUI desktop.
- Run `schema.sql` in MySQL Workbench to initialize `university_db`.
- Set environment variables **outside Git**:
  - `UNIVERSITY_DB_URL=jdbc:mysql://localhost:3306/university_db`
  - `UNIVERSITY_DB_USER=root` (or preferably a limited-access MySQL user)
  - `UNIVERSITY_DB_PASSWORD=<your-local-password>`

```bash
cd university-system
mvn clean compile
mvn javafx:run
```

## Event architecture
The FXML button generates an event; StudentController validates and starts a background Task; DatabaseUtil performs prepared SQL operations; the task's completion handler refreshes the observable student list on the JavaFX Application Thread. The heartbeat runs every 5 seconds and updates the bottom status label via Platform.runLater.

## Test checklist (run locally)
- Launch with MySQL available; confirm list loads.
- Save a valid student and verify they appear in the table.
- Attempt a duplicate registration; confirm error feedback and no insertion.
- Stop MySQL; confirm a readable failure status, then reconnect and refresh.
- Confirm UI remains responsive during a slow database operation.
- Verify heartbeat updates every 5 seconds and stops on exit.

**Caveat:** No live MySQL instance or graphical display was supplied for end-to-end verification. Example email domains are placeholders, not actual university accounts. Do not commit passwords or real student information.
