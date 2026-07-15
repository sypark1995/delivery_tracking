package com.parcelkr.app.i18n

import androidx.compose.runtime.staticCompositionLocalOf

enum class Lang(val code: String, val nativeName: String, val englishName: String) {
    EN("en", "English", "English"),
    KO("ko", "한국어", "Korean"),
    ZH("zh", "中文（简体）", "Chinese"),
    VI("vi", "Tiếng Việt", "Vietnamese"),
    TH("th", "ไทย", "Thai"),
    RU("ru", "Русский", "Russian"),
    JA("ja", "日本語", "Japanese");

    companion object {
        fun fromCode(code: String): Lang = entries.firstOrNull { it.code == code } ?: EN
    }
}

interface AppStrings {
    val appName: String
    val back: String
    val homeTitle: String
    val addTrackingBar: String
    val segActive: String
    val segDelivered: String
    val segAll: String
    val recent: String
    val callDriver: String
    val emptyTitle: String
    val emptySubtitle: String
    val addParcel: String
    val trackingNumber: String
    val paste: String
    val scan: String
    val manual: String
    val detectedCarrier: String
    val change: String
    val addAnotherWay: String
    val trackingDetail: String
    val showOriginal: String
    val onboardingTagline: String
    val chooseLanguage: String
    val cont: String
    val contactDriver: String
    val sendMessageAutoTranslated: String
    val you: String
    val sentInKorean: String
    val sendMessage: String
    val settings: String
    val customs: String
    val personalClearanceCode: String
    val enterCustomsCode: String
    val saveCustomsCode: String
    val copyCustomsCode: String
    val customsCodeCopied: String
    val preferences: String
    val language: String
    val notifications: String
    val theme: String
    val about: String
    val version: String
    val updates: String
    val earlier: String
    val expectedToday: String
    val delivered: String
    val pasteEmailHint: String
    val pasteEmailConfirm: String
    val pasteEmailFailed: String
    val deleteParcel: String
    val deleteParcelConfirmTitle: String
    val deleteParcelConfirmMessage: String
    val cancel: String
    val delete: String
    val trackingLookupFailed: String
    val retry: String
    val trackingDetailFailedTitle: String
    val trackingDetailFailedMessage: String
    val noDriverInfo: String
    val deliveryHistory: String
    val historyEmptyMessage: String
    val parcelsUnit: String
    val stalledBadge: String
    val searchHint: String
    val sortByRecent: String
    val sortByName: String
    val noSearchResults: String
    val statusChangeNotificationTitle: String
    val tagLabel: String
    val addTagHint: String
    val saveTag: String
}

object EnStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "Back"
    override val homeTitle = "Your parcels"
    override val addTrackingBar = "Add tracking number"
    override val segActive = "Active"
    override val segDelivered = "Delivered"
    override val segAll = "All"
    override val recent = "Recent"
    override val callDriver = "Call driver"
    override val emptyTitle = "Track your first parcel"
    override val emptySubtitle = "Paste a tracking number to start. We'll translate every update into your language."
    override val addParcel = "Add parcel"
    override val trackingNumber = "Tracking number"
    override val paste = "Paste"
    override val scan = "Scan"
    override val manual = "Manual"
    override val detectedCarrier = "Detected carrier"
    override val change = "Change"
    override val addAnotherWay = "Or add another way"
    override val trackingDetail = "Tracking detail"
    override val showOriginal = "Show original (Korean)"
    override val onboardingTagline = "Track parcels in Korea — in your own language."
    override val chooseLanguage = "Choose your language"
    override val cont = "Continue"
    override val contactDriver = "Contact driver"
    override val sendMessageAutoTranslated = "Send a message (auto-translated)"
    override val you = "You"
    override val sentInKorean = "Sent in Korean"
    override val sendMessage = "Send message"
    override val settings = "Settings"
    override val customs = "Customs"
    override val personalClearanceCode = "Personal clearance code"
    override val enterCustomsCode = "Enter your personal clearance code"
    override val saveCustomsCode = "Save"
    override val copyCustomsCode = "Copy"
    override val customsCodeCopied = "Copied"
    override val preferences = "Preferences"
    override val language = "Language"
    override val notifications = "Notifications"
    override val theme = "Theme"
    override val about = "About"
    override val version = "Version"
    override val updates = "Updates"
    override val earlier = "Earlier"
    override val expectedToday = "Expected today"
    override val delivered = "Delivered"
    override val pasteEmailHint = "Paste order email text"
    override val pasteEmailConfirm = "Extract tracking number"
    override val pasteEmailFailed = "Couldn't find a tracking number — try manual entry"
    override val deleteParcel = "Delete parcel"
    override val deleteParcelConfirmTitle = "Delete this parcel?"
    override val deleteParcelConfirmMessage = "This tracking number will be removed from your list. This can't be undone."
    override val cancel = "Cancel"
    override val delete = "Delete"
    override val trackingLookupFailed = "Couldn't find this tracking number. Check it and try again."
    override val retry = "Retry"
    override val trackingDetailFailedTitle = "Couldn't load tracking info"
    override val trackingDetailFailedMessage = "Something went wrong looking this up. Try again."
    override val noDriverInfo = "Driver contact isn't available for this parcel yet."
    override val deliveryHistory = "Delivery history"
    override val historyEmptyMessage = "No delivery history yet."
    override val parcelsUnit = "parcels"
    override val stalledBadge = "Stalled {days}d"
    override val searchHint = "Search your parcels"
    override val sortByRecent = "Recent"
    override val sortByName = "Name"
    override val noSearchResults = "No parcels match your search."
    override val statusChangeNotificationTitle = "Delivery update"
    override val tagLabel = "Tag"
    override val addTagHint = "Add a tag (e.g. Family)"
    override val saveTag = "Save"
}

object KoStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "뒤로"
    override val homeTitle = "내 택배"
    override val addTrackingBar = "운송장 번호 추가"
    override val segActive = "진행중"
    override val segDelivered = "배송완료"
    override val segAll = "전체"
    override val recent = "최근"
    override val callDriver = "기사님께 전화"
    override val emptyTitle = "첫 택배를 등록하세요"
    override val emptySubtitle = "운송장 번호를 붙여넣으면 시작됩니다. 모든 업데이트를 당신의 언어로 번역해 드려요."
    override val addParcel = "택배 추가"
    override val trackingNumber = "운송장 번호"
    override val paste = "붙여넣기"
    override val scan = "스캔"
    override val manual = "직접 입력"
    override val detectedCarrier = "감지된 택배사"
    override val change = "변경"
    override val addAnotherWay = "다른 방법으로 추가"
    override val trackingDetail = "배송 상세"
    override val showOriginal = "원문 보기 (한국어)"
    override val onboardingTagline = "한국에서의 택배를 당신의 언어로 추적하세요."
    override val chooseLanguage = "언어를 선택하세요"
    override val cont = "계속"
    override val contactDriver = "기사 연락"
    override val sendMessageAutoTranslated = "메시지 보내기 (자동 번역)"
    override val you = "나"
    override val sentInKorean = "한국어로 전송"
    override val sendMessage = "메시지 전송"
    override val settings = "설정"
    override val customs = "통관"
    override val personalClearanceCode = "개인통관고유부호"
    override val enterCustomsCode = "개인통관고유부호를 입력하세요"
    override val saveCustomsCode = "저장"
    override val copyCustomsCode = "복사"
    override val customsCodeCopied = "복사됨"
    override val preferences = "환경설정"
    override val language = "언어"
    override val notifications = "알림"
    override val theme = "테마"
    override val about = "정보"
    override val version = "버전"
    override val updates = "알림"
    override val earlier = "지난 소식"
    override val expectedToday = "오늘 도착 예정"
    override val delivered = "배송완료"
    override val pasteEmailHint = "주문 확인 메일 내용을 붙여넣으세요"
    override val pasteEmailConfirm = "운송장번호 추출"
    override val pasteEmailFailed = "운송장번호를 찾지 못했어요 — 직접 입력해 주세요"
    override val deleteParcel = "택배 삭제"
    override val deleteParcelConfirmTitle = "이 택배를 삭제할까요?"
    override val deleteParcelConfirmMessage = "이 운송장이 목록에서 삭제됩니다. 이 작업은 되돌릴 수 없어요."
    override val cancel = "취소"
    override val delete = "삭제"
    override val trackingLookupFailed = "운송장번호를 찾지 못했어요. 번호를 확인하고 다시 시도해 주세요."
    override val retry = "다시 시도"
    override val trackingDetailFailedTitle = "배송 정보를 불러오지 못했어요"
    override val trackingDetailFailedMessage = "조회 중 문제가 발생했어요. 다시 시도해 주세요."
    override val noDriverInfo = "아직 이 택배의 기사님 연락처가 없어요."
    override val deliveryHistory = "배송 이력"
    override val historyEmptyMessage = "아직 배송 이력이 없습니다."
    override val parcelsUnit = "개"
    override val stalledBadge = "{days}일째 정지"
    override val searchHint = "택배 검색"
    override val sortByRecent = "최신순"
    override val sortByName = "이름순"
    override val noSearchResults = "검색 결과가 없습니다."
    override val statusChangeNotificationTitle = "배송 상태 업데이트"
    override val tagLabel = "태그"
    override val addTagHint = "태그 추가 (예: 가족)"
    override val saveTag = "저장"
}

object ZhStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "返回"
    override val homeTitle = "我的包裹"
    override val addTrackingBar = "添加运单号"
    override val segActive = "进行中"
    override val segDelivered = "已送达"
    override val segAll = "全部"
    override val recent = "最近"
    override val callDriver = "致电快递员"
    override val emptyTitle = "追踪你的第一个包裹"
    override val emptySubtitle = "粘贴运单号即可开始。我们会把每条更新翻译成你的语言。"
    override val addParcel = "添加包裹"
    override val trackingNumber = "运单号"
    override val paste = "粘贴"
    override val scan = "扫描"
    override val manual = "手动"
    override val detectedCarrier = "识别的快递公司"
    override val change = "更改"
    override val addAnotherWay = "或用其他方式添加"
    override val trackingDetail = "配送详情"
    override val showOriginal = "查看原文（韩语）"
    override val onboardingTagline = "用你的语言追踪在韩国的包裹。"
    override val chooseLanguage = "选择你的语言"
    override val cont = "继续"
    override val contactDriver = "联系快递员"
    override val sendMessageAutoTranslated = "发送消息（自动翻译）"
    override val you = "你"
    override val sentInKorean = "以韩语发送"
    override val sendMessage = "发送消息"
    override val settings = "设置"
    override val customs = "清关"
    override val personalClearanceCode = "个人通关编码"
    override val enterCustomsCode = "请输入个人通关编码"
    override val saveCustomsCode = "保存"
    override val copyCustomsCode = "复制"
    override val customsCodeCopied = "已复制"
    override val preferences = "偏好设置"
    override val language = "语言"
    override val notifications = "通知"
    override val theme = "主题"
    override val about = "关于"
    override val version = "版本"
    override val updates = "动态"
    override val earlier = "更早"
    override val expectedToday = "预计今天送达"
    override val delivered = "已送达"
    override val pasteEmailHint = "粘贴订单确认邮件内容"
    override val pasteEmailConfirm = "提取运单号"
    override val pasteEmailFailed = "未能找到运单号 — 请尝试手动输入"
    override val deleteParcel = "删除包裹"
    override val deleteParcelConfirmTitle = "要删除这个包裹吗？"
    override val deleteParcelConfirmMessage = "该运单将从你的列表中删除，此操作无法撤销。"
    override val cancel = "取消"
    override val delete = "删除"
    override val trackingLookupFailed = "未能找到该运单号。请检查后重试。"
    override val retry = "重试"
    override val trackingDetailFailedTitle = "未能加载配送信息"
    override val trackingDetailFailedMessage = "查询时出现问题，请重试。"
    override val noDriverInfo = "该包裹暂无快递员联系方式。"
    override val deliveryHistory = "配送历史"
    override val historyEmptyMessage = "暂无配送历史。"
    override val parcelsUnit = "个"
    override val stalledBadge = "停滞 {days} 天"
    override val searchHint = "搜索包裹"
    override val sortByRecent = "最新"
    override val sortByName = "名称"
    override val noSearchResults = "没有符合搜索条件的包裹。"
    override val statusChangeNotificationTitle = "配送状态更新"
    override val tagLabel = "标签"
    override val addTagHint = "添加标签（例如：家人）"
    override val saveTag = "保存"
}

object ViStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "Quay lại"
    override val homeTitle = "Bưu kiện của bạn"
    override val addTrackingBar = "Thêm mã vận đơn"
    override val segActive = "Đang giao"
    override val segDelivered = "Đã giao"
    override val segAll = "Tất cả"
    override val recent = "Gần đây"
    override val callDriver = "Gọi tài xế"
    override val emptyTitle = "Theo dõi bưu kiện đầu tiên"
    override val emptySubtitle = "Dán mã vận đơn để bắt đầu. Chúng tôi sẽ dịch mọi cập nhật sang ngôn ngữ của bạn."
    override val addParcel = "Thêm bưu kiện"
    override val trackingNumber = "Mã vận đơn"
    override val paste = "Dán"
    override val scan = "Quét"
    override val manual = "Thủ công"
    override val detectedCarrier = "Đơn vị vận chuyển"
    override val change = "Đổi"
    override val addAnotherWay = "Hoặc thêm cách khác"
    override val trackingDetail = "Chi tiết vận chuyển"
    override val showOriginal = "Xem bản gốc (Tiếng Hàn)"
    override val onboardingTagline = "Theo dõi bưu kiện ở Hàn Quốc — bằng ngôn ngữ của bạn."
    override val chooseLanguage = "Chọn ngôn ngữ của bạn"
    override val cont = "Tiếp tục"
    override val contactDriver = "Liên hệ tài xế"
    override val sendMessageAutoTranslated = "Gửi tin nhắn (tự động dịch)"
    override val you = "Bạn"
    override val sentInKorean = "Đã gửi bằng tiếng Hàn"
    override val sendMessage = "Gửi tin nhắn"
    override val settings = "Cài đặt"
    override val customs = "Hải quan"
    override val personalClearanceCode = "Mã thông quan cá nhân"
    override val enterCustomsCode = "Nhập mã thông quan cá nhân của bạn"
    override val saveCustomsCode = "Lưu"
    override val copyCustomsCode = "Sao chép"
    override val customsCodeCopied = "Đã sao chép"
    override val preferences = "Tùy chọn"
    override val language = "Ngôn ngữ"
    override val notifications = "Thông báo"
    override val theme = "Giao diện"
    override val about = "Giới thiệu"
    override val version = "Phiên bản"
    override val updates = "Cập nhật"
    override val earlier = "Trước đó"
    override val expectedToday = "Dự kiến hôm nay"
    override val delivered = "Đã giao"
    override val pasteEmailHint = "Dán nội dung email xác nhận đơn hàng"
    override val pasteEmailConfirm = "Trích xuất mã vận đơn"
    override val pasteEmailFailed = "Không tìm thấy mã vận đơn — vui lòng nhập thủ công"
    override val deleteParcel = "Xóa bưu kiện"
    override val deleteParcelConfirmTitle = "Xóa bưu kiện này?"
    override val deleteParcelConfirmMessage = "Mã vận đơn này sẽ bị xóa khỏi danh sách của bạn. Không thể hoàn tác."
    override val cancel = "Hủy"
    override val delete = "Xóa"
    override val trackingLookupFailed = "Không tìm thấy mã vận đơn này. Kiểm tra lại và thử lại."
    override val retry = "Thử lại"
    override val trackingDetailFailedTitle = "Không thể tải thông tin vận chuyển"
    override val trackingDetailFailedMessage = "Đã có lỗi xảy ra khi tra cứu. Vui lòng thử lại."
    override val noDriverInfo = "Chưa có thông tin liên hệ tài xế cho bưu kiện này."
    override val deliveryHistory = "Lịch sử giao hàng"
    override val historyEmptyMessage = "Chưa có lịch sử giao hàng."
    override val parcelsUnit = "kiện"
    override val stalledBadge = "Đình trệ {days} ngày"
    override val searchHint = "Tìm kiếm bưu kiện"
    override val sortByRecent = "Mới nhất"
    override val sortByName = "Tên"
    override val noSearchResults = "Không có bưu kiện nào khớp với tìm kiếm của bạn."
    override val statusChangeNotificationTitle = "Cập nhật vận chuyển"
    override val tagLabel = "Thẻ"
    override val addTagHint = "Thêm thẻ (vd: Gia đình)"
    override val saveTag = "Lưu"
}

object ThStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "ย้อนกลับ"
    override val homeTitle = "พัสดุของคุณ"
    override val addTrackingBar = "เพิ่มเลขพัสดุ"
    override val segActive = "กำลังส่ง"
    override val segDelivered = "ส่งแล้ว"
    override val segAll = "ทั้งหมด"
    override val recent = "ล่าสุด"
    override val callDriver = "โทรหาคนขับ"
    override val emptyTitle = "ติดตามพัสดุชิ้นแรกของคุณ"
    override val emptySubtitle = "วางเลขพัสดุเพื่อเริ่มต้น เราจะแปลทุกอัปเดตเป็นภาษาของคุณให้"
    override val addParcel = "เพิ่มพัสดุ"
    override val trackingNumber = "เลขพัสดุ"
    override val paste = "วาง"
    override val scan = "สแกน"
    override val manual = "กรอกเอง"
    override val detectedCarrier = "บริษัทขนส่งที่ตรวจพบ"
    override val change = "เปลี่ยน"
    override val addAnotherWay = "หรือเพิ่มด้วยวิธีอื่น"
    override val trackingDetail = "รายละเอียดการจัดส่ง"
    override val showOriginal = "ดูข้อความต้นฉบับ (ภาษาเกาหลี)"
    override val onboardingTagline = "ติดตามพัสดุในเกาหลี ด้วยภาษาของคุณเอง"
    override val chooseLanguage = "เลือกภาษาของคุณ"
    override val cont = "ต่อไป"
    override val contactDriver = "ติดต่อคนขับ"
    override val sendMessageAutoTranslated = "ส่งข้อความ (แปลอัตโนมัติ)"
    override val you = "คุณ"
    override val sentInKorean = "ส่งเป็นภาษาเกาหลี"
    override val sendMessage = "ส่งข้อความ"
    override val settings = "ตั้งค่า"
    override val customs = "ศุลกากร"
    override val personalClearanceCode = "รหัสศุลกากรส่วนบุคคล"
    override val enterCustomsCode = "กรอกรหัสศุลกากรส่วนบุคคลของคุณ"
    override val saveCustomsCode = "บันทึก"
    override val copyCustomsCode = "คัดลอก"
    override val customsCodeCopied = "คัดลอกแล้ว"
    override val preferences = "ค่ากำหนด"
    override val language = "ภาษา"
    override val notifications = "การแจ้งเตือน"
    override val theme = "ธีม"
    override val about = "เกี่ยวกับ"
    override val version = "เวอร์ชัน"
    override val updates = "อัปเดต"
    override val earlier = "ก่อนหน้านี้"
    override val expectedToday = "คาดว่าจะถึงวันนี้"
    override val delivered = "ส่งแล้ว"
    override val pasteEmailHint = "วางข้อความอีเมลยืนยันคำสั่งซื้อ"
    override val pasteEmailConfirm = "ดึงเลขพัสดุ"
    override val pasteEmailFailed = "หาเลขพัสดุไม่เจอ — ลองกรอกเองดูนะ"
    override val deleteParcel = "ลบพัสดุ"
    override val deleteParcelConfirmTitle = "ลบพัสดุนี้ใช่ไหม"
    override val deleteParcelConfirmMessage = "เลขพัสดุนี้จะถูกลบออกจากรายการของคุณ และไม่สามารถย้อนกลับได้"
    override val cancel = "ยกเลิก"
    override val delete = "ลบ"
    override val trackingLookupFailed = "หาเลขพัสดุนี้ไม่เจอ ลองตรวจสอบแล้วลองใหม่อีกครั้ง"
    override val retry = "ลองใหม่"
    override val trackingDetailFailedTitle = "โหลดข้อมูลการจัดส่งไม่สำเร็จ"
    override val trackingDetailFailedMessage = "เกิดข้อผิดพลาดระหว่างการค้นหา ลองใหม่อีกครั้งนะ"
    override val noDriverInfo = "ยังไม่มีข้อมูลติดต่อคนขับสำหรับพัสดุนี้"
    override val deliveryHistory = "ประวัติการจัดส่ง"
    override val historyEmptyMessage = "ยังไม่มีประวัติการจัดส่ง"
    override val parcelsUnit = "ชิ้น"
    override val stalledBadge = "ค้าง {days} วัน"
    override val searchHint = "ค้นหาพัสดุของคุณ"
    override val sortByRecent = "ล่าสุด"
    override val sortByName = "ชื่อ"
    override val noSearchResults = "ไม่พบพัสดุที่ตรงกับการค้นหาของคุณ"
    override val statusChangeNotificationTitle = "อัปเดตสถานะการจัดส่ง"
    override val tagLabel = "แท็ก"
    override val addTagHint = "เพิ่มแท็ก (เช่น ครอบครัว)"
    override val saveTag = "บันทึก"
}

object RuStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "Назад"
    override val homeTitle = "Ваши посылки"
    override val addTrackingBar = "Добавить трек-номер"
    override val segActive = "В пути"
    override val segDelivered = "Доставлено"
    override val segAll = "Все"
    override val recent = "Недавние"
    override val callDriver = "Позвонить курьеру"
    override val emptyTitle = "Отследите свою первую посылку"
    override val emptySubtitle = "Вставьте трек-номер, чтобы начать. Мы переведём каждое обновление на ваш язык."
    override val addParcel = "Добавить посылку"
    override val trackingNumber = "Трек-номер"
    override val paste = "Вставить"
    override val scan = "Сканировать"
    override val manual = "Вручную"
    override val detectedCarrier = "Определённая служба доставки"
    override val change = "Изменить"
    override val addAnotherWay = "Или добавить другим способом"
    override val trackingDetail = "Детали доставки"
    override val showOriginal = "Показать оригинал (на корейском)"
    override val onboardingTagline = "Отслеживайте посылки в Корее — на вашем родном языке."
    override val chooseLanguage = "Выберите язык"
    override val cont = "Продолжить"
    override val contactDriver = "Связаться с курьером"
    override val sendMessageAutoTranslated = "Отправить сообщение (с автопереводом)"
    override val you = "Вы"
    override val sentInKorean = "Отправлено на корейском"
    override val sendMessage = "Отправить сообщение"
    override val settings = "Настройки"
    override val customs = "Таможня"
    override val personalClearanceCode = "Личный таможенный код"
    override val enterCustomsCode = "Введите ваш личный таможенный код"
    override val saveCustomsCode = "Сохранить"
    override val copyCustomsCode = "Копировать"
    override val customsCodeCopied = "Скопировано"
    override val preferences = "Параметры"
    override val language = "Язык"
    override val notifications = "Уведомления"
    override val theme = "Тема"
    override val about = "О приложении"
    override val version = "Версия"
    override val updates = "Обновления"
    override val earlier = "Ранее"
    override val expectedToday = "Ожидается сегодня"
    override val delivered = "Доставлено"
    override val pasteEmailHint = "Вставьте текст письма с подтверждением заказа"
    override val pasteEmailConfirm = "Извлечь трек-номер"
    override val pasteEmailFailed = "Не удалось найти трек-номер — попробуйте ввести вручную"
    override val deleteParcel = "Удалить посылку"
    override val deleteParcelConfirmTitle = "Удалить эту посылку?"
    override val deleteParcelConfirmMessage = "Этот трек-номер будет удалён из вашего списка. Это действие нельзя отменить."
    override val cancel = "Отмена"
    override val delete = "Удалить"
    override val trackingLookupFailed = "Не удалось найти этот трек-номер. Проверьте его и попробуйте снова."
    override val retry = "Повторить"
    override val trackingDetailFailedTitle = "Не удалось загрузить информацию о доставке"
    override val trackingDetailFailedMessage = "Что-то пошло не так при поиске. Попробуйте снова."
    override val noDriverInfo = "Контакт курьера пока недоступен для этой посылки."
    override val deliveryHistory = "История доставок"
    override val historyEmptyMessage = "Пока нет истории доставок."
    override val parcelsUnit = "посылок"
    override val stalledBadge = "Задержка {days} дн."
    override val searchHint = "Поиск посылок"
    override val sortByRecent = "Недавние"
    override val sortByName = "По имени"
    override val noSearchResults = "Посылок по вашему запросу не найдено."
    override val statusChangeNotificationTitle = "Обновление доставки"
    override val tagLabel = "Тег"
    override val addTagHint = "Добавить тег (например, Семья)"
    override val saveTag = "Сохранить"
}

