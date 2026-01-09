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

## Getting Started

1. Clone the repository:

git clone https://github.com/busrakkan/ClinicalAlertSystem.git

2. Build with Maven:

mvn clean install

3. Run the main class:

mvn exec:java -Dexec.mainClass="it.polito.clinicalalertsystem.Main"
