# Yale Dining Plus
> A new version of the Yale Dining app for Android.

[<img src="https://cdn.rawgit.com/steverichey/google-play-badge-svg/master/img/en_get.svg" width="25%">](https://play.google.com/store/apps/details?id=com.adisa.diningplus)

The [existing Yale Dining app](https://play.google.com/store/apps/details?id=org.yaledining.app) on Android is not very pleasant to use, due to outdated graphics, usability issues, and several recurring bugs. This project replaces the official app by displaying current menu items and nutritional information for each dining hall on campus, using information retrieved from the Yale Dining API. Beyond this, it adds new features such as the ability to set a dining hall as an auto-opening favorite, marking menu items as favorites to be notified when they're served, and setting up visual warnings when a recipe violates your dietary restrictions.

## Features
Yale Dining Plus contains several useful features not included in the original app.

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

## Contributors
This application was created by [Adisa Malik](https://adisamalik.com) for his 2017 senior thesis. It is maintained by [Erik Boesen](https://github.com/ErikBoesen).
