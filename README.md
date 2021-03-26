
> Maintenance 
>
> + Added JavaDocs in JavaDoc folder to all modules, packages and classes, to help code reviewers to understand the code.
> + Added MVC design pattern to improve the organization and flexibility of code (by implementing GameController, GameViewer and GameModel classes and put them in different packages).
> + Added Factory design pattern by implementing GameObjectFactory class in package factory for low coupling.
> + Added Singleton design pattern to the following classes: GameViewer, GameController, GameModel, MusicPlayer, GameSaver and GameLogger, to improve performance and CPU usage. 
> + Added default SYMBOL <code>' '</code>code for GameObject class to deal with undefined symbols.
> + Made class GameObject class abstract and it is inherited by concrete game object classes in package object to improve code reusability.
> + Moved accessory methods to the top of methods to achieve better organization and readability.
> + Applied JUnit Testing in test directory to make sure all the data is stored and updated correctly.
> + Renamed classes (Level to GameLevel, StartMeUp to GameModel) for higher level of clarity.
> + Capitalized all final fields and added "m_" to the beginning of class variables in all classes to make these special fields stand out.
> + Deleted unusable code and add "@deprecated" tag to useless source code for concise purpose in classes of the original source code.
> + Split Main class into Main and GameViewer and StartMeUp into GameModel and GameLevelHandler to support single responsibility.
> + Moves all resources to sub folders of "resource" folder, including music, game levels, game records and images to organize these resources better.


> Extension
>
> + Added start screen with a start button, so the user can choose when to start the game.
> + Added background color as well as floor color selection in the start screen so that the user can choose the color freely, supporting four colors for each.
> + Enabled "Save Game" option in "File" menu so that the player can save uncompleted games.
> + Enabled "Undo" option in "Level" menu so that the player regret his/her steps, but this will cost a step.
> + Enabled "Toggle Music" option in "Level" menu to improve UX.
> + Enabled "Reset Level" option in "Level" menu so that the user can restart a game level.
> + Enabled "Show High Scores" option in "Level" menu so that user can view high scores of current level.
> + Added real-time counters for time, moves count and total move count at the top of the screen so that the player is aware of his/het game statistics.
> + Added a pop up window after each level, showing time and moves count and asking player for user name if he/she scored top 10 for this level. Then update high score list of current level.
> + Added a portal for each map. The portal works as the followings: 
>   <br/>1. For the keeper, it's just a wall. 
>   <br/>2. When the keeper moves a crate into the portal, the crate will be teleported somewhere.
>   <br/>3. If there is another crate at the portal exit, then crate cannot enter the portal.
>   <br/>4. The portal exit is unknown to the player until the player uses it.
> + Used sprites for all game objects to improve interface quality. 
> + Enable the keeper to turn to his move direction to make game more vivid.
> + Enable the program to beep when user presses an invalid keyboard code to inform him/her this is not valid input.
