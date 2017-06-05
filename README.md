# Luminos Engine
---
The Luminos Engine is a game engine written by indie developers, for indie developers.  It uses the modern OpenGL graphics pipeline to bring blazing fast graphics to all platforms where OpenGL 4 and higher are supported.  Entites, terrains, water, user interfaces, and more can be rendered in high resolutions and qualities without sacrificing framerates or application performance.

Luminos is currently an open source project on GitHub, and can be found this [repository](https://www.github.com/nickclark2016/Luminos).  All are welcome to suggest updates to the engine, as well as submit code for review and potential addition to the engine.  Issues are generally resolved within 24 to 48 hours.  If necessary, a hotfix will be pushed to GitHub to resolve your problem.

When possible, the development of Luminos is livestreamed on Twitch by [DragonSlayer0531](https://twitch.tv/dragonslayer0531).  If you would like to contribute to the development of the engine, but do not want to write code, consider donating to me on [PayPal](https://paypal.me/nickclark2016).

### Setting up the JAR
---

##### Eclipse
In order to set up Luminos to work with Eclipse, you will need to include the JAR in your project.  Create a folder in your project where you want to save the JAR file.  Once created, download and save the file to in the directory.  Refresh your project in Eclipse, the JAR should now appear in the navigator and project explorer.  From there, add the project to the Build Path from the Project Properties dialog.

All necessary natives are packed inside the JAR library.  This simplifies the process of adding the Luminos library to your project, thus saving set up time.

### Luminos at a Glance
---

#### Editor (in progress)
There will be two ways to use the Luminos Engine.  Currently, the engine is packaged as a JAR library.  In the future, games can also be created through the Luminos Engine Editor.

##### Scene Editor
Like in all game engines, Luminos will support a scene editor for fast entity and terrain placement.  The scene editor will also allow for the adding of audio sources, lights, and water.  The scene editor will also allow for the addition of components to various game objects.

##### Shader Node Editor
The engine's shader system is geared towards those with an artistic background.  Writing shaders is not always the easiest task in the world, so Luminos will implement a visual shader system, much like the shader system implemented in other engines.

#### Graphics
Luminos uses the modern OpenGL pipeline to render high quality graphics to the screen.  The engine contains several built in renderers and shaders, ranging from entities to terrains, text and user interfaces.  If the engine does not employ a shader or renderer that you would like, the engine is written so that the programmer can build their own rendering system from the ground up.

By default, Luminos requires the system to support OpenGL 3.3, however that can be changed in the engine's internal settings.  Only systems supporting OpenGL 3.3 and later are supported by the engine.  With machines using earlier hardware, the user will need to create their own emulation of functions and structs that are not defined.

#### User Interfaces (in progress)
The Luminos Engine supports custom made user interfaces.  The UI system is powered by Nuklear, an ANSII C library.  Several of these structs are built into the engine, but the user can create their own.  The Java binding to the functions is nearly identical to the C implementation.

#### Physics (in progress)
Currently, the Luminos does not have a physics system, however, there is a framework in place that allows for physics calculation to happen in real time.  The physics engine runs in a separate thread, along side the rendering thread.

The planned physics system is a rigidbody system.  This will allow for the engine to run at the blazing fast speeds that modern gamers demand.  There will be several defined colliders that can be attached to each game object.  By default, terrains will have a mesh collider attached, which will be used for detecting collisions between the terrain and different meshes.

#### Audio (in progress)
The Luminos Engine uses the OpenAL hardware accelerated 3D audio API for playing audio.  Users have the ability to define properties of both the audio source and the audio listener, including position and velocity.  Immersive sound is a must in modern games, and that is what the Luminos Engine is designed to incorporate.
