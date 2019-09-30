import math
import numpy as np


'''
https://medium.com/analytics-vidhya/jenks-natural-breaks-best-range-finder-algorithm-8d1907192051
'''


def find_deviation_raised(points):
    mean = np.mean(points)
    raised = np.power(points - mean, 2)
    return np.sum(raised)


def jnb_algorithm_r2(data_points):
    data = [p[1] for p in data_points]

    if len(data) == 0 or None in data:
        return None, None, None

    data = sorted(data)
    sdam = find_deviation_raised(data)

    best_mid_point = None
    best_sdcm = 0
    for range_mid_point in range(1, len(data)):
        range_1 = data[:range_mid_point]
        range_2 = data[range_mid_point:]
        sdcm_all = find_deviation_raised(
            range_1) + find_deviation_raised(range_2)

        if best_sdcm < sdcm_all:
            best_sdcm = sdcm_all
            best_mid_point = range_mid_point

    gvf = (sdam - best_sdcm)/sdam
    return gvf, [best_1_data, best_2_data]


def jnb_algorithm_r4(data):
    if len(data) == 0 or None in data:
        return None, None, None

    data = sorted(data)
    sdam = find_deviation_raised(data)

    best_1_avg = None
    best_2_avg = None
    best_3_avg = None
    best_4_avg = None
    best_sdcm = 0

    for a in range(1, len(data)):
        for b in range(a + 1, len(data)):
            for c in range(b + 1, len(data)):
                range_1 = data[:a]
                range_2 = data[a:b]
                range_3 = data[b:c]
                range_4 = data[c:]

                avg_1 = np.mean(range_1)
                avg_2 = np.mean(range_2)
                avg_3 = np.mean(range_3)
                avg_4 = np.mean(range_4)

                sdcm_all = find_deviation_raised(range_1) +\
                    find_deviation_raised(range_2) +\
                    find_deviation_raised(range_3) +\
                    find_deviation_raised(range_4)

                if best_sdcm < sdcm_all:
                    best_sdcm = sdcm_all
                    best_1_avg = avg_1
                    best_2_avg = avg_2
                    best_3_avg = avg_3
                    best_4_avg = avg_4

    gvf = (sdam - best_sdcm)/sdam
    return gvf, [best_1_avg, best_2_avg, best_3_avg, best_4_avg]


if __name__ == "__main__":
    data = np.array([4, 5, 9, 10])
    v = jnb_algorithm_r2(data)
    print(v)

    data = np.array([4, 9, 10, 33, 21, 44, 3, 2, 5, 9, 10])
    v = jnb_algorithm_r4(data)
    print(v)
