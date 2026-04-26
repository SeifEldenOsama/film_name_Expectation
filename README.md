# Film Name Expectation Game

## Description

This project is a **Film Name Guessing Game** developed using **JavaFX** for the interactive user interface and **MySQL** for efficient data management. Players are challenged to predict film names based on provided actors, with an integrated hint system to assist when needed. The game features a coin economy, level progression, and a dynamic level selection screen.

## Features

*   **Interactive UI**: Built with JavaFX for a smooth and engaging user experience.
*   **Film Guessing**: Players guess film names based on a given actor.
*   **Hint System**: A coin-based hint system allows players to reveal letters in the film title when stuck.
*   **Coin Economy**: Earn coins for correct guesses and spend them on hints.
*   **Level Progression**: Advance through various levels, each presenting a new film to guess.
*   **Dynamic Level Selection**: A dedicated screen to browse and select unlocked levels.
*   **Background Music and Sound Effects**: Enhances the immersive gaming experience.
*   **MySQL Integration**: Stores and manages game data, including film information, actor details, current level, and coin count.

## Technologies Used

*   **JavaFX**: For building the graphical user interface.
*   **MySQL**: For database management and persistence of game data.
*   **Java**: The core programming language.

## Project Structure

```
film_name_Expectation/
├── bin/                            # Compiled Java classes and resources
│   ├── FilmGame/                   # Compiled game logic classes
│   ├── database/                   # Compiled database management classes
│   ├── images/                     # Compiled image assets
│   └── sound/                      # Compiled sound assets
├── src/                            # Source code
│   ├── FilmGame/                   # JavaFX application logic
│   │   ├── Main.java               # Application entry point
│   │   ├── MainFrame.java          # Main menu screen
│   │   ├── PlayFrame.java          # Core gameplay screen
│   │   ├── LevelsFrame.java        # Level selection screen
│   │   ├── MusicAndSound.java      # Audio utility class
│   │   └── filmdata.java           # Film data model
│   ├── database/                   # Database connection and management
│   │   ├── DataBaseConnection.java # Handles MySQL connection
│   │   └── ManageDataBase.java     # Manages game data in MySQL
│   ├── images/                     # Image assets (icons, backgrounds)
│   └── sound/                      # Sound assets (click, correct, win)
└── README.md                       # This README file
```

## How to Run

To run this application, you will need:

1.  **Java Development Kit (JDK)**: Version 11 or higher, with JavaFX modules.
2.  **MySQL Server**: A running MySQL instance.
3.  **MySQL JDBC Driver**: To connect Java to MySQL.

**Database Setup:**

*   Create a database named `films`.
*   Create a table `film_actors` with columns `id` (INT, PRIMARY KEY, AUTO_INCREMENT), `film` (VARCHAR), and `actor` (VARCHAR).
*   Create a table `game_state` with `current_level` (INT).
*   Create a table `coins` with `value` (INT).
*   Populate the `film_actors` table with your desired film and actor data.

**Application Execution:**

1.  Clone the repository:
    ```bash
    git clone https://github.com/SeifEldenOsama/film_name_Expectation.git
    cd film_name_Expectation
    ```
2.  Ensure your `DataBaseConnection.java` file (located in `src/database/`) is configured with the correct MySQL username and password. The current connection string expects `user=root` and `password=********` for a database named `films` on `localhost`.
3.  Compile and run the JavaFX application. This typically involves using an IDE like IntelliJ IDEA or Eclipse, or compiling from the command line, ensuring the JavaFX SDK and MySQL JDBC driver are correctly configured in your project's build path.
