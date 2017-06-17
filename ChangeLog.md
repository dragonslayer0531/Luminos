# Luminos Change Log
---

### v0.0.2a

##### Added Features
* File System
	* Assimp file loader now manages static model loading
	* Serialization utility allows for fast file loading
* Rendering and Shading
	* Nuklear IMGUI library added.  Allows for the creation of GUI components that will be used both by end users and by the engine editor.
	* Custom GUI object creation
		* Widgets are composed of an array of widget components
		* Widget components define the shapes, buttons, etc. that are to be rendered

##### Modified Features
* Window
	* Fullscreen window bug fix.  Fullscreen is now a borderless window that takes up the entire screen, rather than a true fullscreen window.
	* Window initializes Nuklear rendering context.

##### Deprecated Features
None

##### Removed Features
None

---

---

### v0.0.1a

##### Added Features
* Engine Components
	* Networking - Network framework
	* Phyiscs - Physics Framework
	* Rendering - Uses the modern OpenGL pipeline to render graphics to the screen.  Devices must support OpenGL 3.3 or later, or have the functions and structs defined by the user.
	* User Defined Components - Component Framework
* File Utilities
* Input
	* Controllers
	* Keyboard
	* Mouse
* Rendering
	* Renderers
	* Shaders
* Window and Devices

##### Modified Features
None

##### Deprecated Features
None

##### Removed Features
None