# AI Virtual Style - Virtual Try-On Assistant

A high-end, AI-powered Android application that allows users to create personalized avatars and try on premium garments in real-time using their device's camera. The app utilizes Google MediaPipe for high-precision pose tracking and Jetpack Compose for a modern, fluid user experience.

---

## ✨ Features

### Personalized AI Avatar

* Create a profile with your name, height, and weight.
* The AI automatically calculates your recommended size (S, M, L, XL).

### Real-time Virtual Try-On

* Seamlessly wear virtual clothes that follow your body's movements, tilt, and depth.

### Smart Fitting Logic

* Garments dynamically scale and stretch based on detected shoulder-to-hip proportions for a fitted look.

### AI Scanning Mirror

* Premium UI experience featuring a futuristic scanning laser and live tracking status badges.

### Dual Camera Support

* Switch between front (mirror mode) and rear cameras with automatic orientation correction.

### Premium Wardrobe

* Select from high-quality garments including Blazers, Sporty T-Shirts, and Wedding Dresses.

---

## 🚀 Tech Stack

| Component     | Technology                         |
| ------------- | ---------------------------------- |
| UI            | Jetpack Compose                    |
| Language      | Kotlin                             |
| AI/ML         | Google MediaPipe (Pose Landmarker) |
| Camera        | Android CameraX                    |
| Architecture  | MVVM (Model-View-ViewModel)        |
| Image Loading | Coil                               |
| Backend       | Firebase Firestore                 |

---

## 🛠️ Setup Instructions

### 1. Prerequisites

* Android Studio Ladybug or newer.
* Android device with API Level 24 (Nougat) or higher.
* A functional camera.

---

### 2. Firebase Configuration

This project requires Firebase for storing avatar profiles.

1. Open Firebase Console.
2. Create a new project named **VirtualTryOn**.
3. Add an Android application with package name:

```text
com.example.virtualtryon
```

4. Download the `google-services.json` file.
5. Place it inside:

```text
app/google-services.json
```

6. Enable **Cloud Firestore** from the Firebase Console.

---

### 3. MediaPipe Model

The application uses the `pose_landmarker_lite.task` model for pose detection.

Verify that the file exists at:

```text
app/src/main/assets/pose_landmarker_lite.task
```

If missing, download it from the MediaPipe Pose Landmarker documentation and place it in the assets folder.

---

### 4. Build and Run

1. Clone the repository or open the project in Android Studio.
2. Wait for Gradle Sync to complete.
3. Connect your Android device.
4. Click **Run 'app'**.
5. Grant camera permissions when prompted.

---

## 👗 Adding New Garments

To add custom outfits to the wardrobe:

### Step 1

Place a PNG image with a transparent background inside:

```text
app/src/main/res/drawable/
```

### Step 2

Add a new garment entry in:

```text
com.example.virtualtryon.domain.GarmentType.kt
```

### Step 3

Rebuild the application.

The new garment will automatically appear in the wardrobe selection grid.

---

## 📂 Project Structure

```text
app/
├── src/main/
│   ├── assets/
│   │   └── pose_landmarker_lite.task
│   ├── java/com/example/virtualtryon/
│   │   ├── domain/
│   │   ├── data/
│   │   ├── presentation/
│   │   └── camera/
│   └── res/
│       ├── drawable/
│       └── values/
└── google-services.json
```

---

## 📸 Key Capabilities

* Real-time body pose detection.
* Dynamic outfit scaling and alignment.
* Avatar profile management.
* Camera switching support.
* Firebase cloud storage integration.
* Modern Material 3 UI using Jetpack Compose.

---

## 📄 License

This project is developed for educational, internship assessment, and portfolio purposes.
