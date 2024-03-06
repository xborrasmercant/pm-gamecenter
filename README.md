# Game Center
Game Center is an Android app where you can play and store your stats of two games, 2048 and Senku. To be able to store your scores you must create an account, which will be securely stored into the device private storage.

The app has been developed using Java and Gradle (quite painful).

## Showcase of app

### Identification Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/64944136-fb41-4717-9c8e-a32058786a43"/>
</p>
You can choose between login or register, the username must be unique. Users will be given a default profile picture, they will be able to change it later in settings.

### Hub Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/25255269-a038-40e9-917d-a95ff281d12a"/>
</p>
Once logged in you will be redirected to the Hub Screen where you can choose what game to play or to check the leaderboards or tweak some user settings. At the left side of the top bar the user profile picture and user name will be displayed.

### Settings Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/1602f569-6a6a-4801-b10d-4f5a1228dbc5"/>
</p>
Here you can change your profile picture by tapping on your current one, and also change your username and password and applying the respective changes typing the little right icon. You can also lose all your progress but keeping your account by pressing the reset scores button. If you wish to delete your entire account you can press the red "DELETE ACCOUNT" button.

### Leaderboards Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/bc7929d5-bca4-4c30-9ec3-ceb320cb1f76"/>
</p>
You can check the highscores of both games belonging to all the users registered into the game center; the list is sorted alphabetically.

### 2048 Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/bc04f387-fd5f-49d5-81eb-7244b18b287b"/>
</p>
Welcome to the 2048 screen, here you can play acheiving the maximum points you can. You can undo only a single time after a movement and also restart the current game progress. 

### Senku Screen
<p align="center">
  <img src="https://github.com/xborrasmercant/PM_GameCenter/assets/91749310/16e9c162-6dd5-484d-937e-6fd4e903e6f1"/>
</p>
Welcome to Senku game, the objective of the game is the remove all the pegs leaving only one of them to achieve the victory. Dependending on you win or lose a popup will be prompted. As for 2048 you can undo a single time for movement done and reset current game.

## Main Features

- **User Storage System**
  - User information is stored in XML in private storage.
  - User objects are parsed into an ArrayList using SaxHandler.
  - Each time that you go back or enter a activity a new instance of the activity is created, ensuring the information of the activity (username, profile picture, etc) is updated correctly.

- **Profile Picture Customization**
  - Users can change their profile picture from the device gallery.
  - The picture is saved in device private storage with the username for identification.
  - Upon account deletion, the picture is also deleted.

- **Leaderboards Display**
  - Users' information is displayed alphabetically in the leaderboards screen.
  - A RecyclerView is used for each game's leaderboard.

- **2048 Game Mechanics**
  - Score increases by the number result of the merge.
  - Correct tile merging using an "merged" array to prevent incorrect merges.
  - Tile colors are tiered based on value, with a consistent color for values above 4096.
  - Text size within tiles is adjusted based on the number's length to ensure fit.
  - A custom listener (`onMergeListener`) updates scores and user stats upon merges.
  - Game status is saved when exiting, using SharedPreferences for serialization/deserialization of grid values.

- **Senku Game Mechanics**
  - Cells are selected by tapping and moved to valid positions, checking for proper movement conditions.
  - The game board checks for game status (gameover, win, or playable) after each move.
  - Score increases for each move, representing the number of movements.
  - The game board is a GridLayout with a bidimensional matrix, with corners set to -1 for visibilty off.

- **Settings Menu Features**
  - Profile pictures can be changed by selecting a new image from the gallery, saved with the username as the filename.
  - Users can change their username and password tapping on the icon right of its input fields.
  - A "Reset Scores" option sets all registered scores for the user to 0.
  - The "Delete Account" option removes the account from the UserManager's users ArrayList and the users.xml file.
