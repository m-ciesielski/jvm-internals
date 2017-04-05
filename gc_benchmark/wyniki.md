# Analiza wyników

Obserwując wyniki benchmarków na wykresach, widać istotną zależność między ilością dostępnej pamięci a wydajnością algorytmów GC.

Środowisko testowe:
* procesor Intel i5 3210-M (4 procesory logiczne)
* 8GB pamięci RAM

Pojedynczy wątek, losowy rozmiar alokacji:

| Max rozmiar sterty  | Najlepszy czas |
| ------------- | ------------- |
| 128mb  | G1GC  |
| 512mb  | G1GC  |
| 1024mb  | G1GC  |

Cztery wątki, losowy rozmiar alokacji:

| Max rozmiar sterty  | Najlepszy czas |
| ------------- | ------------- |
| 128mb  | ParallelOldGC  |
| 512mb  | ConcMarkSweepGC  |
| 1024mb  | ParallelOldGC  |

Pojedynczy wątek, stały rozmiar alokacji:

| Max rozmiar sterty  | Najlepszy czas |
| ------------- | ------------- |
| 128mb  | G1GC  |
| 512mb  | ConcMarkSweepGC  |
| 1024mb  | ConcMarkSweepGC  |
  
Cztery wątki, stały rozmiar alokacji:

| Max rozmiar sterty  | Najlepszy czas |
| ------------- | ------------- |
| 128mb  | ConcMarkSweepGC  |
| 512mb  | G1GC  |
| 1024mb  | G1GC  |
