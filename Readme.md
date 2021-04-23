# EMS Mobile App

The EMS Mobile App is a mobile application that allows EMTs to better find a hospital for their patients. The application consists of 3 components: a Django backend, an iOS app, and an Android app

## Release Notes

## Install Guide

### Prerequisites
- For the iOS app, a Mac Computer is needed
- Install XCode and Swift 5
- Install Android Studio with SDK 29
- Install Python 3.8 with pip and virtualenv
- Clone the Git repository

### Download Instructions
1. In terminal, navigate to the folder where you would like to save the project
2. Clone the project to this location using ```git clone https://github.com/ryantobin77/0340_EMS_Mobile.git```
3. A repository named "0340_EMS_Mobile" now exists

### Dependent libraries
- All dependent libraries can be found in the repo directory at ```0340_EMS_Mobile/Backend/EMS_Django_Backend/requirements.txt``` 
- See Build Instructions for the Django Backend below to install dependent libraries 

### Build Instructions
#### Django Backend
1. Initialize your virtualenv with ```virtualenv venv``` in the root directory. Do not push this to Git
2. Activate virtualenv with ```source venv/bin/activate```
3. Go into the backend directory ```cd Backend/EMS_Django_Backend```
4. Install dependent libraries with ```pip install -r requirements.txt```
5. Run migrations ```python manage.py migrate```
6. The Django Backend is now built and ready to run

#### iOS App
1. Open up XCode
2. Click File > Open
3. Navigate to the repo's root directory and go into "EMS\ iOS\ App" and click on "EMS\ iOS\ App.xcodeproj" and click open
4. Within XCode, click Product > Build to build the project

#### Android App
1. Open “EMS_Android_App” folder using Android Studio
2. Open the SDK Manager (icon in the top navigation bar)
    - Make sure the SDK for version 29 is installed and selected
4. Go to File > Project Structure
    - Ensure Compile SDK Version is 29
    - Build Tools Version is 30.0.3
6. Open the AVD Manager (icon in the top navigation bar)
    - If there isn’t an emulator installed, create a virtual device with the SDK version <= 29
7. At the top of the screen, click the drop down next to the hammer icon
    - Select Edit Configurations
    - A configuration named app with module EMS_Android_App.app that deploys the default APK should be created and selected
    - Note: If there isn't a drop down, create the "Add Configuration" button and create a configuration with these specifications
8. Make sure your emulator is selected in the second drop down
9. Press the build button (hammer icon) to build the project


### Installation Instructions
- No additional installation is required to run the application

### Run Instructions

#### Run Instructions for Django Backend
In terminal from the root directory of the repo with virtualenv activated, run the following:

```bash
cd Backend/EMS_Django_Backend
python manage.py runserver
```

#### Run Instructions for the iOS App
1. Make sure the Django Backend is running
2. Open up XCode
3. Click File > Open
3. Navigate to the repo's root directory and go into "EMS\ iOS\ App" and click on "EMS\ iOS\ App.xcodeproj" and click open
4. In the top left of the XCode window, select a simulator to run the application
5. Press the play button to run the app
6. Wait a few seconds and the iOS simulator will open with the application

#### Run Instructions for the Android App
1. Make sure the Django Backend is running
2. Open the directory "EMS_Android_App" using Android Studio
3. Click the "Run 'app'" button (it looks like a green play icon)
4. Your emulator should open and EMS Mobile App will open

### Troubleshooting

#### Django Backend

#### iOS App
1. Ensure the latest version of XCode is installed along with Swift 5. Earlier versions of Swift are not compatible with this app.
2. Our application is dependent on location. Ensure the simulator has a location to use:
    - When the simulator opens, click on the simulator. Click Features > Location > Custom Location
    - We recommend using the following location: lat = 33.77718 and long = -84.39235

#### Android App
1. Under SDK tools within the SDK Manager, make sure Android ADK Build tools, Android Emulator, Android ADK Platform-Tools, and Android ADK Tools are installed
2. Go to Project Tab within Project Structure 
    - Ensure Android Gradle Plugin Version is 4.1.2
    - Ensure Gradle Version is 6.5
