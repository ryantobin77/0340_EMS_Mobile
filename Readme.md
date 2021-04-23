# EMS Mobile App

The EMS Mobile App is a mobile application that allows EMTs to better find a hospital for their patients

## Install Guide

### Prerequisites
- XCode and Swift 5
- Android Studio with SDK 29
- Python 3.8 with pip and virtualenv
- Clone the Git repo

## Download Instructions
1. In terminal, navigate to the folder where you would like to save the project
2. Clone the project to this location using ```git clone https://github.com/ryantobin77/0340_EMS_Mobile.git```
3. A repository named "0340_EMS_Mobile" now exists

### Setting up the backend (Do this only once)
1. Initialize your virtualenv with ```virtualenv venv``` in the root directory. Do not push this to Git
2. Activate virtualenv with ```source venv/bin/activate```
3. Install dependencies with ```pip install -r requirements.txt```
4. Go into the backend directory ```cd Backend/EMS_Django_Backend```
5. Run migrations ```python manage.py migrate```
6. Create a superuser ```python manage.py createsuperuser```
7. Fill out and remember the necessary superuser credentials

### Setting up the iOS App

### Setting up the Android App
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

## Running the App

### Running the backend
From the root directory with virtualenv activated, run the following:

```bash
python manage.py runserver
```
#### Adding data to the backend
1. Navigate to http://127.0.0.1:8000/admin/ in your browser
2. Login to the admin page with your superuser credentials
3. Create a Specialty Center
4. Optionally Create a Diversion
5. Create a Hospital
6. Navigate to http://127.0.0.1:8000/hospitals/ in your browser to see your data in a Json format

### Running the iOS App
1. Make sure the backend is running

### Running the Android App
1. Make sure the backend is running
2. Open the directory "EMS_Android_App" using Android Studio
3. Click the "Run 'app'" button (it looks like a green play icon)
4. Your emulator should open and EMS Mobile App will open

## Troubleshooting

### Android App
1. Under SDK tools within the SDK Manager, make sure Android ADK Build tools, Android Emulator, Android ADK Platform-Tools, and Android ADK Tools are installed
2. Go to Project Tab within Project Structure 
    - Ensure Android Gradle Plugin Version is 4.1.2
    - Ensure Gradle Version is 6.5
