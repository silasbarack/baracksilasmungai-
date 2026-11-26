# Weeks 6–9: Self-check answers and expected observations

Answers follow the lecturer's self-paced JavaFX guides. Observations below are predictions to verify by running the corresponding examples; they are not a record of a GUI session.

## Week 6 — Introduction to JavaFX
1. A Stage is the top-level window; a Scene holds the scene graph shown inside it. The JavaFX Application Thread handles UI events and changes to the displayed scene graph.
2. VBox arranges nodes vertically; HBox horizontally; GridPane in rows and columns; BorderPane in five regions. Layout panes respond to resizing without manually positioning every control.
3. FXML describes the scene graph declaratively. Scene Builder can edit it visually; FXMLLoader constructs the nodes from it.
4. @FXML permits the loader to inject named controls and invoke annotated controller methods. Separating markup from Java keeps layout and event-handling logic easier to maintain.
5. The dashboard combines navigation, summary content and reusable layout containers. Expected: launching DashboardDemo displays those sections; resizing preserves the layout.

## Week 7 — Event handling
1. Filters run during capturing (root toward target); handlers run during bubbling (target toward root). consume() prevents further propagation. Register a bubbling handler with addEventHandler(eventType, handler), or a suitable setOn... method.
2. setOnMouseMoved handles mouse motion without pressing a button. Key events may not reach a node that lacks keyboard focus; request focus when appropriate. getCode() returns the key code.
3. Start the operation in setOnDragDetected by calling startDragAndDrop. acceptTransferModes(MOVE) tells the drag system the target accepts a move. On drop, read event.getDragboard().getString() after checking hasString(), and call setDropCompleted accordingly.
4. A custom event lets interested components react without the producer knowing their implementation. Extend javafx.event.Event; declare a static EventType with a suitable parent and fire it using fireEvent.
Expected: the filter/handler demo shows propagation order; mouse/key labels change with input; drag-and-drop transfers data to the target; custom events notify registered handlers.

## Week 8 — Properties and bindings
1. StringProperty supports observation and binding, unlike a plain String. nameProperty() exposes the property object for listeners and bindings.
2. A ChangeListener receives old and new values; an InvalidationListener reports that the observable is invalid and may need recomputation. Setting an unchanged simple property normally does not fire another notification.
3. Unidirectional binding updates the target when the source changes; directly setting a bound target throws a runtime exception until it is unbound.
4. bind() connects one direction; bindBidirectional() synchronizes both directions. NumberStringConverter bridges numeric values and text representations.
5. Binding disableProperty() keeps validity synchronized with every input change instead of checking only after submission. not() negates the BooleanBinding, so invalid email input can disable submission.
Expected: editing the source updates a bound label; a bidirectional control pair stays synchronized; registration is disabled until the validation conditions pass.

## Week 9 — Asynchronous programming
1. Sleeping on the JavaFX Application Thread blocks processing of input and painting, causing the UI to freeze.
2. Platform.runLater schedules work on the JavaFX Application Thread. Task.setOnSucceeded already runs on that thread, so runLater is redundant inside that callback; it is needed for UI changes originating directly from an arbitrary worker.
3. updateProgress() publishes thread-safe Task progress for bound controls; runLater schedules an arbitrary UI action. Unbind status text before assigning an independent final message or it remains bound to the task.
4. A Task is single-use; a Service creates a new Task for each execution and supports restart. Check isCancelled() inside work loops and return promptly when cancelled.
Expected: the blocked UI example freezes intentionally; the worker example remains responsive; progress advances as work completes; cancelling stops work and a Service can start again.

## Practical verification
Use the class names and Maven commands in README.md. For each demo, compare its actual behavior with the expected observation above and record any differences. Do not report a successful run until it has been performed.
