xử lý giao dịch phân tán bằng cách triển khai saga-ochoreography pattern
+ loan-service publish LoanApprovedEvent lên topic để management service xử lý logic kho hàng 
+ management-service publish InventoryCheckedEvent và Loan-Service consume lại và cập nhật trạng thái đơn mượn
LoanApprovedEvent dưới dạng json

![420720196_1118106919202581_1177905302822029545_n](https://github.com/havanduc20194510/management_assets_microservices/assets/99519345/5b410547-b329-47a6-8c44-c4da93f90ceb)

InventoryCheckedEvent dưới dạng ![420709025_915210426845397_4207890013678826667_n](https://github.com/havanduc20194510/management_assets_microservices/assets/99519345/c317d865-832a-4325-9472-0232ed088495)

nếu không có sản phẩm nào vượt quá số lượng trong kho list sản phẩm của InventoryCheckedEvent sẽ trả về null hoặc ds rỗng
ngược lại nếu có thì trả về danh sách sản phẩm đó 
