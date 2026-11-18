# Da Vinci Cipher — Android APK

โปรเจกต์เกมถอดรหัสภาษาไทย 5 ด่าน สร้างด้วย Android (Java)

## Build APK อัตโนมัติ
ทุกครั้งที่มีการ push ไปยัง branch `main` หรือสั่ง workflow ด้วยตนเอง GitHub Actions จะ:
1. ติดตั้ง JDK 17 และ Android SDK 35
2. ติดตั้ง Gradle 8.9
3. สั่ง `assembleDebug`
4. อัปโหลดไฟล์ APK เป็น Workflow Artifact ชื่อ `DaVinci-Cipher-debug`

## ดาวน์โหลด APK
เปิดแท็บ **Actions** → เลือกรายการ **Build Android APK** ที่สำเร็จ → ส่วน **Artifacts** → ดาวน์โหลด `DaVinci-Cipher-debug` (ZIP) แล้วแตกไฟล์เพื่อรับ `app-debug.apk`

> หมายเหตุ: รุ่นแรกนี้เป็น Debug APK สำหรับทดสอบ ยังไม่ได้เซ็นเป็น Release APK สำหรับเผยแพร่บน Play Store
