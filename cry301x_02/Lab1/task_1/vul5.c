#include <stdio.h>
#include <string.h>
#define GUEST_ACCOUNT "guest"
#define ADMIN_ACCOUNT "admin"

int main()
{
  char account[32] = "";
  char keyword[32];

  strcpy(account, GUEST_ACCOUNT);
  printf("What do you want to search? Keyword: ");
  gets(keyword);
  
  if (strcmp(account, GUEST_ACCOUNT) == 0) 
  {
    printf("Results: Open Information");
  }
  else if (strcmp(account, ADMIN_ACCOUNT) == 0) 
  {
    printf("Results: Secret Information\n");
  }
  
  return 0;
}
