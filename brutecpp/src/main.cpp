#include "brutecpp.hpp"

int main(int argc, char **argv) {
  int threads;
  long long target;

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

  p = 1;
  q = target;

  factorize(target, threads);

  if (p == 1 && q == target) {
    std::cout << "[PRIME] " << target << ": (" << p << ", " << q << ")"
              << std::endl;
  } else {
    std::cout << "[NOT PRIME] " << target << ": (" << p << ", " << q << ")"
              << std::endl;
  }

  return 0;
}
