#include <stdio.h>

int main()
{
  const int ELEMENT_COUNT = 8;
  int arr[ELEMENT_COUNT];
  int i = 0, tmp;

  printf("----Please fill the array----\n");

  while (i < 8) 
  {
    printf("Next elements (enter negative to finish): "); 
    if (scanf("%d", &tmp) > 0)
    {
      arr[i] = tmp;
      i++;
    } else {
      printf("Invalid input. \n");
      while (getchar() != '\n'); // Clear input buffer
    }

    if (tmp < 0) break;
  }

  printf("----Display array---- \n");
  int startIdx, endIdx;
  printf("Start index: ");
  while (scanf("%d", &startIdx) <= 0 || startIdx >= ELEMENT_COUNT || startIdx < 0) {
    printf("Invalid start index.\n");
    printf("Re-endter start index: ");
  }

  printf("End index: ");
  while (scanf("%d", &endIdx) <= 0 || endIdx >= ELEMENT_COUNT || endIdx < 0) {
    printf("Invalid end index.\n");
    printf("Re-endter end index: ");
  }
  
  if (startIdx >= 0) 
  {
    for (i = startIdx; i <= endIdx; i++) 
    {
      printf("%d ", arr[i]);
    }
  }

  return 0;
}