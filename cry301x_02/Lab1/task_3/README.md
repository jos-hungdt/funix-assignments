## Description
Cho chương trình Java như sau cung cấp chức năng lấy ra một mảng con từ mảng lớn. Thực hiện các kiểm thử cần thiết để xác định những lỗi truy cập bộ nhớ khiến chương trình gián đoạn ngoài ý muốn.

## Vulnerability
### Kích thước mảng `n` và dữ liệu nhập vào chưa được được validate
Các dữ liệu nhận vào từ màn nhập liệu chưa được validate có thể gây ra các lỗi trong trường hợp:
- Một số âm được nhập vào và gán cho `n` - kích thước mảng;
- Giá trị không phải số nguyên được truyền vào;

### Lỗ hổng truy cập vùng nhớ không hợp lệ
Xét đoạn code sau trong chương trình đã cho:
```Java
  System.out.print("Start index: ");
  int start = scanner.nextInt();
  
  System.out.print("End index: ");
  int end = scanner.nextInt();
  
  byte[] subData = subArray(input, start, end);
```
`start` và `end` là chỉ mục đầu và cuối cho mảng con được yêu cầu in ra. Dữ liệu nhập vào chưa được kiểm tra đầy đủ để đảm bảo các chỉ mục được nhập vào là hợp lệ. Điều này có thể dẫn tới việc chương trình cố gắng truy cập vào vùng nhớ không hợp lệ khi hai chỉ mục không thuộc trong khoảng từ 0 đến `n` - là khoảng chỉ mục hợp lệ.

### Resource leak
Một instance `Scanner` được khai báo và sử dụng trong chương trình nhưng chưa được close khi kết thúc chương trình.

## Giải pháp 
- Validate dữ liệu được nhập vào đảm bảo `n` là một số nguyên dương, các chỉ mục `start` và `end` nằm trong khoảng hợp lệ và các phần tử mảng được nhập vào là các số hợp lệ.
- Xóa các thư viện không sử dụng.
- Gọi hàm `close()` để giải phóng instance `Scanner`.

Code được chỉnh sửa xem ở file `vul7_fixed.java`.