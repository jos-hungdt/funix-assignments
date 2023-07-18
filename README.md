# funix-assignments
## Lab 4
HƯỚNG DẪN SETUP CÁC BÀI ASSIGNMENT LAB 4
1. Database
  Database được sử dụng là MySQL, vì thế nếu bạn chưa cài đặt MySQL thì cần phải cài đặt MySQL trước. Vào [đây](https://www.dataquest.io/blog/install-mysql-windows/) để tham khảo cách cài MySQL trên Windows.
   Trong folder của từng bài assignment sẽ có một file .sql, chứa script để tạo database cũng như insert data vào cho các bảng. Mở file .sql trong MySQL Workbench lên và execute toàn bộ file.
2. IDE mình dùng là Eclipse. Mở project trong Eclipse IDE và sửa lại connection string của database thành local của bạn. Cách làm như sau:
- Các file .jsp được đặt trong thư mục WebContent. Vì là dùng JSP nên trong mỗi file JSP lại phải khai báo kết nối database 1 lần. Vì thế, có bao nhiêu file JSP thì cần sửa lại chừng đấy connection string.
- Trong file JSP, tìm tag có nội dung như dưới đây
> <sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver" url="jdbc:mysql://localhost/lab4asm11" user="root" password="123456"/>
- Sửa lại user và password của môi trường local của bạn.
3. Sau khi sửa hết các connection string thì có thể khởi chạy project lên rồi.

Chúc bạn thành công ^^!
