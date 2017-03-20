# simon

**Simon** is a copy of the classic memory game made by Hasbro. The user and the computer take separate turns, with the computer on its turn adding to a sequence and the user trying to replicate that sequence. The interface is a panel of four buttons that light up when either the user presses one or the computer displays the current pattern. 
To do this in Java, a queue ADT is implemented. The color order is stored in such a queue, and a new color is randomly generated and enqueued to this every time. To display the pattern and check it against user input, it is copied to a temporary queue which is then dequeued until it is empty.
The program itself runs on a timer, which executes a block of code and then repaints the graphics to match the state of the game, before waiting 300 milliseconds to do it again. 
User input is handled with a mouse listener and polygons, which are invisible to the player but contain objects that are drawn with graphics. 
