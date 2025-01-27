#ifndef BRUTECPP_HPP
#define BRUTECPP_HPP

#include <atomic>
#include <cmath>
#include <cstdlib>
#include <iostream>
#include <mutex>
#include <thread>
#include <vector>

extern std::mutex m;
extern std::atomic<bool> f;
extern long long p;
extern long long q;

void factor(long long target, long long startFactor, long long endFactor);
void factorize(long long target, int threads);

#endif // BRUTECPP_HPP
