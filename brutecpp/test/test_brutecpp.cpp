#include "../src/brutecpp.hpp"
#include <gtest/gtest.h>

int threads = 4;

TEST(NonPrimeTest, FindNonPrimeWorks) {
  long long target = 1337;
  std::pair<long long, long long> pq;

  pq = factorize(target, threads);

  EXPECT_NE(pq.first, 1);
  EXPECT_NE(pq.second, target);
}

TEST(PrimeTest, FindPrimeWorks) {
  long long target = 7561;
  std::pair<long long, long long> pq;

  pq = factorize(target, threads);

  EXPECT_EQ(pq.first, 1);
  EXPECT_EQ(pq.second, target);
}
