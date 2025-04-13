# Drone RTSP Viewer App

## 📱 About the App

As Vyorius Drones Private Limited, one of the essential features required is **live video streaming from the drone to the pilot’s mobile application**. One of the most commonly used formats for receiving such streams is **RTSP (Real Time Streaming Protocol)**.

This app is designed to:
- 🔴 **Stream RTSP video live** within the app
- 📹 **Record the video stream** and save it locally
- 🪟 **Enter Picture-in-Picture (PiP)** mode for multitasking

---

## ✅ Features

### 1. 📡 RTSP Streaming
- Enter an RTSP URL in the text field at the top.
- Tap **Play** to start live viewing directly inside the app.
- Uses [libVLC](https://code.videolan.org/videolan/libvlc) under the hood for smooth playback.

### 2. 🎥 Video Recording
- Tap the **Start Recording** button to begin saving the stream.
- Tap **Stop Recording** to save the file to your phone's **Movies/RTSP_Recordings** directory.
- Video is saved in `.mp4` format and named with a timestamp.

### 3. 🪟 Picture-in-Picture (PiP) Mode
- Tap the **PiP Button** to shrink the video into a floating window.
- Continue to use other apps while watching the stream in a corner.
- Auto-enters PiP on home press (Android 8.0+).

---

## 📸 Screenshots

| Main View | 
|-----------|
| ![WhatsApp Image 2025-04-14 at 00 05 28_eb3cbbcf](https://github.com/user-attachments/assets/f7d3a325-330f-40bb-8628-4429f9e6102a)
) |



---

## 📂 Storage Location
Recorded videos are saved to: /storage/emulated/0/Movies/RTSP_Recordings


---

## ⚙️ Tech Stack

- Java (Android)
- Android Studio (Meerkat 2025)
- libVLC (VideoLAN)
- Target SDK: 34+

---

## 🚀 How to Build

1. Clone this repository
2. Open in Android Studio
3. Make sure internet permission is enabled in `AndroidManifest.xml`
4. Connect a physical device 
5. Build and run

---

## 🛠 Permissions Required

- `INTERNET`: To stream the RTSP video
- `WRITE_EXTERNAL_STORAGE` *(For Android 10 and below)*: To save recordings
- `READ_MEDIA_VIDEO` *(For Android 13+)*: To access video files

---

## 👤 Developed by

Snehadrita Pal  


