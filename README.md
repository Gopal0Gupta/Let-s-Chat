# 💬 Let's Chat – Real-Time Chat App with Profile & Status

**Let's Chat** is a sleek and modern Android chat application built with **Jetpack Compose**, **Firebase**, **MVVM Clean Architecture**, and **Dagger Hilt**. It provides a seamless chat experience with login/signup, real-time messaging, status sharing, and editable user profiles.

---

## 📱 App Preview

[![Watch the Demo](https://github.com/user-attachments/assets/b400eeeb-a072-4916-a751-6306318fa410)](https://youtube.com/shorts/ViI-2kaWcCI?feature=share)

> 🔐 `Secure, Smooth & Stylish Messaging with Let's Chat`

---

## 🚀 Features

### 🔐 Authentication
- Login / Signup using email and password via **Firebase Auth**.
- Persistent sessions with secure sign-in flows.

### 💬 Real-Time Chat
- One-on-one real-time messaging with Firebase Firestore.
- Modern chat UI with optimized performance.

### 📸 Status Sharing *(Like WhatsApp)*
- Upload and view image statuses.
- Time-limited visibility (auto-expire logic coming soon).

### 👤 Profile Management
- Editable user profile including:
  - Name
  - Phone number
  - Profile image (uploaded to Firebase Storage)
- Secure logout functionality.

---

## 🛠️ Tech Stack

| Category        | Tech Used                                 |
|----------------|--------------------------------------------|
| Language        | Kotlin                                     |
| UI Framework    | Jetpack Compose                            |
| Backend         | Firebase (Auth, Firestore, Storage)        |
| Dependency Injection | Dagger Hilt                         |
| Architecture    | MVVM + Clean Architecture                 |
| State Management| Jetpack ViewModel + State Hoisting        |
| Navigation      | Jetpack Navigation Compose                |
| Version Control | Git, GitHub                                |

---

## 📂 Project Structure (Simplified)

```
/letschat-app
├── screens/
│   ├── Chat/
│   ├── Status/
│   ├── Auth/
│   └── Profile/
├── viewmodels/
├── data/
│   ├── models/
│   └── firebase/
├── di/ (Dagger Hilt modules)
├── components/
└── utils/
```

---

## 📦 How to Run

1. Clone this repo:
   ```bash
   git clone https://github.com/Gopal0Gupta/Let-s-Chat.git
   ```
2. Open the project in Android Studio.
3. Connect Firebase project and update `google-services.json`.
4. Make sure all dependencies are synced.
5. Run on a physical device or emulator (API 26+ recommended).

---

## 🛡 Badges

![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024-brightgreen)
![Firebase Auth](https://img.shields.io/badge/Firebase-Auth-yellow)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue)
![Dagger Hilt](https://img.shields.io/badge/DI-Dagger_Hilt-red)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange)

---

## 🔧 Features in Progress

- ✅ Status expiry system (24 hrs)
- 🧾 Chat notifications via FCM
- 📁 Media sharing in chat
- 🧪 Dark mode support

---

## 🤝 Contributing

Pull requests and suggestions are welcome. For major changes, please open an issue to discuss proposed changes.

---

## 👨‍💻 Developer

- **Gopal Gupta**  
  📧 gopalgupta072004@gmail.com  
  🔗 [LinkedIn](https://www.linkedin.com/in/gopal-gupta-056585253) | [GitHub](https://github.com/Gopal0Gupta)

---
