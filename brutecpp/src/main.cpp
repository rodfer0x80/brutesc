#include "brutecpp.hpp"

int main(int argc, char **argv) {
  int threads;
  long long target;
  std::pair<long long, long long> pq;

  if (argc < 2 || argc > 3) {
    std::cerr << "Error: Invalid arguments" << std::endl;
    std::cout << "Usage: ./brutecpp <target :: long long [max = "
                 "9223372036854775807]> <threads :: int>"
              << std::endl;
    return 1;
  } else if (argc == 2) {
    threads = 4;
  } else {
    threads = std::atoi(argv[2]);
  }
  target = std::atoll(argv[1]);

  std::cout << "Threads: " << threads << std::endl;
  std::cout << "Target: " << target << std::endl;

  pq = factorize(target, threads);

  if (pq.first == 1 && pq.second == target) {
    std::cout << "[PRIME]: " << target << "->(" << pq.first << ", " << pq.second
              << ")" << std::endl;
  } else {
    std::cout << "[NOT PRIME]: " << target << "->(" << pq.first << ", "
              << pq.second << ")" << std::endl;
  }

  return 0;
}
