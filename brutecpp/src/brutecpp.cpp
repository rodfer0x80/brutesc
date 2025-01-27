#include "brutecpp.hpp"

std::mutex m;
std::atomic<bool> f(false);
long long p;
long long q;

void factor(long long target, long long startFactor, long long endFactor) {
  for (long long i = startFactor; i <= endFactor && !f.load(); i++) {
    if (target % i == 0) {
      {
        std::lock_guard<std::mutex> guard(m);
        if (!f.load()) {
          p = i;
          q = target / i;
          f.store(true);
        }
      }
      break;
    }
  }
}

std::pair<long long, long long> factorize(long long target, int threads) {
  p = 1;
  q = target;

  long long sqrtTarget = std::sqrt(target);

  std::vector<std::thread> threadVec;

  for (int i = 0; i < threads; i++) {
    long long startFactor = 2 + (sqrtTarget / threads) * i;
    long long endFactor = 2 + (sqrtTarget / threads) * (i + 1);
    if (endFactor > sqrtTarget)
      endFactor = sqrtTarget;

    //std::cout << "Thread [" << i + 1 << "] : range " << startFactor << " to "
    //          << endFactor << std::endl;

    threadVec.push_back(std::thread(factor, target, startFactor, endFactor));
  }

  for (auto &t : threadVec) {
    t.join();
  }
  return {p, q};
}
