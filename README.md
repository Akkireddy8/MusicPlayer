#**TUNE STREAM**<br>
#**GROUP 03**<br>
Akkireddy Akshitha Katyaini Reddy<br>
Ashwitha Reddy Polasani<br>
Karthik Kasula<br>
Pallavi Pokuri<br>

**Problem/Issue Addressed**<br>
Many users struggle with finding an easy-to-use, personalized music streaming app that lets them create, manage, and share playlists, and access their tracks. Tune Stream aims to provide a simple and interactive platform for users to enjoy music seamlessly.<br>

**Activities and Intended Purposes**<br>
**Adaptor Activity**<br>
•	Purpose: Manage and display specific data in a music app, such as notifications, playlists, shared playlists, and song details.<br>
**Auth Activity**<br>
•	Purpose: Allows users to SignUp, Login, and Forgot Password.<br>
**Home Activity**<br>
•	Purpose: Enables users to access Home Page, edit their profile, get notifications.<br>
**Model Activity**<br>
•	Purpose: The Model Activity in an app with Notification Model, Playlist Model, Song Model, and User Model handles and manages the data structure for notifications, playlists, songs, and user information. <br>
**Player Activity**<br>
•	Purpose: The Player Activity in an app with a Music Button Sheet Controller, Music View Model, Player Activity, and Player Controller manages the music playback interface, controls, and data.<br>
**Playlist Activity**<br>
•	Purpose: Allows users to add, create, share and get details of song in playlists.<br>

**Prerequisites**</br>
Before you begin, ensure you have:</br>
•	Android Studio (latest stable version recommended)</br>
•	Java Development Kit (JDK) version 11 or higher</br>
•	An Android Emulator or physical device running Android 8.0 (API level 26) or above</br>

**Installation**</br>
1.**Clone the Repository**:</br>
Copy code</br>
git clone <repository-url>

2.**Open the Project**:</br>
•	Launch Android Studio.
•	Select Open an Existing Project and navigate to the TuneStream directory.

3.**Set Up Dependencies**:</br>
•	Android Studio will automatically sync the Gradle files. If not, click on File > Sync Project with Gradle Files.</br>

**Building and Running the App**</br>
1.**Build the Project**:</br>
•	Click on the Build menu and select Make Project (or use the shortcut Ctrl+F9).

2.**Run the App**:</br>
•	Connect your Android device via USB or start an emulator.</br>
•	Click on the Run button (green triangle) or use the shortcut Shift+F10.</br>

**Project Structure**</br>
app/src/main/java: Contains the source code, including activities, fragments, and ViewModels.</br>
app/src/main/res: Stores resources like layouts, drawables, and strings.</br>
app/src/main/assets: Contains local SQLite database files.</br>
app/src/main/AndroidManifest.xml: App configuration file.</br>

**Troubleshooting**</br>
•	If the Gradle sync fails, ensure your internet connection is stable and that Android Studio is configured to use the correct Gradle version.</br>
•	If the emulator crashes, allocate more RAM or use a physical device.</br>
