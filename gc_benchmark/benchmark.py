import subprocess
import os
import pprint
from collections import defaultdict

from matplotlib import pyplot as plt

MAX_HEAP_SIZES = [128, 512, 1024]  # in mb
GC_ALGORITHMS = ['ParallelOldGC', 'ConcMarkSweepGC', 'G1GC']


def run_single_thread_benchmark(measures_count=100, max_heap_size=512, gc_algorithm='G1GC',
                                allocation_count=100000, threads=1):
    out = subprocess.check_output(['mvn', '-q', 'exec:java',
                                   '-DjvmArgs="-Xmx{}m -XX:+Use{}"'.format(max_heap_size, gc_algorithm),
                                   '-Dexec.mainClass=AllocationBenchmark',
                                   '-Dexec.args={} {} {}'.format(measures_count, allocation_count, threads)],
                                  shell=True, cwd=r'C:\Users\Mateusz\IdeaProjects\jvm-internals\gc_benchmark')
    results = out.decode().replace('\r', '').split('\n')
    results_dict = {}
    for result in filter(None, results):
        key, value = result.split(' ')
        results_dict[key] = value
    return results_dict


def run_benchmark_suite(threads=4):
    results = defaultdict(dict)
    for gc_algorithm in GC_ALGORITHMS:
        for heap_size in MAX_HEAP_SIZES:
            results[gc_algorithm][heap_size] = run_single_thread_benchmark(gc_algorithm=gc_algorithm,
                                                                           max_heap_size=heap_size,
                                                                           threads=threads)
    return results


def plot_benchmark_results(benchmark_results: dict):
    x_axis = MAX_HEAP_SIZES
    averages = defaultdict(list)
    minimums = defaultdict(list)
    maximums = defaultdict(list)

    for gc_algorithm in GC_ALGORITHMS:
        for heap_size in MAX_HEAP_SIZES:
            averages[gc_algorithm].append(benchmark_results[gc_algorithm][heap_size]['Avg:'])
            minimums[gc_algorithm].append(benchmark_results[gc_algorithm][heap_size]['Min:'])
            maximums[gc_algorithm].append(benchmark_results[gc_algorithm][heap_size]['Max:'])

    plt.figure(1)
    for k in averages:
        plt.plot(x_axis, averages[k], label='{} (avg)'.format(k))
    plt.legend()

    plt.figure(2)
    for k in maximums:
        plt.plot(x_axis, maximums[k], label='{} (max)'.format(k))
    plt.legend()

    plt.figure(3)
    for k in minimums:
        plt.plot(x_axis, minimums[k], label='{} (min)'.format(k))
    plt.legend()

    plt.legend()
    plt.show()


def main():
    benchmark_results = run_benchmark_suite()
    plot_benchmark_results(benchmark_results)

if __name__ == '__main__':
    os.environ['JAVA_HOME'] = r'C:\Program Files\Java\jdk1.8.0_121'
    main()
