# EMS Mobile App

The EMS Mobile App is a mobile application that allows EMTs to better find a hospital for their patients. The application consists of 3 components: a Django backend, an iOS app, and an Android app

## Release Notes

### Software Features

#### Display List of Hospitals
The main page displays a list containing the following information about each hospital in Georgia:
- Name 
- Nedocs score - a measure of the severity of overcrowding in the hospital’s emergency department (Normal, Busy, Overcrowded, Severe)
- Specialty centers (i.e. Adult Trauma Center, Primary Stroke Center, etc.)
- Diversions – whether the hospital is redirecting certain types of ambulance traffic to other hospitals due to overcrowding 
- Distance to hospital from current location

You can expand any of the hospitals to see more information about that specific hospital:
- Phone number 
- Street address 
- Specific information about diversions
- Full-length descriptions of specialty centers
- County 
- EMS region 
- Regional Coordinating Hospital 
- Time of last data update - note this is the time that the hospital last input their data, not the last time the application refreshed data from the database, and will be unique to each hospital

#### Pin
You can pin any number of hospitals to the top of the list to more easily monitor their status and can choose to snip them again at any time. Your pinned hospitals will remain at the top of the list even if you apply a different sort on the hospital list and will be sorted within themselves.

#### Filter
You can open the filter menu by selecting the filter icon in the top right corner of the main page to filter the list of hospitals on any combination of the following attributes:
- Specialty centers
- EMS region
- County
- Regional coordinating hospital
You can view all currently applied filters in the bottom bar of the main page. You can remove an individual filter by pressing the “x” on a specific filter card or clear all filters using the button in the bottom right corner of the main page.

#### Sort
You can open the sort menu by selecting the sort icon in the top right corner of the main page to sort the list of hospitals on one of the following attributes
- A-Z (by name)
- Distance from current location (from nearest to furthest)
- Nedocs score (from normal to severe)

#### Search
You can open the search bar by selecting the search icon in the top right corner of the main page. You can then enter a search term to display only hospitals that contain your search term in the name.

#### Refresh
The app will automatically refresh all hospital data from the database when the app is first opened and every time it is reopened. Additionally, you can swipe down on the hospital list to refresh the data at any time.

#### Contact
After expanding a specific hospital’s information, you have a couple of options to make contact with that hospital. You can click on the hospital’s phone number to open your device’s phone application and make a call to the hospital or you can click on the hospital’s address to open your device’s mapping application and navigate to the hospital. Additionally, you can call the Georgia Coordinating Center by selecting the phone icon in the top toolbar on the main page.

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
