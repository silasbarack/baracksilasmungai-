# Week 1 — Activity 1: Event sources and handlers

These are representative examples, not claims of observing particular videos.

| System | Event source | Event | Listener | Handler/action |
|---|---|---|---|---|
| ATM | Card slot | Card inserted | Card reader service | Read card data |
| Microwave | Start keypad | Start pressed | Control board | Start timed heating |
| Traffic lights | Pedestrian button | Crossing requested | Signal controller | Queue pedestrian phase |
| Video game | Keyboard | Left arrow pressed | Input manager | Move player |
| Elevator | Floor button | Floor requested | Lift controller | Queue destination |
| Smartphone | Touch screen | Tap | Touch dispatcher | Open selected app |
| Online shop | Checkout button | Click | Checkout controller | Validate cart |
| Security alarm | Motion sensor | Movement detected | Alarm system | Trigger alert |

**Self-check:** Source = component producing the event; listener = code registered to receive it. One source can notify multiple listeners (a Save button can trigger validation and analytics). One listener can handle several sources (a calculator action handler can handle several digit buttons).

# Activity 2 — Model event queue simulation
Queue: Button A, Button B, Text Updated, Timer, Resize, Checkbox, Menu, Key A, Mouse Move, Button A. Process FIFO, dispatching each to the appropriate handler. A blocking handler prevents subsequent events from being processed until it finishes. A user sees an unresponsive application. Move slow operations off the UI thread and post completed UI updates on the UI thread.

**Reflection (model):** The event queue holds incoming actions until they can be handled. The event loop repeatedly removes the next event and dispatches it. Listeners determine the action to perform. Events are generally processed in queue order on the UI thread. A slow handler blocks the queue. Users experience a frozen or delayed interface when that happens. Background workers keep the interface responsive.

# Activity 3 — Procedural vs event-driven
| Aspect | Procedural | Event-driven |
|---|---|---|
| Flow | Program directs fixed input sequence | User interactions drive callbacks |
| Recovery | Often restart or repeat prompt | Clear/retry without restart |
| Order | Predetermined | Flexible |
| Paths | Comparatively few | Many user interaction sequences |
| Organization | Linear functions | Registered handlers and state |
| User experience | Text prompts | Interactive GUI |

**Reflection (model):** The procedural calculator requests the operands and operator in a fixed order. The GUI calculator responds when a user clicks a button. The procedural example must restart for another calculation, while the GUI remains open. In the GUI, listeners are attached to buttons. Event-driven programming is effective for interactive interfaces because it responds directly to user input. It also separates event capture from calculations.

# Activity 4 — Repairs
- Bug 1: use SwingWorker for slow operations.
- Bug 2: register ActionListener.
- Bug 3: update the clock via javax.swing.Timer on the EDT.
- Bug 4: use window-scoped listeners and stop timers on disposal rather than retaining disposed windows.

# Activities 5–6 — Prototype and self-assessment
Prototype: library checkout UI. Source = Reserve button; event = click; listener = ActionListener; handler = validate the selected book and update status. The user controls the flow.
Self-assessment guide: identify event/source/listener/handler in each runnable app; explain FIFO, EDT, and handling; compare procedural and interactive calculators. Add personal learning notes after running the applications.
