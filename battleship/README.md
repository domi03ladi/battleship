## Game Setup
### 1. Setup your Database with the file *[database-setup.txt](database-setup.txt)*
### 2. Create two Players
#### game-controller - /api/games/createPlayer
````JSON
{
  "name": "Dominik"
}
````

### 3. Create a Grid
#### grid-controller - /api/grids/create
````JSON
{
  "width" : 5,
  "height" : 5
}
````
### 4. Create the Game
#### game-controller - /api/games/create
````JSON
{
  "player1": {
    "id": 1
  },
  "player2": {
    "id": 2
  },
  "grid": {
    "id": 1
  }
}
````
### 5. Position your ships
#### ship-controller - /api/ships/create
````JSON 
{
  "player" : {
    "id": 2
  },
  "game" : {
    "id": 1
  },
  "x" : 0,
  "y" : 3
}
````
### 6. Take your shoots and win the game
#### shot-controller - /api/shots/create
````JSON
{
  "player" : {
    "id": 1
  },
  "game" : {
    "id": 1
  },
  "x" : 0,
  "y" : 3
}
````
### 7. Overview for played Games
#### game-controller - /api/games/history/game/{gameId}

### 8. I added a *[Battleship.postman_collection.json](Battleship.postman_collection.json)* file with some Example Request.

#### Info
**~** marks a field on the grid \
**s** marks a ship of player 1 \
**x** marks a shoot of player 1 \
**u** marks a ship of player 2 \
**y** marks a shoot of player 2
