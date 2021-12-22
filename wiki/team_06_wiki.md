# Group 06 Wiki

## Delivery status updates 

All features were implemented with at least one other member present to proofread and help.

#### 19/9/21: 

Main menu - Ojas

Select topic - Jia

Start quiz - Jia

Get words - Ojas

#### 20/9/21:
Quiz functionality - Jia, Ojas, Ben

Score keeping - Jia

After Quiz - Ojas

Change TTS speed - Ben

#### 23/9/21:

Add macron button - Ben

Multi-threading - Ojas

Throughout the project: 

Refactoring - Ben

Testing/documentation - Jia

Styling - Ojas

## Personal comments:

### Tips from Jia: 

When a bug/error occurs make sure that you are testing your most recent code. I noticed that this was a issue that occured sometimes in our developement process. We were not running the most recent code which caused issues that didnt exist.

When your conditional statements involve integers make sure that you have the correct operands so that your program behaves as intended. For example our score ranges for the reward message were inclusive value ranges. 

Also pay close attention to your while and for loop termination conditions. For example if you are counting from 0 and want to iterate 5 times you will terminate when count reaches 4. An easy way to determine it is to run an example in your head.

### Tips from Ben:

When designing your function try to write methods that aren't dependent on other methods first, so you can test them and make sure they are robust before moving on. Otherwise, you might write a method without being able to test it properly.

It is useful to use an atomic integer when working with a listener.

The numbers used when changing the speed of tts in festival are quite counter intuitive. Usually numbers relate to how much you are speeding up/slowing down the speech e.g. 2x speed is faster and 0.5x speed is slower, but in festival the numbers do the opposite. It is important to test and not make any assumptions.

### Ojas' thoughts:

It is definitely much easier to have one controller per fxml file created. This caused a lot of headaches early on with null pointers etc. The easiest fix is to just have a controller for each scene.

Festival messes with a lot of the GUI elements updating, it'd be nice to look more into threads etc for the final project. I got threading to work in one part of this assignment (the quiz scene loads BEFORE the festival tts speaks) but ideally festival would ALWAYS be on a seperate thread. 

For commands which are going to be very commonly used, it is easiest to create a seperate class for them and create static instances of them, so they can be used by all the controllers. This results in less duplication, shorter code and easier refactoring/bug fixes (did this for Festival and SceneController objects in our code)

Writing the scm files within the program and then using 'festival -b ...scm' was the only way we were able to use 2 different languages, so I'd be curious to see how others did it.
