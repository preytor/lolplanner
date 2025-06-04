# lolplanner
lands of lords domain planner

controls: WASD or Arrow Keys to move
Right click to delete a building on the map and Left click to place a building on the map (if you hold shift while placing the building it keeps being selected)
Page Up and Page Down keys to zoom in and out (it might be laggy if you zoom out too much)

you can open the file settings.properties located in <user>\Documents\lolplanner\ to change the resolution of the program (it might be a bit buggy if you resize the window manually)

remember to dm Preytor#5090 In case of problem or bugs you might find

## Release version
You can find the latest release version at the following link:
https://github.com/preytor/lolplanner/tree/0.5/LoLPlanner/release

## Setup of the project

1. Install the latest version of Java JDK.

2. Install the latest version of Gradle.

3. Clone the repository to your local machine using Git or download the ZIP file and extract it.

4. Open a terminal or command prompt and navigate to the project directory.
```cmd
cd LoLPlanner
```

5. Run the following command to build the project:
```cmd
gradle clean desktop:build
```

6. After the build is complete, you can find the compiled application at the following path:
```cmd
LoLPlanner/desktop/build/distributions/desktop-<version>.zip
```

7. Extract the ZIP file to a desired location and run the application called bin/desktop.bat

8. You can also run the application using the .jar file located at:
```cmd
LoLPlanner/desktop/build/libs/desktop-<version>.jar
```
For that you need to use java executable to run it.

9. If you want to run the project in development mode, you can use the following command:
```cmd
gradle run
```
