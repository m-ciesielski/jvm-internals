# Analiza wyników

Obserwując wyniki benchmarków na wykresach, widać istotną zależność między ilością dostępnej pamięci a wydajnością algorytmów GC.

Wyniki w zależności od największej średniej wyników w testach z trzema rozmiarami stosu (128mb, 512mb, 1024mb):
Pojedynczy wątek, losowy rozmiar alokacji:
	1. G1GC
	2. OldParallelGC
	3. ConcMarkSweepGC

Cztery wątki, losowy rozmiar alokacji:
	1. OldParallelGC
	2. ConcMarkSweepGC
	3. G1GC

Pojedynczy wątek, stały rozmiar alokacji:
	1. ConcMarkSweepGC
	2. G1GC
	3. old
  
Cztery wątki, stały rozmiar alokacji:
	1. OldParallelGC
	2. ConcMarkSweepGC
	3. G1GC
