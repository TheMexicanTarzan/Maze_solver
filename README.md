# Maze_solver
A program that implements a breadth-first search algorithm to solve digital mazes.

DESCRIPTION:
A maze in the form of a matrix will be given in a TXT file, where 0s will be free paths and 1s will be walls; the start will be marked with an "S" and the end will be marked with an "F". 

OUTPUT:
After feeding the maze to the program, it will print the series of steps in the optimal solution. The steps will represent the direction of every single movement from 1-8 in the following manner: 
1: down-left
2: down
3: down-right
4: right
5: up-right
6: up
7: up-left
8: left

The previous structure applies for both the 8 and 4 directional modes. Where the 8 directional mode will use every number and the 4 directional will use just 2, 4, 6, 8. 

NOTE: The prints in the code have been translated but the variables have not been  translated from spanish. Since this program needs to search for a specific file in the computer, some antivirus softwares may impede the functionallity of the code. 
