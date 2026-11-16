# Weeks 6–9 JavaFX practical activities

Requires JDK 21 and Maven 3.9+ with a desktop environment.

From `javafx-exercises`:
```bash
mvn clean compile
mvn javafx:run -DmainClass=edu.riara.edp.week06.FirstWindow
```

Change the final class to run these activities:

- Week 6: `week06.FirstWindow`, `week06.LayoutsDemo`, `week06.FxmlDemo`, `week06.DashboardDemo`.
- Week 7: `week07.FiltersHandlersDemo`, `week07.MouseKeyDemo`, `week07.DragDropDemo`, `week07.CustomEventDemo`.
- Week 8: `week08.PropertyDemo`, `week08.ListenerDemo`, `week08.OneWayBindingDemo`, `week08.TwoWayBindingDemo`, `week08.RegistrationFormDemo`.
- Week 9: `week09.BlockedUiDemo`, `week09.BackgroundTaskDemo`, `week09.ProgressDemo`, `week09.ReusableServiceDemo`.

Prefix each name with `edu.riara.edp.`. The intentionally broken `BlockedUiDemo` illustrates why the UI thread must not be blocked. FXML can be edited in Scene Builder.

**Self-check:** Observe event filter vs handler order (W7); property change vs invalidation listener behavior (W8); cancellation, progress and service restarts (W9). Actual GUI testing and personal reflections remain for the student to complete.
