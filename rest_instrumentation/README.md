# Instrumentacja

Modyfikacja metod oznaczonych
adnotacją `@Measure` z użyciem javaassist.


Instrukcja użycia:
```
mvn package
mvn exec:exec
curl localhost:4567/hello
```

Po wykonaniu zapytania pod adres `localhost:4567/hello`,
w logach aplikacji pojawi się czas wykonania metody oraz lista parametrów z jakimi została wywołana metoda.
