# Dining Plus
For my senior thesis, I decided to create a new version of [Yale's dining hall app](https://play.google.com/store/apps/details?id=org.yaledining.app&hl=en) for Android. The existing Yale Dining app on Android is not very pleasant to use, due to dated visuals, usability issues, and several recurring bugs. Much like the existing app, the app created for this project displays the current menu items and nutritional information for each dining hall on campus, using information retrieved from the Yale Dining API. It also includes the ability to set a dining hall as a favorite, and have the app automatically navigate to that hall’s menu when launched.

## Features
The app contains several useful features not included in the original app.

### Main menu
The dining halls in the main menu are sorted by their distance to the user's location, in addition to displaying their capacity and open/closed status like the original app.

<img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic.png" width="350">

### Menu page
The menu page for each dining hall displays the menus for each meal being served in the dining hall for that day, and includes an option to favorite the dining hall. If the hall has already stopped serving a meal, it is excluded from the list.

<img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic2.png" width="350">

### Nutrition info
The item detail page displays all the nutritional information for a menu item. The page also contains a button that allows the user to follow a menu item. The app uses Android's JobService API to periodically update the dining hall menus in the background, and send the user a notification if a followed item is being served in a dining hall.

<img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic3.png" width="350"> <img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic4.png" width="350">

### Dietary preferences
The user has the ability to indicate dietary traits that they want to avoid. The app then uses this information to highlight menu items on the menu page that contain these traits, making it easier for the user to see which dishes they should avoid.

<img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic5.png" width="350"> <img src="https://raw.githubusercontent.com/amalik12/dining_plus/master/readme/pic6.png" width="350">

## External libraries
[CircleImageView](https://github.com/hdodenhof/CircleImageView)

[Firebase Job Dispatcher](https://github.com/firebase/firebase-jobdispatcher-android)
