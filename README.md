# RSS_Reader_Android

## Ngôn ngữ: Java Andoid

## Environment: 
- minSdk: 30
- targetSdk: 31
- emulator: PIXEL 4 API 30
- gradle version 7.0.2
- JDK: 1.8
- Firebase Auth
- GG API

## Flow
### Đăng nhập, đăng kí
- User đăng kí tài khoản để đăng nhập vào app. Sau khi đăng kí tài khoản thì user sẽ vào được trong app. 
- User đã có tài khoản đăng kí có thể điền tài khoản và mật khẩu đã đăng kí và nhấn login.
- User có thể nhấn SignIn with Google để đăng nhập. Không cần phải đăng kí.

### Read RSS
- Sau khi đăng nhập User chọn trang báo ốn đọc. Phía dưới danh sách trang báo là danh sách xem sau. User phải vào các trang báo và ấn xem sau thì danh sách này mới hiện thị, ban đầu sẽ trống.
- User sau khi chọn trang báo sẽ vào màn hình đọc RSS. User muốn xem chi tiết thì nhấn vào nút View Details. User muốn xem sau thì nhấn nút see later. 
- User nhấn vào nút home để về lại màn hình danh sách xem sau và danh sách các trang báo.
- User có thể vuốt trái phải để chuyển các chủ đề RSS, hoặc có thể nhấn vào tab phía trên màn hình để chuyển.
