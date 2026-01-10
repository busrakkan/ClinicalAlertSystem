# ClinicalAlertSystem

**ClinicalAlertSystem** is a concurrent Java service that monitors hospital room temperatures and generates real-time alerts when thresholds are exceeded.  
The system is designed to be **scalable, fault-tolerant, and suitable for safety-critical environments**.

---

## Features

- Concurrent monitoring of multiple hospital rooms  
- Real-time temperature threshold evaluation  
- Prioritized alerts (High / Medium / Low)  
- Thread-safe alert queue and asynchronous notification  

---

## Architecture

**Workflow:**

`[TemperatureSensor]` → `[SensorTask (concurrent)]` → `[ThresholdEvaluator]` → `[PriorityBlockingQueue<Alert>]` → `[NotificationDispatcher]`

**Key points:**

- Each sensor runs independently  
- High-priority alerts are processed first  
- Sensor faults do not block the system  

---

## Concurrency and Reliability Design

ClinicalAlertSystem is designed as a concurrent producer–consumer system.

### Thread Model
- Each hospital room is monitored by an independent sensor task.
- Sensor tasks run in a fixed-size ExecutorService to control resource usage.
- A dedicated dispatcher thread processes alerts asynchronously.

### Alert Processing
- Sensors produce alerts based on temperature thresholds.
- Alerts are published to a thread-safe PriorityBlockingQueue.
- Alert severity determines processing priority (CRITICAL > HIGH > MEDIUM > LOW).

### Fault Isolation
- Each sensor iteration is protected by exception handling.
- A failure in one sensor does not affect other sensors or the dispatcher.
- Threads terminate gracefully when interrupted.

### Backpressure Handling
- Alert queue capacity is bounded to prevent unbounded memory growth.
- Sensors use non-blocking queue operations to avoid stalling.
- When the queue is full, alerts may be dropped to preserve system stability.

### Shutdown Behavior
- The system registers a shutdown hook.
- All executor services are terminated gracefully on shutdown.

---
## Getting Started

1. Clone the repository:

git clone https://github.com/busrakkan/ClinicalAlertSystem.git

2. Build with Maven:

mvn clean install

3. Run the main class:

mvn exec:java 
