# Concurrent Date Format

Program demonstruje problemy mogące wystąpić w przypadku
 użycia klasy `SimpleDateFormat` jednocześnie przez wiele wątków.
 Żeby zaobserwować te błędy, prawdopodobnie będzie koniecznie
  kilkukrotne uruchomienie programu.
 
 Ponadto przedstawione jest rozwiązanie tego problemu, przez 
 opakowanie `SimpleDateFormat` jako `ThreadLocal`.
 
 
 Użycie:
 ```
mvn compile
mvn exec:java
```
 