object JaStrings : AppStrings {
    override val appName = "ParcelKR"
    override val back = "戻る"
    override val homeTitle = "あなたの荷物"
    override val addTrackingBar = "追跡番号を追加"
    override val segActive = "配送中"
    override val segDelivered = "配達完了"
    override val segAll = "すべて"
    override val recent = "最近"
    override val callDriver = "配達員に電話"
    override val emptyTitle = "はじめての荷物を追跡しましょう"
    override val emptySubtitle = "追跡番号を貼り付けて始めましょう。すべての更新情報をあなたの言語に翻訳します。"
    override val addParcel = "荷物を追加"
    override val trackingNumber = "追跡番号"
    override val paste = "貼り付け"
    override val scan = "スキャン"
    override val manual = "手動入力"
    override val detectedCarrier = "検出された配送業者"
    override val change = "変更"
    override val addAnotherWay = "他の方法で追加"
    override val trackingDetail = "配送詳細"
    override val showOriginal = "原文を見る（韓国語）"
    override val onboardingTagline = "韓国での荷物を、あなたの言語で追跡しましょう。"
    override val chooseLanguage = "言語を選択してください"
    override val cont = "続ける"
    override val contactDriver = "配達員に連絡"
    override val sendMessageAutoTranslated = "メッセージを送信（自動翻訳）"
    override val you = "あなた"
    override val sentInKorean = "韓国語で送信済み"
    override val sendMessage = "メッセージを送信"
    override val settings = "設定"
    override val customs = "通関"
    override val personalClearanceCode = "個人通関固有番号"
    override val enterCustomsCode = "個人通関固有番号を入力してください"
    override val saveCustomsCode = "保存"
    override val copyCustomsCode = "コピー"
    override val customsCodeCopied = "コピーしました"
    override val preferences = "環境設定"
    override val language = "言語"
    override val notifications = "通知"
    override val theme = "テーマ"
    override val about = "アプリについて"
    override val version = "バージョン"
    override val updates = "アップデート"
    override val earlier = "それ以前"
    override val expectedToday = "本日到着予定"
    override val delivered = "配達完了"
    override val pasteEmailHint = "注文確認メールの内容を貼り付けてください"
    override val pasteEmailConfirm = "追跡番号を抽出"
    override val pasteEmailFailed = "追跡番号が見つかりませんでした — 手動で入力してください"
    override val deleteParcel = "荷物を削除"
    override val deleteParcelConfirmTitle = "この荷物を削除しますか？"
    override val deleteParcelConfirmMessage = "この追跡番号はリストから削除されます。元に戻すことはできません。"
    override val cancel = "キャンセル"
    override val delete = "削除"
    override val trackingLookupFailed = "この追跡番号が見つかりませんでした。番号を確認してもう一度お試しください。"
    override val retry = "再試行"
    override val trackingDetailFailedTitle = "配送情報を読み込めませんでした"
    override val trackingDetailFailedMessage = "照会中に問題が発生しました。もう一度お試しください。"
    override val noDriverInfo = "この荷物の配達員の連絡先はまだありません。"
    override val deliveryHistory = "配送履歴"
    override val historyEmptyMessage = "配送履歴はまだありません。"
    override val parcelsUnit = "件"
    override val stalledBadge = "{days}日間停滞"
    override val searchHint = "荷物を検索"
    override val sortByRecent = "新しい順"
    override val sortByName = "名前順"
    override val noSearchResults = "検索条件に一致する荷物がありません。"
    override val statusChangeNotificationTitle = "配送状況の更新"
    override val tagLabel = "タグ"
    override val addTagHint = "タグを追加（例：家族）"
    override val saveTag = "保存"
}

fun stringsFor(lang: Lang): AppStrings = when (lang) {
    Lang.EN -> EnStrings
    Lang.KO -> KoStrings
    Lang.ZH -> ZhStrings
    Lang.VI -> ViStrings
    Lang.TH -> ThStrings
    Lang.RU -> RuStrings
    Lang.JA -> JaStrings
}

val LocalStrings = staticCompositionLocalOf<AppStrings> { EnStrings }
val LocalLang = staticCompositionLocalOf { Lang.EN }
