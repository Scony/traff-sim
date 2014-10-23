#include <iostream>
#include <stdlib.h>
#include <time.h>
using namespace std;

int main()
{
  int n = 50, k = 200;
  cout << n << endl;
  for(int i = 0; i < n; i++)
    {
      cout << i << " " << rand() % 700 << " " << rand() % 700 << endl;
    }
  cout << k << endl;
  for(int i = 0; i < k; i++)
    {
      int x = rand() % 50;
      int y = rand() % 50;
      while(x==y)
	y = rand() % 50;
      cout << i << " " << x << " " << y << " 3 0 0" <<  endl;
    }
  return 0;
}
