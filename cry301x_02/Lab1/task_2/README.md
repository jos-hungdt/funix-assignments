## Description
Cho chương trình viết bằng ngôn ngữ C để nhập và đọc ra các phần tử của một mảng nguyên dương như sau. Thực hiện các kiểm thử cần thiết để chỉ ra lỗ hổng an toàn bảo mật của chương trình và giải thích các nguy cơ.

## Vulnerability
### Sai syntax khởi tạo mảng
Trong chương trình được cung cấp, mảng arr được khai báo như sau `int arr[8] = "";`, đây là lỗi sai cú pháp C.

### Lổ hổng tràn bộ đệm và vòng lặp vô hạn
Xét đoạn code 
```C
int arr[8];

// other codes

while(1) 
{
  printf("Next elements: "); 
  scanf("%d", &tmp);
  
  if (tmp < 0) break;
  
  arr[i] = tmp;
  i++;
}
```
Bất kì giá trị nào được nhập vào đều được lưu trữ vào mảng `arr` mà không hề có sự kiểm tra nào trong khi mảng `arr` chỉ được khai báo với kích thước là 8 phần tử. Vòng lặp chỉ dừng lại khi số nhập vào là một số âm, trường hợp người dùng hoặc kẻ tấn công tạo chương trình để lặp lại việc nhập vào liên tục các số dương. Lỗi tràn bộ đệm sẽ xảy ra.

Trong đoạn code này:
```C
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
``` 
`startIdx` và `endIdx` không được kiểm tra, có thể gây ra lỗi out-of-bounds khi chương trình cố gắng truy cập vào một địa chỉ ô nhớ không hợp lệ. Ví dụ khi `endIdx = 10`, in `arr[10]` sẽ gây ra lỗi do kích thước tối đa của mảng `arr` chỉ là 8 phần tử.

### Lỗ hổng tràn số nguyên
Từ [C Data Type](https://www.tutorialspoint.com/cprogramming/c_data_types.htm), biến `int` có kích thước lưu trữ là 2 hoặc 4 byte hay dải giá trị trong khoảng từ -32,768 đến 32,767 hoặc -2,147,483,648 đến 2,147,483,647. 

Trong chương trình được cung cấp, giá trị nhập vào chưa được kiểm tra mà được gán trực tiếp vào các biến trong chương trình. 

Ngoàn ra các hàm `scanf()` không được kiểm chứng về giá trị nhập vào, người dùng hoàn toàn có thể nhập vào các giá trị không phải kiểu số, gây lỗi cho chương trình.

## Giải pháp 
- Kiểm tra và dừng việc đọc giá trị vào mảng khi đạt tối đa số lượng phần tử;
- Kiểm tra giá trị nhập vào của người dùng từ các hàm `scanf()`;
- Kiếm tra hai biến `startIdx` và `endIdx` để đảm bảo cả hai đều nằm trong giới hạn cho phép.

Code đã chỉnh sửa, xem file `vul6_fixed.c`.
