# GitHub-Android
A GitHub Android client that uses the GitHub API to search for users given a username query and upon clicking on a username, displays the name, company, location and all the repositories neatly in a list with repository name, age and description.

The name of the app is `Outlab9`.

### Requirements(for running the app):
1.Requires API level >=23.

2.Requires Internet permission.

The `output-apk` folder contains the APK file. Download it on your device and run it.(make sure to enable installation from unknown sources and grant it internet access).

### Requirements(for building the project):
1. Android Studio
2. Gradle builds enabled

Build the Android project `GitHub-Android` using Android Studio. `Gradle` will build it automatically for you. Make sure to choose `Gradle` expicitly if `Gradle` is not automatically recognized.
Then the project can be run, packaged into an APK, etc.

## Using the app

On the first screen you will be greeted with a textbox to enter the username you wish to search for. Enter a search query and hit Search.

<img src="https://github.com/ajd12342/GitHub-Android/blob/master/EnterQuery.jpg" width="540" height="927.5" />

The top 30 or so ( i.e. 1 page of) results will be displayed. Click on any username.

<img src="https://github.com/ajd12342/GitHub-Android/blob/master/List.jpg" width="540" height="929.5" />

The user's full name, company, location and a list of all repositories with name, age and description is displayed nicely.

<img src="https://github.com/ajd12342/GitHub-Android/blob/master/RepoInfo.jpg" width="540" height="930" />
