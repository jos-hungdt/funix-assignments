## Description
Cho chương trình viết bằng ngôn ngữ C giả định chức năng tìm kiếm thông tin của một hệ thống. Nếu người dùng chỉ là khách vãng lai (tài khoản guest) thì chỉ hiển thị dữ liệu thông thường. Ngược lại, hiển thị cả dữ liệu nội bộ. Thực hiện các kiểm thử cần thiết để chỉ ra lỗ hổng an toàn bảo mật của chương trình và
giải thích các nguy cơ.

## Syntax notes
- `#define` https://www.geeksforgeeks.org/c/c-define-preprocessor/ 
- `strcpy` - string copy
- `strcmp` - string compare, skip lower/upper -case

## Vulnerability
### Buffer Overflow
Dòng 13 trong chương trình có sử dụng hàm `gets()` để đọc dữ liệu nhập vào từ người dùng và lưu trữ vào biến `keyword`. Biến `keyword` được khai báo có kích thước là 32 ký tự nhưng với logic hiện tại, người dùng có thể nhập vào chuỗi có kích thước bất kỳ.

Từ tiêu chuẩn C11, hàm `gets` đã không còn được sử dụng nữa và đã bị xóa khỏi thư viện chuẩn do những rủi ro của nó, xem chi tiết tại [geeksforgeeks.org -  gets() in C](https://www.geeksforgeeks.org/c/gets-in-c/#:~:text=Lack%20of%20Buffer,or%20EOF%20effectively).

 Trong trường hợp người dùng nhập vào một chuỗi có kích thước lớn hơn nhiều như dưới đây.

```
What do you want to search? Keyword: 12312321333333333333333333333332132132132132132132131111111111111111111111111111
```

Khi đó, lỗi tràn bộ đệm sẽ xảy ra:
```
Exception has occurred.
Segmentation fault
```
**Kết luận**: Có lỗ hổng tràn bộ đệm (Buffer Overflow).

**Giải pháp**: Sử dụng hàm `fgets` để đọc và gán dữ liệu cho biến `keyword` một cách an toàn. 
Xem thêm về hàm `fgets()` tại [C Library - fgets() function](https://www.tutorialspoint.com/c_standard_library/c_function_fgets.htm).

**Kết quả**:
```
What do you want to search? Keyword: 12312321333333333333333333333332132132132132132132131111111111111111111111111111
Results: Open Information
```
Code sau chỉnh sửa, xem trong file `vul5_fixed.c`
