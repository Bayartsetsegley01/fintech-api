# Ledger — FinTech Transaction API Frontend

Минимал, цагаан-хар өнгийн React интерфейс — [fintech-api](https://github.com/Bayartsetsegley01/fintech-api) backend-тэй ажиллана.

## Tech Stack
- React 18 + Vite
- Tailwind CSS
- Montserrat (Google Fonts)

## Ажиллуулах

### 1. Backend-ээ эхлээд асаа
```bash
cd ../api
./mvnw spring-boot:run
```
Backend `http://localhost:8080` дээр ажиллаж байх ёстой.

### 2. Backend дээр CORS тохируулах
`backend-cors/WebConfig.java` файлыг backend project-ийн
`src/main/java/com/fintech/api/config/WebConfig.java` руу хуулна
(`config` folder байхгүй бол шинээр үүсгэ). Энэ нь `localhost:5173`-аас
ирэх хүсэлтийг зөвшөөрнө.

### 3. Frontend суулгаж асаах
```bash
npm install
npm run dev
```
Browser дээр `http://localhost:5173` нээгдэнэ.

## Боломжууд
- Хэрэглэгч жагсаах, шинээр үүсгэх
- Хэрэглэгчийн дансуудыг харах, шинэ данс нэмэх
- Дансны үлдэгдэл болон гүйлгээний түүх харах
- Мөнгө хийх (deposit), татах (withdraw), шилжүүлэх (transfer)
