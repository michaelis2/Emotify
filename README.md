# EMOTIFY: Mood Tracking App using CNN 📱✨

## Overview

Emotify is a mobile application built to help users monitor their emotional well-being.
With a smart Convolutional Neural Network (CNN) for facial emotion detection, Emotify empowers users to:

📷 Capture their mood using the camera
📝 Journal their emotions
📊 Visualize mood trends
🆘 Access mental health resources
All wrapped in a user-friendly and intuitive interface 💗

## FEATURES 

- User Authentication
Users can sign up, log in, and manage their accounts.

- Emotion Recognition
Detects user's facial emotions using a trained CNN model and device camera with CameraX.

- Manual Emotion Logging
Users can manually select or correct the detected emotion if needed.

- Mood Tracker
Captures and logs daily moods, allowing users to view a history of their emotions.

- Journaling
Users can write daily journal entries.

- Analytics 
Displays emotion trends over time.

- Support Hotlines Access
Provides emergency and emotional support hotline numbers based on user's selected country.

## Permissions and Data Usage

To provide core functionality, the application request the following permissions:

- Camera Access: Required to capture facial expressions for real-time emotion detection. Camera data is processed locally with any image captured stored in Firebase Firestore and not shared without user consent.
- Location Access: Used to offer mental health resources relevant to the user’s region. Location data is used only while the app is in use and is never stored or shared.
- The app adheres to strict privacy standards. All permissions will be clearly requested, and users can opt out at any time via device settings. No data is collected or transmitted without explicit user consent.

## 🛠️ Prerequisites

1. Mobile Application 
- Android Studio installed
- Minimum Android SDK: 35 
- Recommended Emulator: Medium Phone API 35
- Internet, Camera, Location Permission allowance in AndroidManifest.xml
  
2. CNN Backend
- Python 3.7 or newer installed.

Python Libraries:

Install the following Python packages:
pip install flask
pip install werkzeug
pip install tensorflow
pip install opencv-python
pip install numpy

You can install Python dependencies using pip:
 ### `pip install tensorflow`

- Model Files:
fer.json — CNN model architecture.
fer.h5 — Pre-trained model weights.

- Face Detection:
haarcascade_frontalface_default.xml — Face detection file from OpenCV.

## 📖 User Manual

STEP 1) Run the flask server

- Open terminal in the Emotify directory and type in the following:
  
 ### ` python3 fyp1.py --host=0.0.0.0 --port=5000 ` (for Macbook)

Step 2) Run the app in Android Studio

- Open Android Studio > Select Emotify/ folder
- Sync Gradle when prompted
- Wait for "Sync finished"
- Start the emulator (Medium Phone API 35)
- Click the green ▶️ Run button

Step 3) Getting Started

- Launch app → Create account or log in
- After login → Welcome to your Home Screen 💫
-Navigate to camera or journaling features
- Emotions auto-detected — or choose your own
- Track, write, and reflect 💗

## 🔓 Permissions
- A pop up will appear to ask user to grant access to location once user reaches homescreen.
- Another pop up will appear to ask user for camera permission once user gets to the camera feature page. 
- These pop up should appear only once, unless the app gets recompiled.

## 🧚🏻‍♀️ Credits
Made with love by
[ Amanda Batrisyia Irawan ] 💕
Thank you for using Emotify! 🌸

