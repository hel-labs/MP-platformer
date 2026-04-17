# MP-platformer
A platformer game built using Java for CSE215 Lab Final Project. Inspired by the classic experience of Mario, Pokemon, and Undertale.

# Project Details
Course: CSE215\
Semester: Spring 2026\
Section: 07\
Instructor: HSM\
Team Members: [Fahim Muntasir Galib, 2522855](https://github.com/not-hexil); [Sami Sharif Arka, 2521620](https://github.com/samiarka)\
Playtester: [Nur E Samdani Samin, 2522090](https://github.com/samd4ni)

# Technical Details
1. Java
2. Build Tool: Gradle
3. GUI: Java Swing

# Features
## Mario inspired sidescroller/platformer overworld
## Pokemon inspired turn-based battle system
## Undertale inspired conversation system 
## Persistent local leaderboard
## Minimal unique ending based on playstyle (Agrressive, Neutral, Passive)
## Inventory and Point System

# Technical Implementations
1. Inheritance and Abstraction:
A base abstract class entity (Defines position, health etc)
A base attack inference (Declares specific attack types of each enemyType for which isWandering = true)
Player class, enemy abstract class(Defines riddlebosses and wandering enemies) will inherit from entity.
Specific enemies will further inherit from enemy abstract class.

An abstract gameState will define the general state behaviour. Specific states (Overworld, Battle, Riddle etc) will inherit from it.

2. Encapsulation:
Sensitive data of different classes (Health, specific tile damage, battle engine etc) will be encapsulated using private modifier, and only accessible thrugh specific getter/setter methods.
Player record will be stored in a file as read-only mode.

3. Polymorphism:
A single executeAction(parameter) will handle different action types flee, fight, spare, talk etc through overloading, and sub-class specific same parameterized methods will be implemented appropriately through overriding.

4. Interface:
A dice-roll system, and battle damage system interface will be implemented, and each battle actions will implement it.

5. Error Handling:
Specific error handling for each state, with specific custom errors for probable errors will be implemented.

6. File I/O:
Player record storage, dynamic map using tiled json output using Java's built in file handling system.

7. GUI: 
A GUI will be implemented for ease of use and asset handling.

