# JVM GC benchmark

Prerequisites:
```
Python 3.5
virtualenv (or conda)
```

```
virtualenv venv
. venv/bin/activate
pip install -r requirements.txt
python benchmark.py
```

Script will measure allocation time for three GC algorithms:
* ParallelOldGC
* G1GC
* ConcMarkSweepGC

On three different max heap sizes:
* 128m
* 512m
* 1024m

Script will produce three plots, with average, minimum and maximum
execution time for different scenarios as a result.

