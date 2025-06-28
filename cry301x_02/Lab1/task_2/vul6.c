#include <stdio.h>

int main()
{
  int arr[8] = "";
  int i = 0, tmp;

  printf("----Please fill the array----\n");

  while(1) 
  {
    printf("Next elements: "); 
    scanf("%d", &tmp);
    
    if (tmp < 0) break;
    
    arr[i] = tmp;
    i++;
  }

  printf("----Display array----");
  int startIdx, endIdx;
  printf("Start index: "); scanf("%d", &startIdx);
  printf("End index: "); scanf("%d", &endIdx);
  
  if (startIdx > 0) 
  {
    for (i = startIdx; i < endIdx; i++) 
    {
      printf("%d", arr[i]);
    }
  }

  return 0;
}