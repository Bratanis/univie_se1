# About this project

This is my solution to the software engineering 1 project for the University of Vienna. 
The task was to create a client that communicates with a server while playing a simple game
The premise of the game is that two clients "compete" with each other without human interaction while the server mediates and ensures there is no "fowl play" going on. Both clients generate a random map, sends it to the server. The server combines the two maps, hides two treasures at random locations, and returns the resulting full map to the clients. The objective is then that each client explores the map to find their respective treasures and then find the enemy castle to "bribe" the guards and win the game. The map exploration is fully automated and the goal it that bot that acomplishes the goal faster than the enemy.
Developing this project it was important to focus on best practices and the use of patterns. Working on this I learned the practical use of various patterns such as MVC, Observer, Facade, Dependency injection, State and many others. I also learned to apply many programming conventions and best practices such as testing, logging, the SOLID principles, etc
