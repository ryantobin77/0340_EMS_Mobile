# EMS Mobile App

The EMS Mobile App is a mobile application that allows EMTs to better find a hospital for their patients

## Installation

### Prerequisites
- XCode and Swift 5
- Android Studio (version here) with SDK (version here)
- Python 3.8 with pip and virtualenv
- Clone the Git repo

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
