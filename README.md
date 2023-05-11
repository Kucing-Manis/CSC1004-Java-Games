# CSC1004-Java-Games
Creating A Rock Paper Scissor RPG Game 

Inspired by the game Slice & Dice.

Idea: Creating an RPG turn base game with rock, paper, and scissors (RPS)
My idea is to do an infinite level (or stage) where when the players defeat the enemy, the players will go to the next stage and fight harder enemies. The goal is to go as high as possible until the player dies (All 3 characters died). 
Game Mechanism:
My game is an infinite turned-based battle where the goal is to go as far as possible (It will collect the stage number as the highscore). At the start, 3 out of 5 characters will be chosen to be spawned. On the other hand, the enemy will also be randomly spawned. When going to the next stage, both the characters and enemies will increase their stats by random (pseudo-random) chance.
Stage finish when the enemy dies. Round finish when two parts are done (The RPS part and battle part)
Characters’ Skills:
The hero can do “Justice” which deals 1-2 times the Hero’s damage
The Wizard can do “Fireball” which deals 0-3 times the Wizard’s damage
The Healer can do “Heal” which heals all characters with the Healer’s damage
The Fighter can do “Pierce” which deals 1 times true damage to the enemy (True damage ignore shield)
The Guard can do “Guard” which deals 0-1 times the guard’s damage and also cause the enemy to only attack the Guard. When using the “Guard” skill again, the enemy will stop focusing on the guard and go back to attack randomly.
Every round, the game has two parts, which are the Rock-Paper-Scissor (RPS stage) and the battle stage 
RPS Stage:
Let's call rock, paper, and scissors shapes.
First, the player will pick one shape, The enemies will also pick one shape randomly (33% chance to win, lose, and draw. In cheated mode, the player will have 20% lose, 30% draw, and 50% win). When the player wins, the player can attack while the enemy cannot attack. When the player draws, both the player and enemy attack, with the player attacking first. Hence, if the enemy dies (Enemy hp <= 0) when the player attack, the enemy will not attack. When the player loses, the player will not be able to attack while the enemy attack.
After the RPS and the results show, it will go to the next stage, the battle stage.
Battle Stage:
Info:  
The enemy will attack the characters randomly (pseudo-random). Every 5 stages, the enemy becomes a boss and can deal 1-2 times enemy’s damage
The multiplication damage will be pseudo-random
When the player loses, the player cannot do anything. Hence, the player will only get attacked by the enemy and it will go to the RPS stage again. 
When the player draws or wins, the player will be able to choose a skill by clicking on the character (3 Characters out of 5). Each character has different skills. After choosing the skills, the player can click on the sword image to start the attack. The player will attack first. After that, if drawn, the enemy will attack.
After both have attacked, it will go back to the RPS stage and start the next round
It will end when the enemy or all 3 characters died. When the enemy dies, it will start the next stage. When all 3 characters die, it will be game over.

Story:
Not yet

