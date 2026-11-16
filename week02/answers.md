# Week 2 — Observer pattern answers and comparison

**Activity 1:** WeatherStationDemo demonstrates Subject registration/removal, notifications, and three independent displays.

**Activity 2:** LegacyObserverDemo uses deprecated Observable/Observer for comparison only. Their class-based inheritance and weakly typed update payloads reduce flexibility, so favor explicit interfaces or PropertyChangeSupport.

**Activity 3:** RefactoredStoreDemo decouples stock updates from dashboard and reorder alert implementations, allowing independent subscribers.

**Activity 4:** PropertyChangeDemo emits old and new values.

| Approach | Advantage | Tradeoff |
|---|---|---|
| Custom Observer | Strongly typed, no framework needed | More boilerplate |
| Legacy Observable | Short example | Deprecated, restricted inheritance |
| PropertyChangeSupport | Old/new values and named properties | JavaBeans conventions |
| Swing ActionListener | Standard widget event API | GUI-specific |

**Activity 5 self-assessment:** Can define Subject/Observer; add/remove observers; explain push versus pull model, coupling and multicast notifications; show the event property name and old/new values. Reflect on your own execution outcomes before submission.
